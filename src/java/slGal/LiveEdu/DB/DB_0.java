/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package slGal.LiveEdu.DB;

/**
 *
 * @author Andrey
 */
public interface DB_0 {

    public final String DB_NAME = "googleapp";

    public interface Persone {
        public final String TABEL_NAME = "persone";
        public final String COLUM_ID = "id";
        public final String COLUM_FIRST_NAME = "firstname";
        public final String COLUM_LAST_NAME = "lastname";
        public final String COLUM_FIRST_NAME_EN = "firstnameEn";
        public final String COLUM_LAST_NAME_EN = "lastnameEn";
        public final String COLUM_COURCE = "course";
        public final String COLUM_SPEC = "spec";       
        public final String COLUM_FACULTY = "faculty";
        public final String COLUM_DEPARTMENT = "department";        
        public final String COLUM_EMAIL = "email";
        public final String COLUM_PASSWORD = "password";
        public final String COLUM_MSDNAA = "msdnaa";
        public final String COLUM_GROUP = "groupa";
        public final String COLUM_UKRAINIAN = "ukrainian";        
        public final String COLUM_EDBO = "edbo";        
        public final String COLUM_IIN = "iin";
        public final String COLUM_DISMISS = "dismiss";
    }
}
