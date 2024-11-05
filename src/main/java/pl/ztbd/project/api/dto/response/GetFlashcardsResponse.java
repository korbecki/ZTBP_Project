package pl.ztbd.project.api.dto.response;

public record GetFlashcardsResponse<I>(I flashcardId, String name, String description) {
}
