// Decompiled by DJ v3.9.9.91 Copyright 2005 Atanas Neshkov  Date: 01.03.2010 19:34:47
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   FileCSVWriter.java

package slGal.LiveEdu;

import java.io.*;

public class FileCSVWriter
{

    public FileCSVWriter(File fileName, String charset, String head[])
        throws FileNotFoundException, UnsupportedEncodingException, IOException
    {
        separator = ",";
       // if(!$assertionsDisabled && fileName != null)
         //if(fileName != null)
           // throw new AssertionError("\u0418\u043C\u044F \u0444\u0430\u0439\u043B\u0430 \u043D\u0435 \u0437\u0430\u0434\u0430\u043D\u043E");
        if(fileName != null)
        {
            if(fileName.exists())
//                throw new AssertionError((new StringBuilder()).append(fileName).append(" \u043D\u0435 \u0441\u0443\u0449\u0435\u0441\u0442\u0432\u0443\u0435\u0442").toString());
                throw new AssertionError((new StringBuilder()).append(fileName).append(" Файл существует").toString());
            fos = new FileOutputStream(fileName);
            osr = charset == null ? new OutputStreamWriter(fos) : new OutputStreamWriter(fos, charset);
            br = new BufferedWriter(osr);
        }
        this.fileName = fileName;
        this.charset = charset;
        this.head = head;
        separator = new String(separator.getBytes(this.charset), this.charset);
        writeHead();
        if(info)
            System.out.println((new StringBuilder()).append("\u0424\u0430\u0439\u043B ").append(this.fileName).append(" \u043E\u0442\u043A\u0440\u044B\u0442 \u0434\u043B\u044F \u0437\u0430\u043F\u0438\u0441\u0438.").toString());
    }

    public FileCSVWriter(String fileName, String charset, String head[])
        throws FileNotFoundException, IOException
    {
        this(new File(fileName), charset, head);
    }

    public void close()
        throws IOException
    {
        br.close();
        osr.close();
        fos.close();
        if(info)
            System.out.println((new StringBuilder()).append("\u0424\u0430\u0439\u043B ").append(fileName).append(" \u0437\u0430\u043F\u0438\u0441\u0430\u043D.").toString());
    }

    public void writeHead()
        throws IOException
    {
        String str = "";
        for(int i = 0; i < head.length; i++)
            str = (new StringBuilder()).append(str).append(head[i]).append(i == head.length - 1 ? "" : separator).toString();

        str = (new StringBuilder()).append(str).append("\n").toString();
        br.write(str);
    }

    public void writeData(String data[])
        throws IOException
    {
        String str = "";
        for(int i = 0; i < data.length; i++)
            str = (new StringBuilder()).append(str).append(data[i]).append(i == data.length - 1 ? "" : separator).toString();

        str = (new StringBuilder()).append(str).append("\n").toString();
        br.write(str);
    }

    public static void enableInfo()
    {
        info = true;
    }

    public static void disableInfo()
    {
        info = false;
    }

    private static boolean info;
    private String head[];
    private File fileName;
    private String charset;
    private FileOutputStream fos;
    private OutputStreamWriter osr;
    private BufferedWriter br;
    private String separator;
   //static final boolean $assertionsDisabled = !slGal/LiveEdu/FileCSVWriter.desiredAssertionStatus();

}