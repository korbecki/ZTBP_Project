package pl.ztbd.project.api.dto.response;

public record LoginResponse(String token, String refreshToken) {

    @Override
    public String toString() {
        return "LoginResponse{" +
                "token='" + token + '\'' +
                ", refreshToken='" + refreshToken + '\'' +
                '}';
    }
}
