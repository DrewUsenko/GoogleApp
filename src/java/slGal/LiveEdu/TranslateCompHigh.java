// Decompiled by DJ v3.9.9.91 Copyright 2005 Atanas Neshkov  Date: 01.03.2010 19:41:21
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   Translate.java

package slGal.LiveEdu;

import java.util.Comparator;

class TranslateCompHigh
    implements Comparator
{

    TranslateCompHigh()
    {
    }
    
    public int compare(TranslateSymbols a, TranslateSymbols b)
    {
        int lena = a.getSymbols().length();
        int lenb = b.getSymbols().length();
        if(lena == lenb) return 0;     
        return lena > lenb ? -1 : 1;
    }
    
    @Override
    public int compare(Object x0, Object x1)
    {
        return compare((TranslateSymbols)x0, (TranslateSymbols)x1);
    }
}