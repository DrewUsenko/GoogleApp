package slGal.LiveEdu.ORM;
// Generated 04.05.2017 21:56:35 by Hibernate Tools 4.3.1



/**
 * PersoneServicesConsultation generated by hbm2java
 */
public class PersoneServicesConsultation  implements java.io.Serializable {


     private byte id;
     private PersonInf personInf;
     private Services services;

    public PersoneServicesConsultation() {
    }

	
    public PersoneServicesConsultation(byte id) {
        this.id = id;
    }
    public PersoneServicesConsultation(PersonInf personInf, Services services) {
       //this.id = id;
       this.personInf = personInf;
       this.services = services;
    }
   
    public byte getId() {
        return this.id;
    }
    
    public void setId(byte id) {
        this.id = id;
    }
    public PersonInf getPersonInf() {
        return this.personInf;
    }
    
    public void setPersonInf(PersonInf personInf) {
        this.personInf = personInf;
    }
    public Services getServices() {
        return this.services;
    }
    
    public void setServices(Services services) {
        this.services = services;
    }




}


