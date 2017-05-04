/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package slGal.LiveEdu;

/**
 *
 * @author Andrey
 */

public class TeacherConst {
    public static final String ATTRIBUTE_FACULTY_LIST = "ATTRIBUTE_FACULTY_LIST";
    public static final String ATTRIBUTE_DEPARTMENT_LIST = "ATTRIBUTE_DEPARTMENT_LIST";
    
    public static final String PARAMETER_FACULTY = "FACULTY";
    public static final String PARAMETER_DEPARTMENT = "DEPARTMENT";
    public static final String PARAMETER_DISMISS = "DISMISS";
    public static final String ATTRIBUTE_TEACHERS = "ATTRIBUTE_TEACHER";
    public static final String ATTRIBUTE_CSV_IMPORT = "ATTRIBUTE_CSV_IMPORT";
    public static final String ACTION_FILTER = "ACTION_FILTER";

    public String getACTION_FILTER() {
        return ACTION_FILTER;
    }
    
    public String getATTRIBUTE_DEPARTMENT_LIST() {
        return ATTRIBUTE_DEPARTMENT_LIST;
    }

    public String getATTRIBUTE_TEACHERS() {
        return ATTRIBUTE_TEACHERS;
    }
    
    public String getATTRIBUTE_FACULTY_LIST() {
        return ATTRIBUTE_FACULTY_LIST;
    }

    public String getPARAMETER_FACULTY() {
        return PARAMETER_FACULTY;
    }

    public String getPARAMETER_DEPARTMENT() {
        return PARAMETER_DEPARTMENT;
    }
    
    public String getPARAMETER_DISMISS() {
        return PARAMETER_DISMISS;
    }    
}
