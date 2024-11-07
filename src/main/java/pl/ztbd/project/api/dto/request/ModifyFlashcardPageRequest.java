package pl.ztbd.project.api.dto.request;

public record ModifyFlashcardPageRequest<I>(String token, I flashcardId, I pageId, String question, String answer) {
}
