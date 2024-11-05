package pl.ztbd.project.cassandra.service;

import org.springframework.stereotype.Service;
import pl.ztbd.project.api.FlashcardsAPI;
import pl.ztbd.project.api.dto.request.*;
import pl.ztbd.project.api.dto.response.GetFlashcardsResponse;
import pl.ztbd.project.api.dto.response.GetPageResponse;
import pl.ztbd.project.api.dto.response.ResolveResponse;

import java.util.List;

@Service
public class FlashcardService implements FlashcardsAPI {
    @Override
    public boolean addFlashcard(AddFlashcardRequest addFlashcardRequest) {
        return false;
    }

    @Override
    public boolean addFlashcardPages(List<AddPageRequest> addPageRequest) {
        return false;
    }

    @Override
    public boolean removeFlashcard(RemoveFlashcardRequest removeFlashcardRequest) {
        return false;
    }

    @Override
    public boolean removeFlashcardPage(RemoveFlashcardPageRequest removeFlashcardPage) {
        return false;
    }

    @Override
    public boolean modifyFlashcard(ModifyFlashcardRequest modifyFlashcardRequest) {
        return false;
    }

    @Override
    public boolean modifyFlashcardPage(ModifyFlashcardPageRequest modifyFlashcardPage) {
        return false;
    }

    @Override
    public List<GetFlashcardsResponse> getFlashcardsResponse(GetFlashcardsRequest getFlashcardsRequest) {
        return List.of();
    }

    @Override
    public GetPageResponse getPageResponse(GetPageRequest getPageRequest) {
        return null;
    }

    @Override
    public ResolveResponse resolve(ResolveRequest resolveRequest) {
        return null;
    }
}
