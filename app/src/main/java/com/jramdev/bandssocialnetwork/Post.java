package com.jramdev.bandssocialnetwork;

public class Post {
    String pId, pTitle, uImage, pImage, pTime, uid, uEmail, pDescription, uName,pGender,pDistrict;

    public Post() {

    }

    public Post(String pId, String pTitle, String uImage, String pImage, String pTime, String uid, String uEmail, String pDescription, String uName, String pGender, String pDistrict) {
        this.pId = pId;
        this.pTitle = pTitle;
        this.uImage = uImage;
        this.pImage = pImage;
        this.pTime = pTime;
        this.uid = uid;
        this.uEmail = uEmail;
        this.pDescription = pDescription;
        this.uName = uName;
        this.pGender = pGender;
        this.pDistrict = pDistrict;
    }

    public String getpDistrict() {
        return pDistrict;
    }

    public void setpDistrict(String pDistrict) {
        this.pDistrict = pDistrict;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getpTitle() {
        return pTitle;
    }

    public void setpTitle(String pTitle) {
        this.pTitle = pTitle;
    }

    public String getuImage() {
        return uImage;
    }

    public void setuImage(String uImage) {
        this.uImage = uImage;
    }

    public String getpImage() {
        return pImage;
    }

    public void setpImage(String pImage) {
        this.pImage = pImage;
    }

    public String getpTime() {
        return pTime;
    }

    public void setpTime(String pTime) {
        this.pTime = pTime;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getuEmail() {
        return uEmail;
    }

    public void setuEmail(String uEmail) {
        this.uEmail = uEmail;
    }

    public String getpDescription() {
        return pDescription;
    }

    public void setpDescription(String pDescription) {
        this.pDescription = pDescription;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public String getpGender() {
        return pGender;
    }

    public void setpGender(String pGender) {
        this.pGender = pGender;
    }
}
