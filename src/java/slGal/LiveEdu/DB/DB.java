/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package slGal.LiveEdu.DB;

/**
 *
 * @author Andrey
 */
public interface DB {

    public final String DB_NAME = "googleschema";

    public interface Person {
        
        public final String TABEL_NAME = "person_inf";
        
        public final String COLUM_ID = "idPerson";
        public final String COLUM_FIRST_NAME = "firstname";
        public final String COLUM_LAST_NAME = "lastname";
        public final String COLUM_PATRONYMIC_NAME = "patronymic";
        public final String COLUM_IIN = "iin";
        public final String COLUM_FIRST_NAME_EN = "firstname_en";
        public final String COLUM_LAST_NAME_EN = "lastname_en";
        public final String COLUM_PATRONYMIC_NAME_EN = "patronymic_en";
        public final String COLUM_EMAIL_PERSONAL = "email_personal";
        public final String COLUM_EMAIL_CORPORATE = "email_corporate";
        public final String COLUM_LDAP = "gitlab";
        public final String COLUM_MOODLE = "moodle";
        public final String COLUM_EXIST = "exist";
        public final String COLUM_EMAIL_PDF = "pdf";
        public final String COLUM_PASS_PERSONAL = "pass_personal";
        public final String COLUM_PASS_CORPORATE = "pass_corporate";
    }
    
    public interface Services {
        
        public final String TABEL_NAME = "Services"; 
        
        public final String COLUM_ID = "idServices";
        public final String COLUM_SERVICE_NAME = "name";
        public final String COLUM_SERVICE_URL = "service_url";
                
    }    
       
    public interface Faculty {
        
        public final String TABEL_NAME = "Faculty_inf"; 
        
        public final String COLUM_ID = "idFaculty";
        public final String COLUM_FACULTY_NAME = "faculty";
        public final String COLUM_FACULTY_FULL = "facultyFull";
        
    }
    
    public interface Spec {
        
        public final String TABEL_NAME = "Spec_inf"; 
        
        public final String COLUM_ID = "idSpec";
        public final String COLUM_SPEC_NAME = "Spec";
        public final String COLUM_SPEC_NUMBER = "numberSpec";
        public final String COLUM_RACULTUID = "Faculty_inf_idFaculty";
        
    }
    
    public interface Department {
        
        public final String TABEL_NAME = "Department_inf"; 
        
        public final String COLUM_ID = "idDepartment";
        public final String COLUM_DEPARTMENT_NAME = "department";
        public final String COLUM_DEPARTMENT_FULL = "departmentFull";
        public final String COLUM_FACULTYID = "Faculty_inf_idFaculty";
        
    }    
    
    public interface Stuff {
        
        public final String TABEL_NAME = "Stuff_inf"; 
        
        public final String COLUM_ID = "idStuff";
        public final String COLUM_DISMISS = "dismiss";
        public final String COLUM_POST = "stuff_post";
        public final String COLUM_CREATION_TIME = "create_time";
        public final String COLUM_UPDATE_TIME = "update_time";
        public final String COLUM_DEPARTMENTID = "Department_inf_idDepartment";
        public final String COLUM_PERSONID = "Person_inf_idPerson";
        
    }    
    
    public interface Student {
        
        public final String TABEL_NAME = "Student_inf"; 
        
        public final String COLUM_ID = "idStudent";
        public final String COLUM_UKRAINIAN = "ukrainian";
        public final String COLUM_STUDIES = "studies";
        public final String COLUM_DATE_LIVE_REG = "date_live_reg";
        public final String COLUM_EDBO = "edbo";
        public final String COLUM_CARD = "studentCard";
        public final String COLUM_GROUP = "group";
        public final String COLUM_CREATION_TIME = "create_time";
        public final String COLUM_UPDATE_TIME = "update_time";
        public final String COLUM_PERSONEID = "Person_inf_idPerson";
        public final String COLUM_SPECID = "Spec_inf_idSpec";
        
    }    
    
    public interface Persone_Services {
        
        public final String TABEL_NAME = "service_person"; 
                
        public final String COLUM_ID_PERSONE = "person_id";
        public final String COLUM_ID_SERVICES = "service_id";
        
    }

}
