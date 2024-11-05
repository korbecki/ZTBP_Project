package pl.ztbd.project.api;

import pl.ztbd.project.api.dto.request.*;
import pl.ztbd.project.api.dto.response.GetFlashcardsResponse;
import pl.ztbd.project.api.dto.response.GetPageResponse;
import pl.ztbd.project.api.dto.response.ResolveResponse;

import java.util.List;

public interface FlashcardsAPI {
    boolean addFlashcard(AddFlashcardRequest addFlashcardRequest);
    boolean addFlashcardPages(List<AddPageRequest> addPageRequest);

    boolean removeFlashcard(RemoveFlashcardRequest removeFlashcardRequest);
    boolean removeFlashcardPage(RemoveFlashcardPageRequest removeFlashcardPage);

    boolean modifyFlashcard(ModifyFlashcardRequest modifyFlashcardRequest);
    boolean modifyFlashcardPage(ModifyFlashcardPageRequest modifyFlashcardPage);

    List<GetFlashcardsResponse> getFlashcardsResponse(GetFlashcardsRequest getFlashcardsRequest);
    GetPageResponse getPageResponse(GetPageRequest getPageRequest);

    ResolveResponse resolve(ResolveRequest resolveRequest);
}
