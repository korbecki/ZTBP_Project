package pl.ztbd.project.api.dto.request;

public record RemoveFlashcardRequest<I>(String token, I flashcardId) {
}
