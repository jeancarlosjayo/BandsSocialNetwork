package com.jramdev.bandssocialnetwork;

public class PromotorUser {
    String uid, age, district, dni, email, enterprise, fullname, onlineStatus, phone, profilepic, ruc, typeuser;

    public PromotorUser() {
    }

    public PromotorUser(String uid, String age, String district, String dni, String email, String enterprise, String fullname, String onlineStatus, String phone, String profilepic, String ruc, String typeuser) {
        this.uid = uid;
        this.age = age;
        this.district = district;
        this.dni = dni;
        this.email = email;
        this.enterprise = enterprise;
        this.fullname = fullname;
        this.onlineStatus = onlineStatus;
        this.phone = phone;
        this.profilepic = profilepic;
        this.ruc = ruc;
        this.typeuser = typeuser;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEnterprise() {
        return enterprise;
    }

    public void setEnterprise(String enterprise) {
        this.enterprise = enterprise;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(String onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProfilepic() {
        return profilepic;
    }

    public void setProfilepic(String profilepic) {
        this.profilepic = profilepic;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getTypeuser() {
        return typeuser;
    }

    public void setTypeuser(String typeuser) {
        this.typeuser = typeuser;
    }
}
