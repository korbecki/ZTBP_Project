package pl.ztbd.project.oracle.service;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.ztbd.project.api.UserAPI;
import pl.ztbd.project.api.dto.request.*;
import pl.ztbd.project.api.dto.response.LoginResponse;
import pl.ztbd.project.api.dto.response.MessageResponse;
import pl.ztbd.project.api.dto.response.RefreshTokenResponse;
import pl.ztbd.project.oracle.entity.OracleRefreshTokenEntity;
import pl.ztbd.project.oracle.entity.OracleUserEntity;
import pl.ztbd.project.oracle.repository.OracleRefreshTokenRepository;
import pl.ztbd.project.oracle.repository.OracleResolvedPageRepository;
import pl.ztbd.project.oracle.repository.OracleUserRepository;
import pl.ztbd.project.security.JwtService;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class OracleUserService implements UserAPI {

    private final OracleUserRepository userRepository;
    private final OracleRefreshTokenRepository refreshTokenRepository;
    private final OracleResolvedPageRepository resolvedPageRepository;
    private final JwtService jwtService;

    @Override
    public MessageResponse registerUser(RegisterUserRequest registerUserRequest) {
        boolean userExistsByEmail = userRepository.existsByEmail(registerUserRequest.email());
        if (userExistsByEmail) {
            return new MessageResponse("User with email " + registerUserRequest.email() + " already exists");
        }

        OracleUserEntity userEntity = OracleUserEntity.builder()
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
                        OracleRefreshTokenEntity refreshTokenEntity = refreshTokenRepository.save(new OracleRefreshTokenEntity(userEntity.getId(), OffsetDateTime.now().plusMinutes(30L), Uuids.timeBased().toString()));
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
                .map(user -> refreshTokenRepository.deleteAllByUserId(user.getId()))
                .orElse(false);
    }

    @Override
    public RefreshTokenResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        String email = jwtService.extractEmail(refreshTokenRequest.token());
        return userRepository.findByEmail(email)
                .map(userEntity -> {
                    OracleRefreshTokenEntity refreshTokenEntity = refreshTokenRepository.findByUserId(userEntity.getId()).orElse(null);
                    if (refreshTokenEntity != null) {
                        boolean isRefreshTokenValid = refreshTokenEntity.getRefreshToken().equals(refreshTokenRequest.refreshToken());
                        if (isRefreshTokenValid) {
                            refreshTokenRepository.delete(refreshTokenEntity);
                            OracleRefreshTokenEntity validRefreshToken = refreshTokenRepository.save(new OracleRefreshTokenEntity(userEntity.getId(), OffsetDateTime.now().plusMinutes(30L), Uuids.timeBased().toString()));
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
                    refreshTokenRepository.deleteAllByUserId(userEntity.getId());
                    resolvedPageRepository.deleteAllByUserId(userEntity.getId());
                    return true;
                }).orElse(false);
    }

}
