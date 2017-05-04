package slGal.LiveEdu.ORM;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Path;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.csveed.annotations.CsvCell;
import org.csveed.annotations.CsvDate;
import org.csveed.annotations.CsvFile;
import org.hibernate.Session;
import slGal.LiveEdu.PasswordGenerator;
import slGal.LiveEdu.Translate;

// Referenced classes of package slGal.LiveEdu:
//            Student, PDFManager
@CsvFile
public abstract class Person {
    //  -------------------------------------------
    
    public static final String EMAIL_DOMEN = "@hneu.net";
    private static Path nameFileAlphabetic;
    protected int id;    
    
    @CsvCell (columnName = "Фамилия")
    protected String firstname;
    
    @CsvCell (columnName = "Имя")
    protected String lastname;
    
    @CsvCell (columnName = "Отчество")
    protected String patronymic;
    
    protected String firstnameEn;
    protected String lastnameEn;
    protected String patronymicEn;
    protected String email;
    private String liveemail;
    protected String password;
    
    @CsvCell (columnName = "Личные данные.украинец?")
    protected Boolean ukrainian;
    
    @CsvCell (columnName = "нач. обуч.")
    @CsvDate (format = "dd.MM.yyyy")
    protected Calendar dateLiveReg;
    
    protected Boolean pdf;
    protected int typeOfEmail;
    protected boolean dismiss;
    
    public int getTypeOfEmail() {
        return typeOfEmail;
    }

    public void setTypeOfEmail(int typeOfEmail) {
        this.typeOfEmail = typeOfEmail;
    }

    public String getPatronymicEn() {
        return patronymicEn;
    }

    public Boolean getPdf() {
        return pdf;
    }

    public void setPdf(Boolean pdf) {
        this.pdf = pdf;
    }

    public boolean getDismiss() {
        return dismiss;
    }
    
    public boolean isDismiss() {
        return dismiss;
    }

    public void setDismiss(boolean dismiss) {
        this.dismiss = dismiss;
    }

    public void setPatronymicEn(String patronymicEn) {
        this.patronymicEn = patronymicEn;
    }

    public String getFirstname() {
        return firstname;
    }

    public Boolean isUkrainian() {
        return ukrainian;
    }

    public Boolean getUkrainian() {
        return ukrainian;
    }

    public void setUkrainian(Boolean ukrainian) {
        this.ukrainian = ukrainian;
    }

    public int getId() {
        return id;
    }

    public void setId(int ID) {
        this.id = ID;
    }

    public String getLastname() {
        return lastname;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public String getFirstnameEn() {
        return firstnameEn;
    }

    public String getLastnameEn() {
        return lastnameEn;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Calendar getDateLiveReg() {
        return dateLiveReg;
    }

//    public Person setFirstname(String firstname) {
//        this.firstname = firstname;
//        return this;
//    }
    
    public void setFirstname(String firstname) {
        this.firstname = firstname;
        //return this;
    }

    public Person setLastname(String lastname) {
        this.lastname = lastname;
        return this;
    }

    public Person setPatronymic(String patronymic) {
        this.patronymic = patronymic;
        return this;
    }

    public Person setFirstnameEn(String firstnameEn) {
        this.firstnameEn = firstnameEn;
        return this;
    }

    public Person setLastnameEn(String lastnameEn) {
        this.lastnameEn = lastnameEn;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Person setPassword(String password) {
        this.password = password;
        return this;
    }

//    public Person setDateLiveReg(Calendar dateLiveReg) {
//        this.dateLiveReg = dateLiveReg;
//        return this;
//    }

    public void setDateLiveReg(Calendar dateLiveReg) {
        this.dateLiveReg = dateLiveReg;
        //return this;
    }

    public void setDateLiveReg(Date dateLiveReg) {        
        this.dateLiveReg = Calendar.getInstance();
        this.dateLiveReg.setTime(dateLiveReg);        
    }
    
    public Person(String firstname, String lastname, String patronymic) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.patronymic = patronymic;
    }

    public void SaveOrUpdate(Session session) {
        session.saveOrUpdate(this);
    }

    public Person() {
    }

    public static void setNameOfAlphabeticFile(Path pRootDir) {
        nameFileAlphabetic = pRootDir.resolve("res/alphabetic.ini");
    }

    public static Path getNameOfAlphabeticFile() {
        return nameFileAlphabetic;
    }

    /**
     * Translate FIO
     */
    public void translateFIO() {
        Translate tr = null;
        try {
            //            tr = new Translate(nameFileAlphabetic, "Cp1251");  // Exception FileNotFoundException in nameFileAlphabetic, UnsupportedEncodingException in "Cp1251"
            tr = new Translate(Person.nameFileAlphabetic.toFile(), "UTF-8"); // Exception FileNotFoundException in nameFileAlphabetic, UnsupportedEncodingException in "Cp1251"
            if (this.firstnameEn == null) {
                setFirstnameEn(tr.translating1(firstname, "UTF-8")); // Exception UnsupportedEncodingException in "Cp1251"
            }
            if (this.lastnameEn == null) {
                setLastnameEn(tr.translating1(lastname, "UTF-8")); // Exception UnsupportedEncodingException in "Cp1251"
            }
            if (this.patronymicEn == null) {
                setPatronymicEn(tr.translating1(patronymic, "UTF-8")); // Exception UnsupportedEncodingException in "Cp1251"
            }
        } catch (FileNotFoundException | UnsupportedEncodingException ex) {
            Logger.getLogger(Translate.class.getName()).log(Level.SEVERE, "translateFIO", ex);
        }
    }

    public boolean generateEmail() {
//        assert (firstnameEn != null);
//        assert (lastname != null);
//        assert (email == null);
//        assert (password == null);

        if (email != null) {
            return false;
        }
        if (this.firstname == null || this.lastnameEn == null) {
            return false;
        }

        StringBuilder sbNewEmail = new StringBuilder()
                .append(this.firstnameEn)
                .append(".")
                .append(this.lastnameEn)
                .append(Person.EMAIL_DOMEN);

        setEmail(sbNewEmail.toString());
        return true;
    }

    public boolean generateEmail(int type) {
//        assert (firstnameEn != null);
//        assert (lastname != null);
//        assert (email == null);
//        assert (password == null);

        if (email != null) {
            return false;
        }
        if (this.firstnameEn == null || this.lastnameEn == null) {
            return false;
        }
        
        String lastnameEnFirst = lastnameEn.split(" ")[0];        
        StringBuilder sbNewEmail = new StringBuilder();
        switch (type) {
            case 1:
                sbNewEmail.append(this.firstnameEn)
                        .append(".")
                        .append(lastnameEnFirst)
                        .append(Person.EMAIL_DOMEN);
                break;
            case 2:
                sbNewEmail.append(lastnameEnFirst)
                        .append(".")
                        .append(this.firstnameEn)
                        .append(Person.EMAIL_DOMEN);                
                break;
            case 3:
                sbNewEmail.append(this.firstnameEn)
                        .append("-")
                        .append(lastnameEnFirst)
                        .append(Person.EMAIL_DOMEN);
                break;
            case 4:
                sbNewEmail.append(lastnameEnFirst)
                        .append("-")
                        .append(this.firstnameEn)
                        .append(Person.EMAIL_DOMEN);                
                break;
        }        
        setEmail(sbNewEmail.toString().replace(" ", ""));
        setTypeOfEmail(type);
        return true;
    }

    /**
     * Create new password. If password is empty when it will set in new password.
     *
     * @return
     */
    public boolean generatePassword() {
        if (password == null) {            
            PasswordGenerator pswGen = new PasswordGenerator.BuilderMask()
                    .appendMask(PasswordGenerator.SYMBOLS_UPPER_ALPHABETIC_ENGLISH, 1)
                    .appendMask(PasswordGenerator.SYMBOLS_LOWER_ALPHABETIC_ENGLISH, 3)
                    .appendMask(PasswordGenerator.SYMBOLS_DIGIT, 4)
                    .build();

            setPassword(pswGen.generate());
            return true;
        }else {
            return false;
        }
    }

    public void clearPassword() {
        setPassword(null);
    }

    public void clearEmail() {
        setEmail(null);
    }

    public String getLiveemail() {
        return liveemail;
    }

    public void setLiveemail(String liveemail) {
        this.liveemail = liveemail;
    }

    @Override
    public String toString() {
        return "Person{" + "ID=" + id + ", firstname=" + firstname + ", lastname=" + lastname + ", patronymic=" + patronymic + ", firstnameEn=" + firstnameEn + ", lastnameEn=" + lastnameEn + ", patronymicEn=" + patronymicEn + ", email=" + email + ", liveemail=" + liveemail + ", password=" + password + ", ukrainian=" + ukrainian + ", dateLiveReg=" + dateLiveReg + ", pdf=" + pdf + ", typeOfEmail=" + typeOfEmail + '}';
    }        
}