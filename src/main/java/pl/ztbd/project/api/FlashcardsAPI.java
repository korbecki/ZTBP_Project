package pl.ztbd.project.api;

import pl.ztbd.project.api.dto.request.*;
import pl.ztbd.project.api.dto.response.GetFlashcardsResponse;
import pl.ztbd.project.api.dto.response.GetPageResponse;
import pl.ztbd.project.api.dto.response.ResolveResponse;

import java.util.List;

public interface FlashcardsAPI<I> {
    boolean addFlashcard(AddFlashcardRequest<I> addFlashcardRequest);
    boolean addFlashcardPages(List<AddPageRequest<I> > addPageRequest);

    boolean removeFlashcard(RemoveFlashcardRequest<I>  removeFlashcardRequest);
    boolean removeFlashcardPage(RemoveFlashcardPageRequest<I>  removeFlashcardPage);

    boolean modifyFlashcard(ModifyFlashcardRequest<I>  modifyFlashcardRequest);
    boolean modifyFlashcardPage(ModifyFlashcardPageRequest<I>  modifyFlashcardPage);

    List<GetFlashcardsResponse<I> > getFlashcards(GetFlashcardsRequest<I>  getFlashcardsRequest);
    List<GetPageResponse<I> > getPages(GetPageRequest<I>  getPageRequest);

    ResolveResponse resolve(ResolveRequest<I> resolveRequest);
}
