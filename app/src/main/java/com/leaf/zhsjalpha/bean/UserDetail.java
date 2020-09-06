package com.leaf.zhsjalpha.bean;

public class UserDetail {
    public String studentId;
    public Integer studentCard;
    public String name;
    public String sex;
    public String birthday;
    public String address;
    public String familyPhone;
    public String familyName;
    public String nation;
    public String picURL;
    public Integer idcard;
    public String password;
    public Integer gradeId;
    public Integer schoolId;

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public Integer getStudentCard() {
        return studentCard;
    }

    public void setStudentCard(Integer studentCard) {
        this.studentCard = studentCard;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFamilyPhone() {
        return familyPhone;
    }

    public void setFamilyPhone(String familyPhone) {
        this.familyPhone = familyPhone;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getPicURL() {
        return picURL;
    }

    public void setPicURL(String picURL) {
        this.picURL = picURL;
    }

    public Integer getIdcard() {
        return idcard;
    }

    public void setIdcard(Integer idcard) {
        this.idcard = idcard;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getGradeId() {
        return gradeId;
    }

    public void setGradeId(Integer gradeId) {
        this.gradeId = gradeId;
    }

    public Integer getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(Integer schoolId) {
        this.schoolId = schoolId;
    }

    @Override
    public String toString() {
        return "UserDetail{" +
                "studentId='" + studentId + '\'' +
                ", studentCard=" + studentCard +
                ", name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", birthday='" + birthday + '\'' +
                ", address='" + address + '\'' +
                ", familyPhone='" + familyPhone + '\'' +
                ", familyName='" + familyName + '\'' +
                ", nation='" + nation + '\'' +
                ", picURL='" + picURL + '\'' +
                ", idcard=" + idcard +
                ", password='" + password + '\'' +
                ", gradeId=" + gradeId +
                ", schoolId=" + schoolId +
                '}';
    }
}
