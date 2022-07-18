package com.jramdev.bandssocialnetwork;

public class BandaUser {
    String uid, nameband, namemanager, department, district, onlineStatus, phone, profilepic, ruc, typeuser;

    public BandaUser() {
    }

    public BandaUser(String uid, String nameband, String namemanager, String department, String district, String onlineStatus, String phone, String profilepic, String ruc, String typeuser) {
        this.uid = uid;
        this.nameband = nameband;
        this.namemanager = namemanager;
        this.department = department;
        this.district = district;
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

    public String getNameband() {
        return nameband;
    }

    public void setNameband(String nameband) {
        this.nameband = nameband;
    }

    public String getNamemanager() {
        return namemanager;
    }

    public void setNamemanager(String namemanager) {
        this.namemanager = namemanager;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
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
