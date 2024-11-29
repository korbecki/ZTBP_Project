package pl.ztbd.project.cassandra.service;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.ztbd.project.api.UserAPI;
import pl.ztbd.project.api.dto.request.*;
import pl.ztbd.project.api.dto.response.LoginResponse;
import pl.ztbd.project.api.dto.response.MessageResponse;
import pl.ztbd.project.api.dto.response.RefreshTokenResponse;
import pl.ztbd.project.cassandra.entity.CassandraRefreshTokenEntity;
import pl.ztbd.project.cassandra.entity.CassandraUserEntity;
import pl.ztbd.project.cassandra.entity.key.RefreshTokenKey;
import pl.ztbd.project.cassandra.repository.CassandraRefreshTokenRepository;
import pl.ztbd.project.cassandra.repository.CassandraUserRepository;
import pl.ztbd.project.security.JwtService;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CassandraUserService implements UserAPI {

    private final CassandraUserRepository userRepository;
    private final CassandraRefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;

    @Override
    public MessageResponse registerUser(RegisterUserRequest registerUserRequest) {
        boolean userExistsByEmail = userRepository.existsByEmail(registerUserRequest.email());
        if (userExistsByEmail) {
            return new MessageResponse("User with email " + registerUserRequest.email() + " already exists");
        }

        CassandraUserEntity userEntity = CassandraUserEntity.builder()
                .name(registerUserRequest.name())
                .surname(registerUserRequest.surname())
                .userName(registerUserRequest.userName())
                .password(registerUserRequest.password())
                .email(registerUserRequest.email())
                .build();

        userRepository.save(userEntity);
        return new MessageResponse("User with email " + registerUserRequest.email() + " created");
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        return userRepository.findByEmail(loginRequest.email())
                .map(userEntity -> {
                    boolean shouldLogin = userEntity.getPassword().equals(loginRequest.password());
                    if (shouldLogin) {
                        RefreshTokenKey key = new RefreshTokenKey(userEntity.getEmail(), UUID.randomUUID().toString());

                        CassandraRefreshTokenEntity refreshTokenEntity = new CassandraRefreshTokenEntity(
                                key,
                                UUID.randomUUID().toString(),
                                OffsetDateTime.now().plusMinutes(30L)
                        );

                        refreshTokenRepository.save(refreshTokenEntity);

                        return new LoginResponse(jwtService.generateToken(loginRequest.email()), refreshTokenEntity.getRefreshToken());
                    }
                    return null;
                })
                .orElse(null);
    }

    @Override
    public boolean logout(LogoutRequest logoutRequest) {
        String email = jwtService.extractEmail(logoutRequest.token());
        return userRepository.findByEmail(email)
                .map(user -> refreshTokenRepository.deleteAllByUserEmail(user.getEmail()))
                .orElse(false);
    }

    @Override
    public RefreshTokenResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        String email = jwtService.extractEmail(refreshTokenRequest.token());
        return userRepository.findByEmail(email)
                .map(userEntity -> {
                    CassandraRefreshTokenEntity refreshTokenEntity = refreshTokenRepository.findByUserEmail(userEntity.getEmail()).orElse(null);
                    if (refreshTokenEntity != null) {
                        boolean isRefreshTokenValid = refreshTokenEntity.getRefreshToken().equals(refreshTokenRequest.refreshToken());
                        if (isRefreshTokenValid) {
                            refreshTokenRepository.delete(refreshTokenEntity);
                            RefreshTokenKey key = new RefreshTokenKey(userEntity.getEmail(), UUID.randomUUID().toString());
                            CassandraRefreshTokenEntity validRefreshToken = refreshTokenRepository.save(new CassandraRefreshTokenEntity(key, Uuids.timeBased().toString(), OffsetDateTime.now().plusMinutes(30L)));
                            return new RefreshTokenResponse(jwtService.generateToken(userEntity.getEmail()), validRefreshToken.getRefreshToken());
                        }
                    }
                    return null;
                })
                .orElse(null);
    }

    @Override
    public boolean deleteAccount(DeleteAccountRequest deleteAccountRequest) {
        String email = jwtService.extractEmail(deleteAccountRequest.token());
        return userRepository.findByEmail(email)
                .map(userEntity -> {
                    refreshTokenRepository.deleteAllByUserEmail(userEntity.getEmail());
                    return true;
                }).orElse(false);
    }
}
