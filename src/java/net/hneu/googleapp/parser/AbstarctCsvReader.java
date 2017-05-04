/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.hneu.googleapp.parser;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.supercsv.exception.SuperCsvCellProcessorException;

/**
 *
 * @author Andrey
 */
class AbstarctCsvReader {
    protected final List<SuperCsvCellProcessorException> errorList = new ArrayList<>();
    protected final File fileCSV;
    protected String[] header;
    protected boolean isComplite;
    protected boolean isError;
    protected final List<List<String>> messageErrorList = new ArrayList<>();
    protected final List<CsvEntity> studentList = new ArrayList<>();

    public List<SuperCsvCellProcessorException> getErrorList() {
        return Collections.unmodifiableList(errorList);
    }

    public List<String> getHeader() {
        return new ArrayList<>(Arrays.asList(header));
    }

    public List<List<String>> getMessageErrorList() {
        return messageErrorList;
    }

    public List<CsvEntity> getStudentList() {
        return Collections.unmodifiableList(studentList);
    }

    public boolean isError() {
        return isError;
    }
    
    public AbstarctCsvReader(File fileCSV) {
        this.fileCSV = fileCSV;
    }    
    
    public int countValide() {
        return (int) messageErrorList.stream().filter(x -> x.isEmpty()).count();
    }
}
