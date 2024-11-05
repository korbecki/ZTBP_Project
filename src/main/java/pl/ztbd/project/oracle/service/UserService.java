package pl.ztbd.project.oracle.service;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.ztbd.project.api.UserAPI;
import pl.ztbd.project.api.dto.request.*;
import pl.ztbd.project.api.dto.response.LoginResponse;
import pl.ztbd.project.api.dto.response.MessageResponse;
import pl.ztbd.project.api.dto.response.RefreshTokenResponse;
import pl.ztbd.project.oracle.entity.RefreshTokenEntity;
import pl.ztbd.project.oracle.entity.UserEntity;
import pl.ztbd.project.oracle.repository.RefreshTokenRepository;
import pl.ztbd.project.oracle.repository.ResolvedPageRepository;
import pl.ztbd.project.oracle.repository.UserRepository;
import pl.ztbd.project.security.JwtService;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class UserService implements UserAPI {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final ResolvedPageRepository resolvedPageRepository;
    private final JwtService jwtService;

    @Override
    public MessageResponse registerUser(RegisterUserRequest registerUserRequest) {
        boolean userExistsByEmail = userRepository.existsByEmail(registerUserRequest.email());
        if (userExistsByEmail) {
            return new MessageResponse("User with email " + registerUserRequest.email() + " already exists");
        }

        UserEntity userEntity = UserEntity.builder()
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
                        RefreshTokenEntity refreshTokenEntity = refreshTokenRepository.save(new RefreshTokenEntity(userEntity.getId(), OffsetDateTime.now().plusMinutes(30L), Uuids.timeBased().toString()));
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
                    RefreshTokenEntity refreshTokenEntity = refreshTokenRepository.findByUserId(userEntity.getId()).orElse(null);
                    if (refreshTokenEntity != null) {
                        boolean isRefreshTokenValid = refreshTokenEntity.getRefreshToken().equals(refreshTokenRequest.refreshToken());
                        if (isRefreshTokenValid) {
                            refreshTokenRepository.delete(refreshTokenEntity);
                            RefreshTokenEntity validRefreshToken = refreshTokenRepository.save(new RefreshTokenEntity(userEntity.getId(), OffsetDateTime.now().plusMinutes(30L), Uuids.timeBased().toString()));
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
