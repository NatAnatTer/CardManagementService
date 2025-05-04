package ru.project.CardManagementService.entity;


public enum StateOfCard {
    ACTIVE("Активна"),
    BLOCK("Заблокирована"),
    EXPIRED("Истек срок действия");

    private String title;

    StateOfCard(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
