package pl.ztbd.project.api.dto.request;

public record RegisterUserRequest(String name, String surname, String password, String userName, String email) {
}
