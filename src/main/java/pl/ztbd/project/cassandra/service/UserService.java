package pl.ztbd.project.cassandra.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.ztbd.project.api.UserAPI;
import pl.ztbd.project.api.dto.request.*;
import pl.ztbd.project.api.dto.response.LoginResponse;
import pl.ztbd.project.api.dto.response.MessageResponse;
import pl.ztbd.project.api.dto.response.RefreshTokenResponse;

@Service
@RequiredArgsConstructor
public class UserService implements UserAPI {
    @Override
    public MessageResponse registerUser(RegisterUserRequest registerUserRequest) {
        return null;
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        return null;
    }

    @Override
    public boolean logout(LogoutRequest logoutRequest) {
        return false;
    }

    @Override
    public RefreshTokenResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        return null;
    }

    @Override
    public boolean deleteAccount(DeleteAccountRequest deleteAccountRequest) {
        return false;
    }
}
