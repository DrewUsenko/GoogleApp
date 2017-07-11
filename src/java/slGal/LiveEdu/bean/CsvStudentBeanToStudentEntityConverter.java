/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package slGal.LiveEdu.bean;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import slGal.LiveEdu.DB.DB;
import slGal.LiveEdu.DB.HibernateUtil;
import slGal.LiveEdu.ORM.PersonInf;
import slGal.LiveEdu.ORM.SpecInf;

/**
 *
 * @author Andrey
 */
public class CsvStudentBeanToStudentEntityConverter implements Converter<CsvStudentBean, PersonInf> {

    @Override
    public PersonInf convert(CsvStudentBean source) {
        Session ses = HibernateUtil.currentSession();

        SpecInf spec = (SpecInf) ses.createCriteria(SpecInf.class)
                .add(Restrictions.eq(DB.Spec.COLUM_SPEC_NUMBER,source.getSpecialityNumber())).uniqueResult();
        String query = "";
        query = ses.createSQLQuery("INSERT INTO person_inf (`firstname`, `lastname`, `patronymic`)  VALUES ('"+source.getFirstName()+"', '"+source.getLastName()+"', '"+source.getPatronymic()+"');").list().toString();
        
        
        String id = ses.createSQLQuery("SELECT idPerson FROM person_inf WHERE  idPerson=(SELECT MAX(idPerson) FROM person_inf)").list().toString();
        //query.toString();
        int myInt = (source.isUkrainian()) ? 1 : 0;
        int myInt1 = (source.getDismiss()) ? 1 : 0;
        
        query = ses.createSQLQuery("INSERT INTO student_inf (`ukrainian`, `studies`, `date_live_reg`, `EDBO`, `studentCard`, `group`, `Person_inf_idPerson`, `Spec_inf_idSpec`)"
                + " VALUES ('"+myInt+"', '"+myInt1+"', "
                        + "'"+"2010-09-01"+"', '"+source.getEdbo()+"', '"+source.getCard()+"', "
                                + "'"+source.getGroupa()+"', '"+id+"', '"+spec+"');").list().toString();        
        
        PersonInf person = new PersonInf();                        
        
        return person;
    }
}
