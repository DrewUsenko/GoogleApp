package slGal.LiveEdu.ORM;
// Generated 04.05.2017 21:56:35 by Hibernate Tools 4.3.1


import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import slGal.LiveEdu.DB.DB;
import slGal.LiveEdu.DB.HibernateUtil;
import slGal.LiveEdu.Translate;

/**
 * StudentInf generated by hbm2java
 */
public class StudentInf  implements java.io.Serializable {


     private Integer idStudent;
     private PersonInf personInf;
     private SpecInf specInf;
     private boolean ukrainian;
     private boolean studies;
     private Date dateLiveReg;
     private String edbo;
     private String studentCard;
     private String group;
     private Date createTime;
     private Date updateTime;

    public StudentInf() {
    }

	
    public StudentInf(PersonInf personInf, SpecInf specInf, boolean ukrainian, boolean studies, Date dateLiveReg, String edbo, String studentCard, String group) {
        this.personInf = personInf;
        this.specInf = specInf;
        this.ukrainian = ukrainian;
        this.studies = studies;
        this.dateLiveReg = dateLiveReg;
        this.edbo = edbo;
        this.studentCard = studentCard;
        this.group = group;
    }
    public StudentInf(PersonInf personInf, SpecInf specInf, boolean ukrainian, boolean studies, Date dateLiveReg, String edbo, String studentCard, String group, Date createTime, Date updateTime) {
       this.personInf = personInf;
       this.specInf = specInf;
       this.ukrainian = ukrainian;
       this.studies = studies;
       this.dateLiveReg = dateLiveReg;
       this.edbo = edbo;
       this.studentCard = studentCard;
       this.group = group;
       this.createTime = createTime;
       this.updateTime = updateTime;
    }
   
    public Integer getIdStudent() {
        return this.idStudent;
    }   
   
    
    public void setIdStudent(Integer idStudent) {
        this.idStudent = idStudent;
    }
    public PersonInf getPersonInf() {
        return this.personInf;
    }
    
    public void setPersonInf(PersonInf personInf) {
        this.personInf = personInf;
    }
    public SpecInf getSpecInf() {
        return this.specInf;
    }
    
    public void setSpecInf(SpecInf specInf) {
        this.specInf = specInf;
    }
    public boolean isUkrainian() {
        return this.ukrainian;
    }
    
    public void setUkrainian(boolean ukrainian) {
        this.ukrainian = ukrainian;
    }
    public boolean isStudies() {
        return this.studies;
    }
    
    public void setStudies(boolean studies) {
        this.studies = studies;
    }
    public Date getDateLiveReg() {
        return this.dateLiveReg;
    }
    
    public void setDateLiveReg(Date dateLiveReg) {
        this.dateLiveReg = dateLiveReg;
    }
    public String getEdbo() {
        return this.edbo;
    }
    
    public void setEdbo(String edbo) {
        this.edbo = edbo;
    }
    public String getStudentCard() {
        return this.studentCard;
    }
    
    public void setStudentCard(String studentCard) {
        this.studentCard = studentCard;
    }
    public String getGroup() {
        return this.group;
    }
    
    public void setGroup(String group) {
        this.group = group;
    }
    public Date getCreateTime() {
        return this.createTime;
    }
    
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    public Date getUpdateTime() {
        return this.updateTime;
    }
    
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }


    public static ArrayList<String> getAllGroups() {
        Session ses = HibernateUtil.currentSession();
        Criteria cr = ses.createCriteria(StudentInf.class);
        cr.setProjection(Projections.distinct(Projections.property("group")));
        cr.addOrder(Order.asc("group"));
        List<String> groups = cr.list();
        HibernateUtil.closeSession();
        return (ArrayList<String>) groups;
    }
    
    public static ArrayList<String> getAllFaculty() {
        Session ses = HibernateUtil.currentSession();
        Criteria cr = ses.createCriteria(FacultyInf.class);
        cr.setProjection(Projections.distinct(Projections.property("faculty")));
        cr.addOrder(Order.asc("faculty"));
        List<String> faculty = cr.list();
        HibernateUtil.closeSession();
        return (ArrayList<String>) faculty;        
    }
    
    public static List<String> getAllSpecs() {
        Session ses = HibernateUtil.currentSession();
        Criteria cr = ses.createCriteria(SpecInf.class);
        cr.setProjection(Projections.distinct(Projections.property("spec")));
        cr.addOrder(Order.asc("spec"));
        List<String> spec = cr.list();
        HibernateUtil.closeSession();
        return (ArrayList<String>) spec;  
    }
    
    
    
}


