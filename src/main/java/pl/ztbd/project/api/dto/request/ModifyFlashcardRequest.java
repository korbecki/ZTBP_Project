package pl.ztbd.project.api.dto.request;

public record ModifyFlashcardRequest<I>(String token, I flashcardId, String name, String description) {
}
