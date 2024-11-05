package pl.ztbd.project.cassandra.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.ztbd.project.api.FlashcardsAPI;
import pl.ztbd.project.api.dto.request.*;
import pl.ztbd.project.api.dto.response.GetFlashcardsResponse;
import pl.ztbd.project.api.dto.response.GetPageResponse;
import pl.ztbd.project.api.dto.response.ResolveResponse;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FlashcardService implements FlashcardsAPI<String> {

    @Override
    public boolean addFlashcard(AddFlashcardRequest<String> addFlashcardRequest) {
        return false;
    }

    @Override
    public boolean addFlashcardPages(List<AddPageRequest<String>> addPageRequest) {
        return false;
    }

    @Override
    public boolean removeFlashcard(RemoveFlashcardRequest<String> removeFlashcardRequest) {
        return false;
    }

    @Override
    public boolean removeFlashcardPage(RemoveFlashcardPageRequest<String> removeFlashcardPage) {
        return false;
    }

    @Override
    public boolean modifyFlashcard(ModifyFlashcardRequest<String> modifyFlashcardRequest) {
        return false;
    }

    @Override
    public boolean modifyFlashcardPage(ModifyFlashcardPageRequest<String> modifyFlashcardPage) {
        return false;
    }

    @Override
    public List<GetFlashcardsResponse<String>> getFlashcards(GetFlashcardsRequest<String> getFlashcardsRequest) {
        return List.of();
    }

    @Override
    public List<GetPageResponse<String>> getPages(GetPageRequest<String> getPageRequest) {
        return List.of();
    }

    @Override
    public ResolveResponse resolve(ResolveRequest<String> resolveRequest) {
        return null;
    }
}
