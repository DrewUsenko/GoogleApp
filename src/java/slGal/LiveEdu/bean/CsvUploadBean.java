/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package slGal.LiveEdu.bean;

import java.util.List;

/**
 *
 * @author Andrey
 */
public class CsvUploadBean {
    private String titleOfPage;
    private int numberValideRecord;
    private List<String> headOfTable;
    private List<CsvStudentBean> bodyOfTable;
    private List<List<String>> errorOfRecords;

    public String getTitleOfPage() {
        return titleOfPage;
    }

    public void setTitleOfPage(String titleOfPage) {
        this.titleOfPage = titleOfPage;
    }

    public int getNumberValideRecord() {
        return numberValideRecord;
    }

    public void setNumberValideRecord(int numberValideRecord) {
        this.numberValideRecord = numberValideRecord;
    }

    public List<String> getHeadOfTable() {
        return headOfTable;
    }

    public void setHeadOfTable(List<String> headOfTable) {
        this.headOfTable = headOfTable;
    }

    public List<CsvStudentBean> getBodyOfTable() {
        return bodyOfTable;
    }

    public void setBodyOfTable(List<CsvStudentBean> bodyOfTable) {
        this.bodyOfTable = bodyOfTable;
    }

    public List<List<String>> getErrorOfRecords() {
        return errorOfRecords;
    }

    public void setErrorOfRecords(List<List<String>> errorOfRecords) {
        this.errorOfRecords = errorOfRecords;
    }        
}
