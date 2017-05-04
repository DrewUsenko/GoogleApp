/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package slGal.LiveEdu.bean;

import net.hneu.googleapp.parser.CsvReaderStudent;
import org.supercsv.io.CsvBeanReader;
import slGal.LiveEdu.ORM.Student;

/**
 *
 * @author Andrey
 */
public class CsvStudentBeanToStudentEntityConverter implements Converter<CsvStudentBean, Student>{

    @Override
    public Student convert(CsvStudentBean source) {
        Student destination = new Student();        
        destination.setCard(source.getCard());
        destination.setCourse(source.getCourse());
        destination.setDateLiveReg(source.getDateLiveReg());
        destination.setDismiss(source.getDismiss());
        destination.setStudies(source.getExtramuralStudent());
        destination.setEdbo(source.getEdbo());
        destination.setFaculty(source.getFaculty());
        destination.setFacultyFull(source.getFacultyFull());
        destination.setFirstname(source.getFirstName());
        destination.setGroupa(source.getGroupa());
        destination.setLastname(source.getLastName());
        destination.setPatronymic(source.getPatronymic());
        //destination.setSpecialityName(source.getSpecialityName());
        destination.setNumberSpec(source.getSpecialityCode());
        destination.setSpec(source.getSpecialityNumber());                
        destination.setUkrainian(source.isUkrainian());
        return destination;
    }
}
