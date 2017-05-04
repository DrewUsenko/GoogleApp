/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package slGal.LiveEdu.bean;

import java.util.Date;

/**
 *
 * @author Andrey
 */
public class CsvStudentBean {
    private String lastName;
    private String firstName;
    private String patronymic;
    private boolean ukrainian;
    private Date dateLiveReg;
    private int course;
    private Boolean dismiss;
    private String groupa;
    private String extramuralStudent;
    private String faculty;
    private String facultyFull;
    private String specialityName;
    private String specialityCode;
    private String specialityNumber;
    private String card;
    private String edbo;

    public CsvStudentBean() {
    }

    public String getSpecialityCode() {
        return specialityCode;
    }

    public void setSpecialityCode(String specialityCode) {
        this.specialityCode = specialityCode;
    }

    
    public String getExtramuralStudent() {
        return extramuralStudent;
    }

    public void setExtramuralStudent(String extramuralStudent) {
        this.extramuralStudent = extramuralStudent;
    }

    
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public boolean isUkrainian() {
        return ukrainian;
    }

    public void setUkrainian(boolean ukrainian) {
        this.ukrainian = ukrainian;
    }

    public Date getDateLiveReg() {
        return dateLiveReg;
    }

    public void setDateLiveReg(Date dateLiveReg) {
        this.dateLiveReg = dateLiveReg;
    }

    public int getCourse() {
        return course;
    }

    public void setCourse(int course) {
        this.course = course;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public Boolean getDismiss() {
        return dismiss;
    }

    public void setDismiss(Boolean dismiss) {
        this.dismiss = dismiss;
    }

    public String getGroupa() {
        return groupa;
    }

    public void setGroupa(String groupa) {
        this.groupa = groupa;
    }

    public String getEdbo() {
        return edbo;
    }

    public void setEdbo(String edbo) {
        this.edbo = edbo;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String facult) {
        this.faculty = facult;
    }

    public String getFacultyFull() {
        return facultyFull;
    }

    public void setFacultyFull(String facultyFull) {
        this.facultyFull = facultyFull;
    }

    public String getSpecialityNumber() {
        return specialityNumber;
    }

    public void setSpecialityNumber(String specialityNumber) {
        this.specialityNumber = specialityNumber;
    }

    public String getSpecialityName() {
        return specialityName;
    }

    public void setSpecialityName(String specialityName) {
        this.specialityName = specialityName;
    }

 
}
