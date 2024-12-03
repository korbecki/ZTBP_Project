package pl.ztbd.project.oracle.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.ztbd.project.api.FlashcardsAPI;
import pl.ztbd.project.api.dto.request.*;
import pl.ztbd.project.api.dto.response.GetFlashcardsResponse;
import pl.ztbd.project.api.dto.response.GetPageResponse;
import pl.ztbd.project.api.dto.response.ResolveResponse;
import pl.ztbd.project.oracle.entity.OracleFlashcardEntity;
import pl.ztbd.project.oracle.entity.OracleFlashcardPageEntity;
import pl.ztbd.project.oracle.entity.OracleResolvedPageEntity;
import pl.ztbd.project.oracle.entity.OracleUserEntity;
import pl.ztbd.project.oracle.repository.OracleFlashcardPageRepository;
import pl.ztbd.project.oracle.repository.OracleFlashcardRepository;
import pl.ztbd.project.oracle.repository.OracleResolvedPageRepository;
import pl.ztbd.project.oracle.repository.OracleUserRepository;
import pl.ztbd.project.security.JwtService;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class OracleFlashcardsService implements FlashcardsAPI<Long> {
    private final OracleUserRepository userRepository;
    private final OracleFlashcardRepository flashcardRepository;
    private final OracleFlashcardPageRepository oracleFlashcardPageRepository;
    private final OracleResolvedPageRepository resolvedPageRepository;
    private final JwtService jwtService;
    private final OracleResolvedPageRepository oracleResolvedPageRepository;

    @Override
    public Long addFlashcard(AddFlashcardRequest<Long> addFlashcardRequest) {
        OracleUserEntity userEntity = authenticateUser(addFlashcardRequest.token());
        if (userEntity == null) {
            return null;
        }

        OracleFlashcardEntity flashcardEntity = flashcardRepository.save(new OracleFlashcardEntity(addFlashcardRequest.name(), addFlashcardRequest.description()));
        List<OracleFlashcardPageEntity> flashcardPageEntities = addFlashcardRequest.pages()
                .stream()
                .map(page -> new OracleFlashcardPageEntity(flashcardEntity.getId(), page.question(), page.answer()))
                .toList();
        oracleFlashcardPageRepository.saveAll(flashcardPageEntities);
        return flashcardEntity.getId();
    }

    @Override
    public boolean addFlashcardPages(AddPagesRequest<Long> addPageRequest) {
        OracleUserEntity userEntity = authenticateUser(addPageRequest.token());
        if (userEntity == null) {
            return false;
        }

        List<OracleFlashcardPageEntity> flashcardPageEntities = addPageRequest
                .addPageRequestList()
                .stream()
                .map(page -> new OracleFlashcardPageEntity(page.flashcardId(), page.question(), page.answer()))
                .toList();
        oracleFlashcardPageRepository.saveAll(flashcardPageEntities);
        return true;
    }

    @Override
    public boolean removeFlashcard(RemoveFlashcardRequest<Long> removeFlashcardRequest) {
        OracleUserEntity userEntity = authenticateUser(removeFlashcardRequest.token());
        if (userEntity == null) {
            return false;
        }

        flashcardRepository.deleteById(removeFlashcardRequest.flashcardId());
        return true;
    }

    @Override
    public boolean removeFlashcardPage(RemoveFlashcardPageRequest<Long> removeFlashcardPage) {
        OracleUserEntity userEntity = authenticateUser(removeFlashcardPage.token());
        if (userEntity == null) {
            return false;
        }
        oracleResolvedPageRepository.deleteAllByFlashcardPageId(removeFlashcardPage.pageId());
        oracleFlashcardPageRepository.deleteById(removeFlashcardPage.pageId());
        return true;
    }

    @Override
    public boolean modifyFlashcard(ModifyFlashcardRequest<Long> modifyFlashcardRequest) {
        OracleUserEntity userEntity = authenticateUser(modifyFlashcardRequest.token());
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
        OracleUserEntity userEntity = authenticateUser(modifyFlashcardPage.token());
        if (userEntity == null) {
            return false;
        }

        return oracleFlashcardPageRepository.findById(modifyFlashcardPage.pageId())
                .map(flashcardPageEntity -> {
                    flashcardPageEntity.setQuestion(modifyFlashcardPage.question());
                    flashcardPageEntity.setAnswer(modifyFlashcardPage.answer());
                    oracleFlashcardPageRepository.save(flashcardPageEntity);
                    return true;
                })
                .orElse(false);
    }

    @Override
    public List<GetFlashcardsResponse<Long>> getFlashcards(GetFlashcardsRequest<Long> getFlashcardsRequest) {
        OracleUserEntity userEntity = authenticateUser(getFlashcardsRequest.token());
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
        OracleUserEntity userEntity = authenticateUser(getPageRequest.token());
        if (userEntity == null) {
            return null;
        }

        return oracleFlashcardPageRepository.findAllByFlashcardId(getPageRequest.flashcardId())
                .stream()
                .map(flashcardPageEntity -> new GetPageResponse<Long>(flashcardPageEntity.getId(), flashcardPageEntity.getQuestion()))
                .toList();
    }

    @Override
    public ResolveResponse resolve(ResolveRequest<Long> resolveRequest) {
        OracleUserEntity userEntity = authenticateUser(resolveRequest.token());
        if (userEntity == null) {
            return null;
        }

        return oracleFlashcardPageRepository.findById(resolveRequest.pageId())
                .map(flashcardPageEntity -> {

                    boolean isCorrect = flashcardPageEntity.getAnswer().equals(resolveRequest.answer());
                    resolvedPageRepository.save(new OracleResolvedPageEntity(userEntity.getId(), resolveRequest.pageId(), resolveRequest.answer(), isCorrect));
                    return new ResolveResponse(flashcardPageEntity.getAnswer(), resolveRequest.answer(), isCorrect);
                })
                .orElse(null);


    }

    private OracleUserEntity authenticateUser(String token) {
        String email = jwtService.extractEmail(token);
        return userRepository.findByEmail(email).orElse(null);
    }
}
