package cn.edu.zju.bean;

public class Drug {

    private String id;
    private String name;
    private boolean biomarker;
    private String drugUrl;
    private String ObjCls;
    private Boolean favorited = Boolean.FALSE;

    public Drug() {
    }

    public Drug(String id, String name, boolean biomarker, String drugUrl, String objCls) {
        this.id = id;
        this.name = name;
        this.biomarker = biomarker;
        this.drugUrl = drugUrl;
        ObjCls = objCls;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isBiomarker() {
        return biomarker;
    }

    public void setBiomarker(boolean biomarker) {
        this.biomarker = biomarker;
    }

    public String getDrugUrl() {
        return drugUrl;
    }

    public void setDrugUrl(String drugUrl) {
        this.drugUrl = drugUrl;
    }

    public String getObjCls() {
        return ObjCls;
    }

    public void setObjCls(String objCls) {
        ObjCls = objCls;
    }


    public void setFavorited(Boolean favorited) {
        this.favorited = favorited;
    }

    public boolean isFavorited() {
        return this.favorited != null && this.favorited;
    }


}
