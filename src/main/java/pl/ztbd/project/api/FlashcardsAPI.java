package pl.ztbd.project.api;

import pl.ztbd.project.api.dto.request.*;
import pl.ztbd.project.api.dto.response.GetFlashcardsResponse;
import pl.ztbd.project.api.dto.response.GetPageResponse;
import pl.ztbd.project.api.dto.response.ResolveResponse;

import java.util.List;

public interface FlashcardsAPI<ID> {
    ID addFlashcard(AddFlashcardRequest<ID> addFlashcardRequest);

    boolean addFlashcardPages(AddPagesRequest<ID> addPageRequest);

    boolean removeFlashcard(RemoveFlashcardRequest<ID> removeFlashcardRequest);

    boolean removeFlashcardPage(RemoveFlashcardPageRequest<ID> removeFlashcardPage);

    boolean modifyFlashcard(ModifyFlashcardRequest<ID> modifyFlashcardRequest);

    boolean modifyFlashcardPage(ModifyFlashcardPageRequest<ID> modifyFlashcardPage);

    List<GetFlashcardsResponse<ID>> getFlashcards(GetFlashcardsRequest<ID> getFlashcardsRequest);

    List<GetPageResponse<ID>> getPages(GetPageRequest<ID> getPageRequest);

    ResolveResponse resolve(ResolveRequest<ID> resolveRequest);
}
