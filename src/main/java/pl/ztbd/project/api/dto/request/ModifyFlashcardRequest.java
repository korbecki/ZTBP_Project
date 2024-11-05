package pl.ztbd.project.api.dto.request;

public record ModifyFlashcardRequest<I>(I userId, I flashcardId, String name, String description) {
}
