package pl.ztbd.project.api.dto.request;

public record GetPageRequest<I>(I userId, I flashcardId) {
}
