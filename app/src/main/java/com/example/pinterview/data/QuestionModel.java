package com.example.pinterview.data;

public class QuestionModel {
    private Integer id;
    private String content;
    private Integer expiredTime;
    private String company;
    private String type;

    public QuestionModel() {}

    public QuestionModel(Integer id, String content, Integer expiredTime, String company, String type) {
        this.id = id;
        this.content = content;
        this.expiredTime = expiredTime;
        this.company = company;
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public Integer getExpiredTime() {
        return expiredTime;
    }

    public String getCompany() {
        return company;
    }

    public String getType() {
        return type;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setExpiredTime(Integer expiredTime) {
        this.expiredTime = expiredTime;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public void setType(String type) {
        this.type = type;
    }
}
