package pl.ztbd.project.api.dto.request;

public record RemoveFlashcardPageRequest<I>(I userId, I flashcardId, I pageId) {
}
