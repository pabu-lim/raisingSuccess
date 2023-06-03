package com.pabu.raisingsuccess;

import java.util.Date;

public class ToDoModel {

    private int todoNumber;
    private String task;
    private int status;

    private String completionDate;

    private int completionLevel;



    public int getId() {
        return todoNumber;
    }

    public void setId(int id) {
        this.todoNumber = id;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(String completionDate) {
        this.completionDate = completionDate;
    }

    public int getCompletionLevel() {
        return completionLevel;
    }

    public void setCompletionLevel(int completionLevel) {
        this.completionLevel = completionLevel;
    }
}

