package pl.ztbd.project.process;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.RandomStringUtils;
import pl.ztbd.project.api.FlashcardsAPI;
import pl.ztbd.project.api.UserAPI;
import pl.ztbd.project.api.dto.request.*;
import pl.ztbd.project.api.dto.response.LoginResponse;
import pl.ztbd.project.api.dto.response.MessageResponse;
import pl.ztbd.project.api.dto.response.ResolveResponse;
import pl.ztbd.project.utils.StepEnum;
import pl.ztbd.project.utils.Timer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Log4j2
public class TestScenario implements Runnable {

    private final FlashcardsAPI<?> flashcardsAPI;
    private final UserAPI userAPI;

    public TestScenario(FlashcardsAPI<?> flashcardsAPI, UserAPI userAPI) {
        this.flashcardsAPI = flashcardsAPI;
        this.userAPI = userAPI;
    }


    public void run() {
        Timer timer = Timer.start(StepEnum.FULL_TEST);
        run(flashcardsAPI, userAPI);
        timer.stop();
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

    private String registerAndLogin(UserAPI userAPI, String name, String surname, String password, String userName, String email) {
        Timer timer = Timer.start(StepEnum.REGISTER);
        MessageResponse registerResponse = userAPI.registerUser(new RegisterUserRequest(name, surname, password, userName, email));
        timer.stop();
        log.info("Register Response: {}", registerResponse);


        timer = Timer.start(StepEnum.LOGIN);
        LoginResponse loginResponse = userAPI.login(new LoginRequest(email, password));
        timer.stop();
        log.info("Login Response: {}", loginResponse);
        return loginResponse.token();
    }

    private <ID> ID createFlashcard(FlashcardsAPI<ID> flashcardsAPI, String token) {
        String flashcardName = generateRandomString();
        String flashcardDescription = generateRandomString();
        List<AddFlashcardRequest.Page> pages = generateFlashcardPages();
        AddFlashcardRequest<ID> request = new AddFlashcardRequest<>(token, flashcardName, flashcardDescription, pages);

        Timer timer = Timer.start(StepEnum.ADD_NEW_FLASHCARD);
        ID id = flashcardsAPI.addFlashcard(request);
        timer.stop();
        log.info("Add Flashcard Response: {}", id);
        return id;
    }

    private List<AddFlashcardRequest.Page> generateFlashcardPages() {
        List<AddFlashcardRequest.Page> pages = new ArrayList<>();
        String question = generateRandomString();
        String answer = generateRandomString();
        pages.add(new AddFlashcardRequest.Page(question, answer));
        return pages;
    }

    private <ID> void addPagesToFlashcard(FlashcardsAPI<ID> flashcardsAPI, String token, ID flashcardId) {
        List<AddPageRequest<ID>> pageRequests = new ArrayList<>();

        String question = generateRandomString();
        String answer = generateRandomString();
        pageRequests.add(new AddPageRequest<>(flashcardId, question, answer));

        AddPagesRequest<ID> addPagesRequest = new AddPagesRequest<>(token, pageRequests);
        Timer timer = Timer.start(StepEnum.ADD_PAGES_TO_FLASHCARD);
        boolean addPagesResponse = flashcardsAPI.addFlashcardPages(addPagesRequest);
        timer.stop();
        log.info("Add Pages Response: {}", addPagesResponse);
    }

    private <ID> void modifyFlashcard(FlashcardsAPI<ID> flashcardsAPI, String token, ID flashcardId) {
        String newFlashcardName = generateRandomString();
        String newFlashcardDescription = generateRandomString();
        ModifyFlashcardRequest<ID> request = new ModifyFlashcardRequest<>(token, flashcardId, newFlashcardName, newFlashcardDescription);
        Timer timer = Timer.start(StepEnum.MODIFY_FLASHCARD);
        boolean modifyFlashcardResponse = flashcardsAPI.modifyFlashcard(request);
        timer.stop();
        log.info("Modify Flashcard Response: {}", modifyFlashcardResponse);
    }

    private <ID> void modifyFlashcardPages(FlashcardsAPI<ID> flashcardsAPI, String token, ID flashcardId) {
        Timer timer = Timer.start(StepEnum.MODIFY_FLASHCARD_PAGES);
        flashcardsAPI.getPages(new GetPageRequest<>(token, flashcardId))
                .stream()
                .peek(it -> log.info("Modify Page, peek: {}", it))
                .forEach(page -> {
                    String newQuestion = generateRandomString();
                    String newAnswer = generateRandomString();
                    ModifyFlashcardPageRequest<ID> modifyPageRequest = new ModifyFlashcardPageRequest<>(token, flashcardId, page.pageId(), newQuestion, newAnswer);
                    boolean modifyFlashcardPageResponse = flashcardsAPI.modifyFlashcardPage(modifyPageRequest);
                    log.info("Modify Page: {}", modifyFlashcardPageResponse);
                });
        timer.stop();
    }

    private <ID> void resolveFlashcardPages(FlashcardsAPI<ID> flashcardsAPI, String token, ID flashcardId) {
        Timer timer = Timer.start(StepEnum.RESOLVE_PAGES);
        flashcardsAPI.getPages(new GetPageRequest<>(token, flashcardId))
                .stream()
                .peek(it -> log.info("Resolve Page, peek: {}", it))
                .forEach(page -> {
                    String userAnswer = generateRandomString();
                    ResolveRequest<ID> resolveRequest = new ResolveRequest<>(token, flashcardId, page.pageId(), userAnswer);
                    ResolveResponse resolveResponse = flashcardsAPI.resolve(resolveRequest);
                    log.info("Resolve Page Response: {}", resolveResponse);
                });
        timer.stop();
    }

    private <ID> void removeFlashcardPages(FlashcardsAPI<ID> flashcardsAPI, String token, ID flashcardId) {
        Timer timer = Timer.start(StepEnum.REMOVE_PAGES);
        flashcardsAPI.getPages(new GetPageRequest<>(token, flashcardId))
                .stream()
                .limit(10)
                .forEach(page -> {
                    RemoveFlashcardPageRequest<ID> removePageRequest = new RemoveFlashcardPageRequest<>(token, flashcardId, page.pageId());
                    boolean removePageResponse = flashcardsAPI.removeFlashcardPage(removePageRequest);
                    log.info("Remove Page: {}", removePageResponse);
                });
        timer.stop();
    }

    private <ID> void removeFlashcard(FlashcardsAPI<ID> flashcardsAPI, String token, ID flashcardId) {
        RemoveFlashcardRequest<ID> removeRequest = new RemoveFlashcardRequest<>(token, flashcardId);
        Timer timer = Timer.start(StepEnum.REMOVE_FLASHCARD);
        boolean removeFlashcard = flashcardsAPI.removeFlashcard(removeRequest);
        log.info("Remove Flashcards: {}", removeFlashcard);
        timer.stop();
    }

    private void logoutAndDeleteAccount(UserAPI userAPI, String token, String userName, String password) {
        Timer timer = Timer.start(StepEnum.LOGOUT_AND_DELETE_ACCOUNT);
        userAPI.refreshToken(new RefreshTokenRequest(token, token)); // Assuming token is also the refresh token.
        userAPI.logout(new LogoutRequest(token));
        userAPI.login(new LoginRequest(userName, password));
        userAPI.deleteAccount(new DeleteAccountRequest(token));
        timer.stop();
    }

}
