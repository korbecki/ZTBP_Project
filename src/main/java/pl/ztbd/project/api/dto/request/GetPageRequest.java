package pl.ztbd.project.api.dto.request;

public record GetPageRequest<I>(String token, I flashcardId) {
}
