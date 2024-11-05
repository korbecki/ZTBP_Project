package pl.ztbd.project.api.dto.request;

public record RefreshTokenRequest(String token, String refreshToken) {
}
