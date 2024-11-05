package pl.ztbd.project.api.dto.request;

public record AddPageRequest(Long flashcardId, String question, String answer) {
}
