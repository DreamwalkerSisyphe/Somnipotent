package com.dream.somnipotent;

public class RCInformation {
    private String id;
    private String subject;
    private String description;
    // private int IconImage;
    public RCInformation(String id, String subject, String description) {
        this.id = id;
        this.subject = subject;
        this.description = description;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public String getDescription() {
        return description;
    }

    public String getId() {
        return id;
    }
}