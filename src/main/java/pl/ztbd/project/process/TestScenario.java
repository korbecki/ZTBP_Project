package pl.ztbd.project.process;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import pl.ztbd.project.api.FlashcardsAPI;
import pl.ztbd.project.api.UserAPI;
import pl.ztbd.project.api.dto.request.*;
import pl.ztbd.project.api.dto.response.LoginResponse;
import pl.ztbd.project.oracle.service.FlashcardsService;
import pl.ztbd.project.oracle.service.UserService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TestScenario {
    private final FlashcardsService flashcardsService;
    private final UserService userService;


    public void run(int count) {
        for (int i = 0; i < count; i++) {
            run(flashcardsService, userService);
        }
    }

    private <ID> void run(FlashcardsAPI<ID> flashcardsAPI, UserAPI userAPI) {
        String name = generateRandomString();
        String surname = generateRandomString();
        String password = generateRandomString();
        String userName = generateRandomString();
        String email = generateRandomString();

        String token = registerAndLogin(userAPI, name, surname, password, userName, email);

        ID flashcardId = createFlashcard(flashcardsAPI, token);

        addPagesToFlashcard(flashcardsAPI, token, flashcardId);

        modifyFlashcard(flashcardsAPI, token, flashcardId);

        modifyFlashcardPages(flashcardsAPI, token, flashcardId);

        resolveFlashcardPages(flashcardsAPI, token, flashcardId);

        removeFlashcardPages(flashcardsAPI, token, flashcardId);

        removeFlashcard(flashcardsAPI, token, flashcardId);

        logoutAndDeleteAccount(userAPI, token, userName, password);
    }

    private String generateRandomString() {
        return RandomStringUtils.secure().nextAlphanumeric(10);
    }

    private <ID> String registerAndLogin(UserAPI userAPI, String name, String surname, String password, String userName, String email) {
        userAPI.registerUser(new RegisterUserRequest(name, surname, password, userName, email));
        LoginResponse loginResponse = userAPI.login(new LoginRequest(userName, password));
        return loginResponse.token();
    }

    private <ID> ID createFlashcard(FlashcardsAPI<ID> flashcardsAPI, String token) {
        String flashcardName = generateRandomString();
        String flashcardDescription = generateRandomString();

        List<AddFlashcardRequest.Page> pages = generateFlashcardPages();
        AddFlashcardRequest<ID> request = new AddFlashcardRequest<>(token, flashcardName, flashcardDescription, pages);
        return flashcardsAPI.addFlashcard(request);
    }

    private List<AddFlashcardRequest.Page> generateFlashcardPages() {
        List<AddFlashcardRequest.Page> pages = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            String question = generateRandomString();
            String answer = generateRandomString();
            pages.add(new AddFlashcardRequest.Page(question, answer));
        }
        return pages;
    }

    private <ID> void addPagesToFlashcard(FlashcardsAPI<ID> flashcardsAPI, String token, ID flashcardId) {
        List<AddPageRequest<ID>> pageRequests = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            String question = generateRandomString();
            String answer = generateRandomString();
            pageRequests.add(new AddPageRequest<>(flashcardId, question, answer));
        }
        AddPagesRequest<ID> addPagesRequest = new AddPagesRequest<>(token, pageRequests);
        flashcardsAPI.addFlashcardPages(addPagesRequest);
    }

    private <ID> void modifyFlashcard(FlashcardsAPI<ID> flashcardsAPI, String token, ID flashcardId) {
        String newFlashcardName = generateRandomString();
        String newFlashcardDescription = generateRandomString();
        ModifyFlashcardRequest<ID> request = new ModifyFlashcardRequest<>(token, flashcardId, newFlashcardName, newFlashcardDescription);
        flashcardsAPI.modifyFlashcard(request);
    }

    private <ID> void modifyFlashcardPages(FlashcardsAPI<ID> flashcardsAPI, String token, ID flashcardId) {
        flashcardsAPI.getPages(new GetPageRequest<>(token, flashcardId))
                .forEach(page -> {
                    String newQuestion = generateRandomString();
                    String newAnswer = generateRandomString();
                    ModifyFlashcardPageRequest<ID> modifyPageRequest = new ModifyFlashcardPageRequest<>(token, flashcardId, page.pageId(), newQuestion, newAnswer);
                    flashcardsAPI.modifyFlashcardPage(modifyPageRequest);
                });
    }

    private <ID> void resolveFlashcardPages(FlashcardsAPI<ID> flashcardsAPI, String token, ID flashcardId) {
        flashcardsAPI.getPages(new GetPageRequest<>(token, flashcardId))
                .forEach(page -> {
                    String userAnswer = generateRandomString();
                    ResolveRequest<ID> resolveRequest = new ResolveRequest<>(token, flashcardId, page.pageId(), userAnswer);
                    flashcardsAPI.resolve(resolveRequest);
                });
    }

    private <ID> void removeFlashcardPages(FlashcardsAPI<ID> flashcardsAPI, String token, ID flashcardId) {
        flashcardsAPI.getPages(new GetPageRequest<>(token, flashcardId))
                .stream()
                .limit(10)
                .forEach(page -> {
                    RemoveFlashcardPageRequest<ID> removePageRequest = new RemoveFlashcardPageRequest<>(token, flashcardId, page.pageId());
                    flashcardsAPI.removeFlashcardPage(removePageRequest);
                });
    }

    private <ID> void removeFlashcard(FlashcardsAPI<ID> flashcardsAPI, String token, ID flashcardId) {
        RemoveFlashcardRequest<ID> removeRequest = new RemoveFlashcardRequest<>(token, flashcardId);
        flashcardsAPI.removeFlashcard(removeRequest);
    }

    private <ID> void logoutAndDeleteAccount(UserAPI userAPI, String token, String userName, String password) {
        userAPI.refreshToken(new RefreshTokenRequest(token, token)); // Assuming token is also the refresh token.
        userAPI.logout(new LogoutRequest(token));
        LoginResponse loginResponse = userAPI.login(new LoginRequest(userName, password));
        userAPI.deleteAccount(new DeleteAccountRequest(token));
    }
}
