package com.pabu.raisingsuccess.models;

public class GoalModel {
    private int goalNumber;
    private String bigGoal;
    private String smallGoal;
    private String goalStartDate;
    private String goalEndDate;

    public int getGoalNumber() {
        return goalNumber;
    }

    public void setGoalNumber(int goalNumber) {
        this.goalNumber = goalNumber;
    }

    public String getBigGoal() {
        return bigGoal;
    }

    public void setBigGoal(String bigGoal) {
        this.bigGoal = bigGoal;
    }

    public String getSmallGoal() {
        return smallGoal;
    }

    public void setSmallGoal(String smallGoal) {
        this.smallGoal = smallGoal;
    }

    public String getGoalStartDate() {
        return goalStartDate;
    }

    public void setGoalStartDate(String goalStartDate) {
        this.goalStartDate = goalStartDate;
    }

    public String getGoalEndDate() {
        return goalEndDate;
    }

    public void setGoalEndDate(String goalEndDate) {
        this.goalEndDate = goalEndDate;
    }
}
