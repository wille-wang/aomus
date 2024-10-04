package com.examples.demo;

public class ListItem {
    private String title;
    private String content;

    public ListItem(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
}