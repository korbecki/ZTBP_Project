package pl.ztbd.project.api.dto.response;

import lombok.ToString;


public record MessageResponse(String message) {

    @Override
    public String toString() {
        return "MessageResponse{" +
                "message='" + message + '\'' +
                '}';
    }
}
