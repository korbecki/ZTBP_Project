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
import pl.ztbd.project.oracle.entity.UserEntity;
import pl.ztbd.project.oracle.repository.FlashcardPageRepository;
import pl.ztbd.project.oracle.repository.FlashcardRepository;
import pl.ztbd.project.oracle.repository.ResolvedPageRepository;
import pl.ztbd.project.oracle.repository.UserRepository;
import pl.ztbd.project.security.JwtService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FlashcardsService implements FlashcardsAPI<Long> {
    private final UserRepository userRepository;
    private final FlashcardRepository flashcardRepository;
    private final FlashcardPageRepository flashcardPageRepository;
    private final ResolvedPageRepository resolvedPageRepository;
    private final JwtService jwtService;

    @Override
    public Long addFlashcard(AddFlashcardRequest<Long> addFlashcardRequest) {
        UserEntity userEntity = authenticateUser(addFlashcardRequest.token());
        if (userEntity == null) {
            return null;
        }

        FlashcardEntity flashcardEntity = flashcardRepository.save(new FlashcardEntity(addFlashcardRequest.name(), addFlashcardRequest.description()));
        List<FlashcardPageEntity> flashcardPageEntities = addFlashcardRequest.pages()
                .stream()
                .map(page -> new FlashcardPageEntity(flashcardEntity.getId(), page.question(), page.answer()))
                .toList();
        flashcardPageRepository.saveAll(flashcardPageEntities);
        return flashcardEntity.getId();
    }

    @Override
    public boolean addFlashcardPages(AddPagesRequest<Long> addPageRequest) {
        UserEntity userEntity = authenticateUser(addPageRequest.token());
        if (userEntity == null) {
            return false;
        }

        List<FlashcardPageEntity> flashcardPageEntities = addPageRequest
                .addPageRequestList()
                .stream()
                .map(page -> new FlashcardPageEntity(page.flashcardId(), page.question(), page.answer()))
                .toList();
        flashcardPageRepository.saveAll(flashcardPageEntities);
        return true;
    }

    @Override
    public boolean removeFlashcard(RemoveFlashcardRequest<Long> removeFlashcardRequest) {
        UserEntity userEntity = authenticateUser(removeFlashcardRequest.token());
        if (userEntity == null) {
            return false;
        }

        flashcardRepository.deleteById(removeFlashcardRequest.flashcardId());
        return true;
    }

    @Override
    public boolean removeFlashcardPage(RemoveFlashcardPageRequest<Long> removeFlashcardPage) {
        UserEntity userEntity = authenticateUser(removeFlashcardPage.token());
        if (userEntity == null) {
            return false;
        }

        flashcardPageRepository.deleteById(removeFlashcardPage.pageId());
        return true;
    }

    @Override
    public boolean modifyFlashcard(ModifyFlashcardRequest<Long> modifyFlashcardRequest) {
        UserEntity userEntity = authenticateUser(modifyFlashcardRequest.token());
        if (userEntity == null) {
            return false;
        }


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
        UserEntity userEntity = authenticateUser(modifyFlashcardPage.token());
        if (userEntity == null) {
            return false;
        }

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
        UserEntity userEntity = authenticateUser(getFlashcardsRequest.token());
        if (userEntity == null) {
            return null;
        }

        return flashcardRepository.findAll()
                .stream()
                .map(flashcardEntity -> new GetFlashcardsResponse<Long>(flashcardEntity.getId(), flashcardEntity.getName(), flashcardEntity.getDescription()))
                .toList();
    }

    @Override
    public List<GetPageResponse<Long>> getPages(GetPageRequest<Long> getPageRequest) {
        UserEntity userEntity = authenticateUser(getPageRequest.token());
        if (userEntity == null) {
            return null;
        }

        return flashcardPageRepository.findAllByFlashcardId(getPageRequest.flashcardId())
                .stream()
                .map(flashcardPageEntity -> new GetPageResponse<Long>(flashcardPageEntity.getId(), flashcardPageEntity.getQuestion()))
                .toList();
    }

    @Override
    public ResolveResponse resolve(ResolveRequest<Long> resolveRequest) {
        UserEntity userEntity = authenticateUser(resolveRequest.token());
        if (userEntity == null) {
            return null;
        }

        return flashcardPageRepository.findById(resolveRequest.pageId())
                .map(flashcardPageEntity -> {

                    boolean isCorrect = flashcardPageEntity.getAnswer().equals(resolveRequest.answer());
                    resolvedPageRepository.save(new ResolvedPageEntity(userEntity.getId(), resolveRequest.pageId(), resolveRequest.answer(), isCorrect));
                    return new ResolveResponse(flashcardPageEntity.getAnswer(), resolveRequest.answer(), isCorrect);
                })
                .orElse(null);


    }

    private UserEntity authenticateUser(String token) {
        String email = jwtService.extractEmail(token);
        return userRepository.findByEmail(email).orElse(null);
    }
}
