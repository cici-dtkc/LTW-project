package vn.edu.hcmuaf.fit.webdynamic.model;

public class Brand {
    private Integer id;
    private String name;        // Samsung, iPhone, Oppo
    private String logo;

    public Brand() {
    }
    public Brand(Integer id) {
        this.id = id;
    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}
