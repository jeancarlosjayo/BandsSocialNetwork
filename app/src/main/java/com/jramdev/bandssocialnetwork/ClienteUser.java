package com.jramdev.bandssocialnetwork;

public class ClienteUser {
    String uid, age, district, dni, email, fullname, onlineStatus, phone, profilepic, typeuser;

    public ClienteUser() {
    }

    public ClienteUser(String uid, String age, String district, String dni, String email, String fullname, String onlineStatus, String phone, String profilepic, String typeuser) {
        this.uid = uid;
        this.age = age;
        this.district = district;
        this.dni = dni;
        this.email = email;
        this.fullname = fullname;
        this.onlineStatus = onlineStatus;
        this.phone = phone;
        this.profilepic = profilepic;
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

    public String getTypeuser() {
        return typeuser;
    }

    public void setTypeuser(String typeuser) {
        this.typeuser = typeuser;
    }
}
