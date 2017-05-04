package slGal.LiveEdu.ORM;
// Generated 04.05.2017 21:56:35 by Hibernate Tools 4.3.1


import java.util.HashSet;
import java.util.Set;

/**
 * SpecInf generated by hbm2java
 */
public class SpecInf  implements java.io.Serializable {


     private Integer idSpec;
     private FacultyInf facultyInf;
     private String spec;
     private String numberSpec;
     private Set<StudentInf> studentInfs = new HashSet<StudentInf>(0);

    public SpecInf() {
    }

	
    public SpecInf(FacultyInf facultyInf, String spec, String numberSpec) {
        this.facultyInf = facultyInf;
        this.spec = spec;
        this.numberSpec = numberSpec;
    }
    public SpecInf(FacultyInf facultyInf, String spec, String numberSpec, Set<StudentInf> studentInfs) {
       this.facultyInf = facultyInf;
       this.spec = spec;
       this.numberSpec = numberSpec;
       this.studentInfs = studentInfs;
    }
   
    public Integer getIdSpec() {
        return this.idSpec;
    }
    
    public void setIdSpec(Integer idSpec) {
        this.idSpec = idSpec;
    }
    public FacultyInf getFacultyInf() {
        return this.facultyInf;
    }
    
    public void setFacultyInf(FacultyInf facultyInf) {
        this.facultyInf = facultyInf;
    }
    public String getSpec() {
        return this.spec;
    }
    
    public void setSpec(String spec) {
        this.spec = spec;
    }
    public String getNumberSpec() {
        return this.numberSpec;
    }
    
    public void setNumberSpec(String numberSpec) {
        this.numberSpec = numberSpec;
    }
    public Set<StudentInf> getStudentInfs() {
        return this.studentInfs;
    }
    
    public void setStudentInfs(Set<StudentInf> studentInfs) {
        this.studentInfs = studentInfs;
    }




}


