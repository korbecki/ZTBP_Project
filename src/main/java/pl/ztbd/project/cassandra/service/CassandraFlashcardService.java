package pl.ztbd.project.cassandra.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.ztbd.project.api.FlashcardsAPI;
import pl.ztbd.project.api.dto.request.*;
import pl.ztbd.project.api.dto.response.GetFlashcardsResponse;
import pl.ztbd.project.api.dto.response.GetPageResponse;
import pl.ztbd.project.api.dto.response.ResolveResponse;
import pl.ztbd.project.cassandra.entity.CassandraFlashcardEntity;
import pl.ztbd.project.cassandra.entity.CassandraPageByFlashcardEntity;
import pl.ztbd.project.cassandra.entity.CassandraResolvedPageByFlashcardEntity;
import pl.ztbd.project.cassandra.entity.CassandraUserEntity;
import pl.ztbd.project.cassandra.entity.key.PageByFlashcardEntityKey;
import pl.ztbd.project.cassandra.entity.key.ResolvedPageByFlashcardEntityKey;
import pl.ztbd.project.cassandra.repository.CassandraFlashcardRepository;
import pl.ztbd.project.cassandra.repository.CassandraPageByFlashcardRepository;
import pl.ztbd.project.cassandra.repository.CassandraResolvedPageByFlashcardRepository;
import pl.ztbd.project.cassandra.repository.CassandraUserRepository;
import pl.ztbd.project.security.JwtService;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CassandraFlashcardService implements FlashcardsAPI<String> {

    private final CassandraUserRepository userRepository;
    private final CassandraFlashcardRepository flashcardRepository;
    private final CassandraPageByFlashcardRepository pageByFlashcardRepository;
    private final CassandraResolvedPageByFlashcardRepository resolvedPageByFlashcardRepository;
    private final JwtService jwtService;

    @Override
    public String addFlashcard(AddFlashcardRequest<String> addFlashcardRequest) {
        CassandraUserEntity userEntity = authenticateUser(addFlashcardRequest.token());
        if (userEntity == null) {
            return null;
        }

        CassandraFlashcardEntity flashcardEntity = flashcardRepository.save(
                CassandraFlashcardEntity.builder()
                        .name(addFlashcardRequest.name())
                        .description(addFlashcardRequest.description())
                        .build()
        );

        List<CassandraPageByFlashcardEntity> pages = addFlashcardRequest.pages().stream()
                .map(page -> CassandraPageByFlashcardEntity.builder()
                        .pageByFlashcardEntityKey(new PageByFlashcardEntityKey(flashcardEntity.getId(), UUID.randomUUID()))
                        .question(page.question())
                        .answer(page.answer())
                        .createdAt(OffsetDateTime.now())
                        .build())
                .toList();

        pageByFlashcardRepository.saveAll(pages);
        return flashcardEntity.getId().toString();
    }

    @Override
    public boolean addFlashcardPages(AddPagesRequest<String> addPageRequest) {
        CassandraUserEntity userEntity = authenticateUser(addPageRequest.token());
        if (userEntity == null) {
            return false;
        }

        List<CassandraPageByFlashcardEntity> pages = addPageRequest.addPageRequestList().stream()
                .map(page -> CassandraPageByFlashcardEntity.builder()
                        .pageByFlashcardEntityKey(new PageByFlashcardEntityKey(UUID.fromString(page.flashcardId()), UUID.randomUUID()))
                        .question(page.question())
                        .answer(page.answer())
                        .createdAt(OffsetDateTime.now())
                        .build())
                .toList();

        pageByFlashcardRepository.saveAll(pages);
        return true;
    }

    @Override
    public boolean removeFlashcard(RemoveFlashcardRequest<String> removeFlashcardRequest) {
        CassandraUserEntity userEntity = authenticateUser(removeFlashcardRequest.token());
        if (userEntity == null) {
            return false;
        }

        flashcardRepository.deleteById(UUID.fromString(removeFlashcardRequest.flashcardId()));
        return true;
    }

    @Override
    public boolean removeFlashcardPage(RemoveFlashcardPageRequest<String> removeFlashcardPage) {
        CassandraUserEntity userEntity = authenticateUser(removeFlashcardPage.token());
        if (userEntity == null) {
            return false;
        }

        pageByFlashcardRepository.deleteById(
                new PageByFlashcardEntityKey(UUID.fromString(removeFlashcardPage.flashcardId()), UUID.fromString(removeFlashcardPage.pageId()))
        );
        return true;
    }

    @Override
    public boolean modifyFlashcard(ModifyFlashcardRequest<String> modifyFlashcardRequest) {
        CassandraUserEntity userEntity = authenticateUser(modifyFlashcardRequest.token());
        if (userEntity == null) {
            return false;
        }

        return flashcardRepository.findById(UUID.fromString(modifyFlashcardRequest.flashcardId()))
                .map(flashcard -> {
                    flashcard.setName(modifyFlashcardRequest.name());
                    flashcard.setDescription(modifyFlashcardRequest.description());
                    flashcardRepository.save(flashcard);
                    return true;
                })
                .orElse(false);
    }

    @Override
    public boolean modifyFlashcardPage(ModifyFlashcardPageRequest<String> modifyFlashcardPage) {
        CassandraUserEntity userEntity = authenticateUser(modifyFlashcardPage.token());
        if (userEntity == null) {
            return false;
        }

        return pageByFlashcardRepository.findById(
                new PageByFlashcardEntityKey(UUID.fromString(modifyFlashcardPage.flashcardId()), UUID.fromString(modifyFlashcardPage.pageId()))
        ).map(page -> {
            page.setQuestion(modifyFlashcardPage.question());
            page.setAnswer(modifyFlashcardPage.answer());
            pageByFlashcardRepository.save(page);
            return true;
        }).orElse(false);
    }

    @Override
    public List<GetFlashcardsResponse<String>> getFlashcards(GetFlashcardsRequest<String> getFlashcardsRequest) {
        CassandraUserEntity userEntity = authenticateUser(getFlashcardsRequest.token());
        if (userEntity == null) {
            return null;
        }

        return flashcardRepository.findAll().stream()
                .map(flashcard -> new GetFlashcardsResponse<>(
                        flashcard.getId().toString(),
                        flashcard.getName(),
                        flashcard.getDescription()))
                .toList();
    }

    @Override
    public List<GetPageResponse<String>> getPages(GetPageRequest<String> getPageRequest) {
        CassandraUserEntity userEntity = authenticateUser(getPageRequest.token());
        if (userEntity == null) {
            return null;
        }

        return pageByFlashcardRepository.findAllByFlashcardId(UUID.fromString(getPageRequest.flashcardId())).stream()
                .map(page -> new GetPageResponse<>(
                        page.getPageByFlashcardEntityKey().getFlashcardPageId().toString(),
                        page.getQuestion()))
                .toList();
    }

    @Override
    public ResolveResponse resolve(ResolveRequest<String> resolveRequest) {
        CassandraUserEntity userEntity = authenticateUser(resolveRequest.token());
        if (userEntity == null) {
            return null;
        }

        return pageByFlashcardRepository.findById(
                new PageByFlashcardEntityKey(UUID.fromString(resolveRequest.flashcardId()), UUID.fromString(resolveRequest.pageId()))
        ).map(page -> {
            boolean isCorrect = page.getAnswer().equals(resolveRequest.answer());

            resolvedPageByFlashcardRepository.save(
                    CassandraResolvedPageByFlashcardEntity.builder()
                            .resolvedPageByFlashcardEntity(new ResolvedPageByFlashcardEntityKey(
                                    userEntity.getEmail(),
                                    UUID.fromString(resolveRequest.flashcardId()),
                                    UUID.fromString(resolveRequest.pageId())))
                            .answer(resolveRequest.answer())
                            .isCorrect(isCorrect)
                            .createdAt(OffsetDateTime.now())
                            .build()
            );
            return new ResolveResponse(page.getAnswer(), resolveRequest.answer(), isCorrect);
        }).orElse(null);
    }

    private CassandraUserEntity authenticateUser(String token) {
        String email = jwtService.extractEmail(token);
        return userRepository.findById(email).orElse(null);
    }
}
