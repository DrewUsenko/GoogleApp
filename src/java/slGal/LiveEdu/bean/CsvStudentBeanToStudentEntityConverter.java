/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package slGal.LiveEdu.bean;

import net.hneu.googleapp.parser.CsvReaderStudent;
import org.supercsv.io.CsvBeanReader;
import slGal.LiveEdu.ORM.StudentInf;

/**
 *
 * @author Andrey
 */
public class CsvStudentBeanToStudentEntityConverter implements Converter<CsvStudentBean, StudentInf>{

    @Override
    public StudentInf convert(CsvStudentBean source) {
        StudentInf destination = new StudentInf();        
        destination.setStudentCard(source.getCard());
        destination.setDateLiveReg(source.getDateLiveReg());
        destination.setStudies(Boolean.valueOf(source.getExtramuralStudent()));
        destination.setEdbo(source.getEdbo());
        //destination.setFaculty(source.getFaculty());
        destination.getPersonInf().setFirstname(source.getFirstName());
        destination.setGroup(source.getGroupa());
        destination.getPersonInf().setLastname(source.getLastName());
        destination.getPersonInf().setPatronymic(source.getPatronymic());
        //destination.setSpecialityName(source.getSpecialityName());
        //destination.setSpec(source.getSpecialityNumber());                
        destination.setUkrainian(source.isUkrainian());
        return destination;
    }
}
