package pl.ztbd.project.api.dto.request;

public record AddPageRequest<I>(I flashcardId, String question, String answer) {
}
