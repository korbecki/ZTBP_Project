package pl.ztbd.project.api.dto.request;

public record ModifyFlashcardPageRequest<I>(I userId, I flashcardId, I pageId, String question, String answer) {
}
