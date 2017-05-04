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

// Referenced classes of package slGal.LiveEdu:
//            TranslateSymbols, TranslateCompHigh
public class Translate {

    private List<TranslateSymbols> alph;
    private String charset;

    public Translate(File fileName, String charset)
            throws UnsupportedEncodingException, FileNotFoundException {
        this.charset = charset;

        ArrayList alphArray = new ArrayList();
        //File file = new File(fileName);
        FileInputStream inFile = new FileInputStream(fileName);
        BufferedReader in = new BufferedReader(new InputStreamReader(inFile, charset));
        int size = 0;
        try {
            String str = in.readLine();
//            char cc = str.charAt(0);
//            System.out.println(cc);
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

//    public String translating1(String str, String charset)
//            throws UnsupportedEncodingException {
//        
//        assert (str != null);
//        assert (charset != null);        
//        
//        String rez = new String(str.getBytes(charset), this.charset);
//        for (int i = 0; i < alph.size(); i++) {
//            TranslateSymbols t = (TranslateSymbols) alph.get(i);
//            String symbol = t.getSymbols();
//            String translate = t.getTranslate();
//            if (rez.contains(symbol)) {
//                if (translate.length() > 1) {
//                    translate = (new StringBuilder()).append(translate.substring(0, 1)).append(translate.substring(1).toLowerCase()).toString();
//                }
//                rez = rez.replaceAll(symbol, translate);
//                rez = rez.replaceAll(symbol.toLowerCase(), translate.toLowerCase());
//            } else {
//                rez = rez.replaceAll(symbol.toLowerCase(), translate.toLowerCase());
//            }
//        }
//
//        return rez;
//    }
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