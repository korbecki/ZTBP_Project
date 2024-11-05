package pl.ztbd.project.api.dto.response;

public record ResolveResponse(String correctAnswer, String answer, Boolean isCorrect) {
}
