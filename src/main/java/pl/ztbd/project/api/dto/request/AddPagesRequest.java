package pl.ztbd.project.api.dto.request;

import java.util.List;

public record AddPagesRequest<I>(String token, List<AddPageRequest<I>> addPageRequestList) {
}
