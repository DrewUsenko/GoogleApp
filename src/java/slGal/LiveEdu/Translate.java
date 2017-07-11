// Decompiled by DJ v3.9.9.91 Copyright 2005 Atanas Neshkov  Date: 01.03.2010 19:41:09
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   Translate.java
package slGal.LiveEdu;

import edu.hneu.googleapp.utill.StringExt;
import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Translate {

    private List<TranslateSymbols> alph;
    private String charset;

    public Translate(File fileName, String charset)
            throws UnsupportedEncodingException, FileNotFoundException {
        this.charset = charset;

        ArrayList alphArray = new ArrayList();
        FileInputStream inFile = new FileInputStream(fileName);
        BufferedReader in = new BufferedReader(new InputStreamReader(inFile, charset));
        int size = 0;
        try {
            String str = in.readLine();
            size = Integer.parseInt(str); // Exception NumberFormatException
            alph = alphArray;
            for (int i = 0; i < size; i++) {
                TranslateSymbols newTS = new TranslateSymbols();
                newTS.readData(in);  // Exception IOException
                alph.add(newTS);
            }
            java.util.Comparator c = new TranslateCompHigh();
            Collections.sort(alph, c);

        } catch (NumberFormatException e) {
            System.out.println("Неверный формат файла " + e.toString());
            e.printStackTrace();
//            System.exit(-1);
        } catch (IOException ex) {
            System.out.println("Неверный формат файла");
            Logger.getLogger(Translate.class.getName()).log(Level.SEVERE, null, ex);
//            System.exit(-1);
        } finally {
            try {
                in.close();
                inFile.close();
            } catch (IOException ex) {
                Logger.getLogger(Translate.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public String translating(String pTranslateString, String pCharset)
            throws UnsupportedEncodingException {

        assert (pTranslateString != null);
        assert (pCharset != null);

        String result = pTranslateString.toLowerCase();
        for (TranslateSymbols ts : alph) {
            switch (result.indexOf(ts.getSymbols())) {
                case -1:
                    break;
                case 0:
                    result = result.replaceFirst(ts.getSymbols(), ts.getTranslateFirst());
                default:
                    result = result.replaceAll(ts.getSymbols(), ts.getTranslateNext());
            }
        }
        return StringExt.toUpperCaseFirtLitere(result);
    }

    public String translating1(String pTranslateString, String pCharset)
            throws UnsupportedEncodingException {

        assert (pTranslateString != null);
        assert (pCharset != null);
        
        String result = pTranslateString;
        for (TranslateSymbols ts : alph) {
            result = result.replaceAll(StringExt.toUpperCaseFirtLitere(ts.getSymbols()), StringExt.toUpperCaseFirtLitere(ts.getTranslateFirst()));
            result = result.replaceAll(ts.getSymbols().toLowerCase(), ts.getTranslateNext().toLowerCase());
        }
        return result;

    }
}