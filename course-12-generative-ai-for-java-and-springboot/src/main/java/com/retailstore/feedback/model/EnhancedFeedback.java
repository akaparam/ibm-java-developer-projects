package com.retailstore.feedback.model;

public class EnhancedFeedback extends FeedbackEntry {
    private String category;
    private String actionableInsight;

    public EnhancedFeedback() {}

    public EnhancedFeedback(FeedbackEntry entry) {
        super(entry.getId(), entry.getCustomer(), entry.getDepartment(),
              entry.getDate(), entry.getComment(), entry.getSentiment());
    }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getActionableInsight() { return actionableInsight; }
    public void setActionableInsight(String actionableInsight) {
        this.actionableInsight = actionableInsight;
    }
}
