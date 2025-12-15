package vn.edu.hcmuaf.fit.webdynamic.model;

import java.time.LocalDateTime;

public class Image {

    private int id;
    private int variantColorId;
    private String imgPath;
    private int isMain;          // 1 = ảnh đại diện, 0 = ảnh phụ
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

    public int getVariantColorId() {
        return variantColorId;
    }

    public void setVariantColorId(int variantColorId) {
        this.variantColorId = variantColorId;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public int getIsMain() {
        return isMain;
    }

    public void setIsMain(int isMain) {
        this.isMain = isMain;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
