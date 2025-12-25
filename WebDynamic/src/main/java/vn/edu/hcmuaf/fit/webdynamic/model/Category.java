package vn.edu.hcmuaf.fit.webdynamic.model;

import java.time.LocalDateTime;

public class Category {
    private int id;
    private String name;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructor không tham số
    public Category() {
    }
    public Category(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
