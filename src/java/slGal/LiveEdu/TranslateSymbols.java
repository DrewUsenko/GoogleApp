// Decompiled by DJ v3.9.9.91 Copyright 2005 Atanas Neshkov  Date: 01.03.2010 19:41:34
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   TranslateSymbols.java
package slGal.LiveEdu;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TranslateSymbols {

    private String mSymbols;
    private String mTranslateInFirst;
    private String mTranslateInNext;
    
    public TranslateSymbols() {
    }

    public TranslateSymbols(String symbols, String translate) {
        this.mSymbols = symbols;
        this.mTranslateInFirst = translate;
    }

    public TranslateSymbols(TranslateSymbols original) {
        TranslateSymbols tmp = null;
        try {
            tmp = (TranslateSymbols) original.clone();
        } catch (CloneNotSupportedException e) {
        }
        mSymbols = tmp.mSymbols;
        mTranslateInFirst = tmp.mTranslateInFirst;
    }

    public String getSymbols() {
        return mSymbols;
    }

    public String getTranslateFirst() {
        return mTranslateInFirst;
    }

    public String getTranslateNext() {
        return mTranslateInNext;
    }
    
    @Override
    public String toString() {
        return (new StringBuilder()).append(mSymbols).append(" - ").append(mTranslateInFirst).toString();
    }

    public void readData(BufferedReader in) {
        try {
            String s = in.readLine(); // Exception IOExeption
            String separator = ";";
            StringTokenizer t = new StringTokenizer(s, separator);
            
            mSymbols = t.nextToken().trim();
            mTranslateInFirst = t.nextToken().trim();
            mTranslateInNext = t.nextToken().trim();
        } catch (IOException ex) {
            Logger.getLogger(Translate.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}