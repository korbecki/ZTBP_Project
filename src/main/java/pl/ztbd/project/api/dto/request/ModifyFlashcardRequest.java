package pl.ztbd.project.api.dto.request;

public record ModifyFlashcardRequest(Long userId, Long flashcardId, String name, String description) {
}
