// Decompiled by DJ v3.9.9.91 Copyright 2005 Atanas Neshkov  Date: 01.03.2010 19:39:29
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   LiveManager.java

package slGal.LiveEdu;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import slGal.LiveEdu.ORM.Person;



// Referenced classes of package slGal.LiveEdu:
//            FileCSVWriter, Person

public class LiveManager
{

    public LiveManager(){
    }

    public static void CreateFile(String fileName, List persons) throws FileNotFoundException, IOException
    {
        FileCSVWriter outFileLiveEdu;
        FileCSVWriter outFileMSDNAA;
        String nameFileLiveEdu = "";
        String nameFileMSDNAA = "";
        int indexSeparator = fileName.lastIndexOf(".");
        if(indexSeparator != -1)
        {
            String nameFilePrefix = fileName.substring(0, indexSeparator);
            String extentionFile = fileName.substring(indexSeparator + 1);
            nameFileLiveEdu = (new StringBuilder()).append(nameFilePrefix)
                    .append(" [Live@edu]")
                    .append("-")
                    //.append(new Date().getTime())
                    .append(".")
                    .append(extentionFile)
                    .toString();
            nameFileMSDNAA = (new StringBuilder()).append(nameFilePrefix)
                    .append(" [MSDNAA]")
                    .append("-")
                    //.append(new Date().getTime())
                    .append(".txt")
                    .toString();
        }
        FileCSVWriter.enableInfo();
        
        //outFileLiveEdu = new FileCSVWriter((new StringBuilder()).append(nameFileLiveEdu).toString(), charsetLiveEdu, headLiveEdu);
        outFileLiveEdu = new FileCSVWriter(nameFileLiveEdu, charsetLiveEdu, headLiveEdu);

        //outFileMSDNAA = new FileCSVWriter((new StringBuilder()).append(nameFileMSDNAA).toString(), charsetLiveEdu, headMSDNAA);
        outFileMSDNAA = new FileCSVWriter(nameFileMSDNAA, charsetLiveEdu, headMSDNAA);
        
        String dataLiveEdu[] = new String[headLiveEdu.length];
        String dataMSDNAA[];
        for(Iterator i$ = persons.iterator(); i$.hasNext(); outFileMSDNAA.writeData(dataMSDNAA))
        {
            Person person = (Person)i$.next();
            dataLiveEdu[0] = person.getEmail();
            dataLiveEdu[1] = person.getFirstname();
            dataLiveEdu[2] = person.getLastname();
            dataLiveEdu[3] = person.getPassword();
            outFileLiveEdu.writeData(dataLiveEdu);
            dataMSDNAA = (new String[] {
                ""

//            Person person = (Person)i$.next();
//            dataLiveEdu[0] = "Add";
//            dataLiveEdu[1] = person.getEmail();
//            dataLiveEdu[2] = person.getPass();
//            dataLiveEdu[3] = "false";
//            dataLiveEdu[4] = person.getFirstname();
//            dataLiveEdu[5] = person.getLastname();
//            dataLiveEdu[6] = "1058";
//            outFileLiveEdu.writeData(dataLiveEdu);
//            dataMSDNAA = (new String[] {
//                ""
            });
            dataMSDNAA[0] = dataLiveEdu[0];
        }
        outFileLiveEdu.close();
        outFileMSDNAA.close();

    }

    static String headLiveEdu[] = {
        //"Action", "Membername", "Password", "ResetPassword", "Personal2_CS.Name.First", "Personal2_CS.Name.Last", "Personal_CS.LangPreference"
        "Email Address", "First Name", "Last Name", "Password"
    };
    static String headMSDNAA[] = {
        ""
    };
    static String charsetLiveEdu = "UTF-8";

}