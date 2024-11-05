package pl.ztbd.project.api;

import pl.ztbd.project.api.dto.request.*;
import pl.ztbd.project.api.dto.response.LoginResponse;
import pl.ztbd.project.api.dto.response.MessageResponse;
import pl.ztbd.project.api.dto.response.RefreshTokenResponse;

public interface UserAPI {
    MessageResponse registerUser(RegisterUserRequest registerUserRequest);

    LoginResponse login(LoginRequest loginRequest);

    boolean logout(LogoutRequest logoutRequest);

    RefreshTokenResponse refreshToken(RefreshTokenRequest refreshTokenRequest);

    boolean deleteAccount(DeleteAccountRequest deleteAccountRequest);
}
