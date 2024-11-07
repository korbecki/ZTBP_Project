package pl.ztbd.project.api.dto.request;

public record ResolveRequest<I>(String token, I flashcardId, I pageId, String answer) {
}
