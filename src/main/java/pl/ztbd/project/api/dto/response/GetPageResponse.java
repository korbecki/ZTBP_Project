package pl.ztbd.project.api.dto.response;

public record GetPageResponse<I>(I pageId, String question) {
}
