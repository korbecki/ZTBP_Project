package pl.ztbd.project.api.dto.request;

import java.util.List;

public record AddFlashcardRequest(Long userId, String name, String description, List<Page> pages) {
    public record Page(String question, String answer) {}
}
