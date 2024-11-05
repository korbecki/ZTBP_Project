package pl.ztbd.project.api.dto.request;

public record ResolveRequest<I>(I userId, I pageId, String answer) {
}
