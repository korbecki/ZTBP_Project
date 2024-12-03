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
import pl.ztbd.project.cassandra.repository.CassandraResolvedPageByFlashcardRepository;
import pl.ztbd.project.cassandra.repository.CassandraUserRepository;
import pl.ztbd.project.security.JwtService;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CassandraUserService implements UserAPI {

    private final CassandraUserRepository userRepository;
    private final CassandraRefreshTokenRepository refreshTokenRepository;
    private final CassandraResolvedPageByFlashcardRepository resolvedPageByFlashcardRepository;
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
                        RefreshTokenKey key = new RefreshTokenKey(userEntity.getEmail(), UUID.randomUUID());

                        CassandraRefreshTokenEntity refreshTokenEntity = new CassandraRefreshTokenEntity(
                                key,
                                Uuids.timeBased().toString(),
                                OffsetDateTime.now().plusMinutes(30L).toInstant()
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
                .map(user -> refreshTokenRepository.deleteAllByRefreshTokenKey_UserEmail(user.getEmail()))
                .orElse(false);
    }

    @Override
    public RefreshTokenResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        String email = jwtService.extractEmail(refreshTokenRequest.token());
        return userRepository.findByEmail(email)
                .map(userEntity -> {
                    CassandraRefreshTokenEntity refreshTokenEntity = refreshTokenRepository.findByRefreshTokenKey_UserEmail(userEntity.getEmail()).orElse(null);
                    if (refreshTokenEntity != null) {
                        boolean isRefreshTokenValid = refreshTokenEntity.getRefreshToken().equals(refreshTokenRequest.refreshToken());
                        if (isRefreshTokenValid) {
                            refreshTokenRepository.delete(refreshTokenEntity);
                            RefreshTokenKey key = new RefreshTokenKey(userEntity.getEmail(), UUID.randomUUID());
                            CassandraRefreshTokenEntity validRefreshToken = refreshTokenRepository.save(new CassandraRefreshTokenEntity(key, Uuids.timeBased().toString(), OffsetDateTime.now().plusMinutes(30L).toInstant()));
                            return new RefreshTokenResponse(jwtService.generateToken(userEntity.getEmail()), validRefreshToken.getRefreshToken().toString());
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
                    refreshTokenRepository.deleteAllByRefreshTokenKey_UserEmail(userEntity.getEmail());
                    resolvedPageByFlashcardRepository.deleteAllByResolvedPageByFlashcardEntityKey_UserEmail(userEntity.getEmail());
                    userRepository.deleteById(userEntity.getEmail());
                    return true;
                }).orElse(false);
    }
}
