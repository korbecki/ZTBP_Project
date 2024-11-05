package pl.ztbd.project.oracle.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.ztbd.project.api.FlashcardsAPI;
import pl.ztbd.project.api.dto.request.*;
import pl.ztbd.project.api.dto.response.GetFlashcardsResponse;
import pl.ztbd.project.api.dto.response.GetPageResponse;
import pl.ztbd.project.api.dto.response.ResolveResponse;
import pl.ztbd.project.oracle.repository.FlashcardPageRepository;
import pl.ztbd.project.oracle.repository.FlashcardRepository;
import pl.ztbd.project.oracle.repository.ResolvedPageRepository;
import pl.ztbd.project.oracle.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FlashcardsService implements FlashcardsAPI {
    private final UserRepository userRepository;
    private final FlashcardRepository flashcardRepository;
    private final FlashcardPageRepository flashcardPageRepository;
    private final ResolvedPageRepository resolvedPageRepository;


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
