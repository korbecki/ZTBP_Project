package pl.ztbd.project.api.dto.request;

public record RemoveFlashcardPageRequest<I>(String token, I flashcardId, I pageId) {
}
