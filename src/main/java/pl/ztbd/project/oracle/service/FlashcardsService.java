package pl.ztbd.project.oracle.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.ztbd.project.api.FlashcardsAPI;
import pl.ztbd.project.api.dto.request.*;
import pl.ztbd.project.api.dto.response.GetFlashcardsResponse;
import pl.ztbd.project.api.dto.response.GetPageResponse;
import pl.ztbd.project.api.dto.response.ResolveResponse;
import pl.ztbd.project.oracle.entity.FlashcardEntity;
import pl.ztbd.project.oracle.entity.FlashcardPageEntity;
import pl.ztbd.project.oracle.entity.ResolvedPageEntity;
import pl.ztbd.project.oracle.repository.FlashcardPageRepository;
import pl.ztbd.project.oracle.repository.FlashcardRepository;
import pl.ztbd.project.oracle.repository.ResolvedPageRepository;
import pl.ztbd.project.oracle.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FlashcardsService implements FlashcardsAPI<Long> {
    private final UserRepository userRepository;
    private final FlashcardRepository flashcardRepository;
    private final FlashcardPageRepository flashcardPageRepository;
    private final ResolvedPageRepository resolvedPageRepository;


    @Override
    public boolean addFlashcard(AddFlashcardRequest<Long> addFlashcardRequest) {
        FlashcardEntity flashcardEntity = flashcardRepository.save(new FlashcardEntity(addFlashcardRequest.name(), addFlashcardRequest.description()));
        List<FlashcardPageEntity> flashcardPageEntities = addFlashcardRequest.pages()
                .stream()
                .map(page -> new FlashcardPageEntity(flashcardEntity.getId(), page.question(), page.answer()))
                .toList();
        flashcardPageRepository.saveAll(flashcardPageEntities);
        return true;
    }

    @Override
    public boolean addFlashcardPages(List<AddPageRequest<Long>> addPageRequest) {
        List<FlashcardPageEntity> flashcardPageEntities = addPageRequest
                .stream()
                .map(page -> new FlashcardPageEntity(page.flashcardId(), page.question(), page.answer()))
                .toList();
        flashcardPageRepository.saveAll(flashcardPageEntities);
        return true;
    }

    @Override
    public boolean removeFlashcard(RemoveFlashcardRequest<Long> removeFlashcardRequest) {
        flashcardRepository.deleteById(removeFlashcardRequest.flashcardId());
        return true;
    }

    @Override
    public boolean removeFlashcardPage(RemoveFlashcardPageRequest<Long> removeFlashcardPage) {
        flashcardPageRepository.deleteById(removeFlashcardPage.pageId());
        return true;
    }

    @Override
    public boolean modifyFlashcard(ModifyFlashcardRequest<Long> modifyFlashcardRequest) {
        return flashcardRepository.findById(modifyFlashcardRequest.flashcardId())
                .map(flashcardEntity -> {
                    flashcardEntity.setDescription(modifyFlashcardRequest.description());
                    flashcardEntity.setName(modifyFlashcardRequest.name());
                    flashcardRepository.save(flashcardEntity);
                    return true;
                })
                .orElse(false);

    }

    @Override
    public boolean modifyFlashcardPage(ModifyFlashcardPageRequest<Long> modifyFlashcardPage) {
        return flashcardPageRepository.findById(modifyFlashcardPage.pageId())
                .map(flashcardPageEntity -> {
                    flashcardPageEntity.setQuestion(modifyFlashcardPage.question());
                    flashcardPageEntity.setAnswer(modifyFlashcardPage.answer());
                    flashcardPageRepository.save(flashcardPageEntity);
                    return true;
                })
                .orElse(false);
    }

    @Override
    public List<GetFlashcardsResponse<Long>> getFlashcards(GetFlashcardsRequest<Long> getFlashcardsRequest) {
        return flashcardRepository.findAll()
                .stream()
                .map(flashcardEntity -> new GetFlashcardsResponse<Long>(flashcardEntity.getId(), flashcardEntity.getName(), flashcardEntity.getDescription()))
                .toList();
    }

    @Override
    public List<GetPageResponse<Long>> getPages(GetPageRequest<Long> getPageRequest) {
        return flashcardPageRepository.findAllByFlashcardId(getPageRequest.flashcardId())
                .stream()
                .map(flashcardPageEntity -> new GetPageResponse<Long>(flashcardPageEntity.getId(), flashcardPageEntity.getQuestion()))
                .toList();
    }

    @Override
    public ResolveResponse resolve(ResolveRequest<Long> resolveRequest) {
        return flashcardPageRepository.findById(resolveRequest.pageId())
                        .map(flashcardPageEntity -> {
                            boolean isCorrect = flashcardPageEntity.getAnswer().equals(resolveRequest.answer());
                            resolvedPageRepository.save(new ResolvedPageEntity(resolveRequest.userId(), resolveRequest.pageId(), resolveRequest.answer(), isCorrect));
                            return new ResolveResponse(flashcardPageEntity.getAnswer(), resolveRequest.answer(), isCorrect);
                        })
                .orElse(null);


    }
}
