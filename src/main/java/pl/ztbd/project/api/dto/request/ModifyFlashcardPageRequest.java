package pl.ztbd.project.api.dto.request;

public record ModifyFlashcardPageRequest(Long userId, Long flashcardId, Long pageId, String question, String answer) {
}
