// Decompiled by DJ v3.9.9.91 Copyright 2005 Atanas Neshkov  Date: 01.03.2010 19:39:41
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   Password.java

package slGal.LiveEdu;


public class PasswordOld
{
    private static String lowerAlphabetic = "abcdefghijklmnopqrstuvwxyz";
    private static String upperApphabetic = "ZYXWVUTSRQPONMLKJIHGFEDCBA";
    private static String digit = "1234567890";
    private static String znaki = "!;%:?*()_+=-~/\\<>,.[]{}";
    private int lenPassword;
    private String setAlphabetic;
        
    public PasswordOld(String setAlphabetic, int lenPassword)
    {
        //this.setAlphabetic = "";
        StringBuilder pass=new StringBuilder();
        this.lenPassword = lenPassword;

        if(setAlphabetic.contains("l"))
        {
         pass.append(lowerAlphabetic);
        }
        if(setAlphabetic.contains("u"))
        {
             pass.append(upperApphabetic);
        }
        if(setAlphabetic.contains("d"))
        {
                pass.append(digit);
        }
        if(setAlphabetic.contains("z"))
        {
              pass.append(znaki);
        }
        this.setAlphabetic=pass.toString();
    }
    
    public PasswordOld()
    {
        this("uld", 10);
    }

    public String getPasword()
    {
        String res = "";
        for(int i = 0; i < lenPassword; i++)
        {
            int r = (int)Math.floor(Math.random() * (double)setAlphabetic.length());
            res = (new StringBuilder()).append(res).append(setAlphabetic.substring(r, r + 1)).toString();
        }
        return res;
    }
}