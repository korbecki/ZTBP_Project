package pl.ztbd.project.api.dto.request;

public record RemoveFlashcardRequest<I>(I userId, I flashcardId) {
}
