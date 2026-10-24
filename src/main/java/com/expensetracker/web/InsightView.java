package com.expensetracker.web;

/** Small view-model for one insight card on the Insights page. */
public class InsightView {
    private final String name, text, requiredPlan;
    private final boolean unlocked, streakCard;
    public InsightView(String name, boolean unlocked, String text, boolean streakCard, String requiredPlan) {
        this.name = name; this.unlocked = unlocked; this.text = text; this.streakCard = streakCard; this.requiredPlan = requiredPlan;
    }
    public String getName() { return name; }
    public boolean isUnlocked() { return unlocked; }
    public String getText() { return text; }
    public boolean isStreakCard() { return streakCard; }
    public String getRequiredPlan() { return requiredPlan; }
}
