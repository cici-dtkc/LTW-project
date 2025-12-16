package vn.edu.hcmuaf.fit.webdynamic.model;

import java.time.LocalDateTime;

public class Image {
    private int id;
    private String imgPath;
    private boolean main;
    private LocalDateTime createdAt;

    // ===== Constructors =====
    public Image() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public boolean isMain() {
        return main;
    }

    public void setMain(boolean main) {
        this.main = main;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
