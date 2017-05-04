/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package slGal.LiveEdu.bean;

import net.hneu.googleapp.parser.CsvReaderStudent;
import org.supercsv.io.CsvBeanReader;

/**
 *
 * @author Andrey
 */
public class CsvStudentToCsvStudentBeanConverter implements Converter<CsvReaderStudent.CsvStudent, CsvStudentBean>{

    @Override
    public CsvStudentBean convert(CsvReaderStudent.CsvStudent source) {
        CsvStudentBean destination = new CsvStudentBean();        
        destination.setCard(source.getCard());
        destination.setCourse(source.getCourse());
        destination.setDateLiveReg(source.getDateLiveReg());
        destination.setDismiss(source.getDismiss());
        destination.setExtramuralStudent(source.getExtramuralStudent());
        destination.setEdbo(source.getEdbo());
        destination.setFaculty(source.getFaculty());
        destination.setFacultyFull(source.getFacultyFull());
        destination.setFirstName(source.getFirstName());
        destination.setGroupa(source.getGroupa());
        destination.setLastName(source.getLastName());
        destination.setPatronymic(source.getPatronymic());
        destination.setSpecialityName(source.getSpecialityName());
        destination.setSpecialityCode(source.getSpecialityCode());
        destination.setSpecialityNumber(source.getSpecialityNumber());                
        destination.setUkrainian(source.isUkrainian());
        return destination;
    }
}
