// Decompiled by DJ v3.9.9.91 Copyright 2005 Atanas Neshkov  Date: 01.03.2010 19:39:50
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   PDFManager.java
package slGal.LiveEdu;

import com.lowagie.text.*;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import slGal.LiveEdu.ORM.Person;
import slGal.LiveEdu.ORM.*;

// Referenced classes of package slGal.LiveEdu:
//            Person
public class PDFManager {

//    private static Path ROOT = Paths.get(".");
    private static Path RESOURCE = Paths.get("res");
    private static final String FONT_LOCATION = "Liberation Sans.ttf";
    private static final String FONT_LOCATION_BOLD = "LiberationSans-Bold.ttf";
    private static final String PAGE1_LOCATION = "page1new.txt";
    private static final String PAGE2_LOCATION = "page2.txt";
    private static final String PAGE3_LOCATION = "page3.txt";

    public PDFManager() {
    }

    public static void setResurce(Path rootPath) {
        RESOURCE = rootPath;
    }

    public static void createPDF_New(Person person, Path pathDir, boolean msdnFlag, boolean office365Flag) {
        Document document;
        if (msdnFlag && office365Flag) {
            document = new Document(PageSize.A4, 15F, 15F, 15F, 15F);
        } else if (msdnFlag || office365Flag) {
            document = new Document(PageSize.A5.rotate(), 15F, 15F, 15F, 15F);
        } else {
            document = new Document(PageSize.A6, 15F, 15F, 15F, 15F);
        }

        try {
            String fileName = (new StringBuilder())
                    .append(person.getLastname())
                    .append("_")
                    .append(person.getFirstname())
                    .append(".pdf")
                    .toString();

            PdfWriter.getInstance(document, new FileOutputStream(pathDir.resolve(fileName).toFile()));
            document.open();

            int numberCol = (msdnFlag || office365Flag) ? 2 : 1;
            PdfPTable table = new PdfPTable(numberCol);

            table.getDefaultCell().setBorder(0);
            table.addCell(liveEdu(person));

            if (office365Flag) {
                table.addCell(office365(person));
            }

            if (msdnFlag) {
                table.addCell(msdnAA(person));
            }

            if (msdnFlag && office365Flag) {
                table.addCell(empty(person));
            }

//            table.setWidthPercentage((msdnFlag || office365Flag) ? 100F : 50F);
            table.setWidthPercentage(100F);
            document.add(table);
        } catch (DocumentException | IOException ex) {
            Logger.getLogger(PDFManager.class.getName()).log(Level.SEVERE, "message", ex);
        } finally {
            document.close();
        }
    }

    private static PdfPTable liveEdu(Person person) throws BadElementException, IOException, DocumentException {
        BaseFont fontNormal = BaseFont.createFont(RESOURCE.resolve(FONT_LOCATION).toString(), "Identity-H", false);
        BaseFont fontBold = BaseFont.createFont(RESOURCE.resolve(FONT_LOCATION_BOLD).toString(), "Identity-H", false);

        Font fontNormal10 = new Font(fontNormal, 10F, 0);
        Font fontNormal12 = new Font(fontNormal, 12F, 0);
        Font fontBold12 = new Font(fontBold, 12F, 0);

        PdfPTable tableLiveEdu = new PdfPTable(1);

        tableLiveEdu.setWidthPercentage(100F);
        tableLiveEdu.getDefaultCell().setBorder(0);
        tableLiveEdu.addCell(loadPicture(RESOURCE.resolve("logo1.JPG").toString()));

        FileInputStream fstream = new FileInputStream(RESOURCE.resolve(PAGE1_LOCATION).toFile());
        BufferedReader br = new BufferedReader(new InputStreamReader(fstream, "utf-8"));

        // tableLiveEdu.addCell(new Paragraph(br.readLine(), font));
        // line 1
        String line1 = String.format(br.readLine());
        PdfPCell cell = new PdfPCell(new Paragraph(line1, fontNormal12));
        //cell.setLeading(15F, 0.0F);
        cell.setHorizontalAlignment(3);
        cell.setBorder(0);
        tableLiveEdu.addCell(cell);

        // line 2
        String line2 = String.format(br.readLine(), new Object[]{person.getFirstname(), person.getPatronymic(), person.getLastname()});
        PdfPCell cell2 = new PdfPCell(new Paragraph(line2, fontBold12));
        cell2.setHorizontalAlignment(1);
        cell2.setBorder(0);
        //cell2.setLeading(15F, 0.0F);
        tableLiveEdu.addCell(cell2);

        // line 3
        String line3 = String.format(br.readLine());
        PdfPCell cell3 = new PdfPCell(new Paragraph(line3, fontNormal12));
        //cell.setLeading(15F, 0.0F);
        cell3.setHorizontalAlignment(3);
        cell3.setBorder(0);
        tableLiveEdu.addCell(cell3);

        // line 4
        String line4 = String.format(br.readLine());
        PdfPCell cell4 = new PdfPCell(new Paragraph(line4, fontNormal12));
        //cell.setLeading(15F, 0.0F);
        cell4.setHorizontalAlignment(3);
        cell4.setBorder(0);
        tableLiveEdu.addCell(cell4);

//        tableLiveEdu.addCell(new Paragraph(br.readLine(), fontNormal12));
        // line 5
        String line5 = String.format(br.readLine(), new Object[]{person.getEmail()});
        PdfPCell cell5 = new PdfPCell(new Paragraph(line5, fontBold12));
        //cell.setLeading(15F, 0.0F);
        cell5.setHorizontalAlignment(1);
        cell5.setBorder(0);
        tableLiveEdu.addCell(cell5);

        // line 6
        String line6 = String.format(br.readLine());
        PdfPCell cell6 = new PdfPCell(new Paragraph(line6, fontNormal12));
        //cell.setLeading(15F, 0.0F);
        cell6.setHorizontalAlignment(3);
        cell6.setBorder(0);
        tableLiveEdu.addCell(cell6);

        // line 7
        String line7 = String.format(br.readLine(), new Object[]{person.getEmail().replaceAll(Person.EMAIL_DOMEN, "")});
        PdfPCell cell7 = new PdfPCell(new Paragraph(line7, fontBold12));
        //cell.setLeading(15F, 0.0F);
        cell7.setHorizontalAlignment(1);
        cell7.setBorder(0);
        tableLiveEdu.addCell(cell7);

        // line 8
        String line8 = String.format(br.readLine());
        PdfPCell cell8 = new PdfPCell(new Paragraph(line8, fontNormal12));
        cell8.setBorder(0);
        cell8.setHorizontalAlignment(3);
        tableLiveEdu.addCell(cell8);

        // line 9
        String line9 = String.format(br.readLine());
        PdfPCell cell9 = new PdfPCell(new Paragraph(line9, fontBold12));
        cell9.setBorder(0);
        cell9.setHorizontalAlignment(1);
        tableLiveEdu.addCell(cell9);

        // line 10
        String line10 = String.format(br.readLine());
        PdfPCell cell10 = new PdfPCell(new Paragraph(line10, fontNormal12));
        cell10.setBorder(0);
        cell10.setHorizontalAlignment(3);
        tableLiveEdu.addCell(cell10);

        //line 11
        String line11 = String.format(br.readLine(), new Object[]{person.getPassword()});
        PdfPCell cell11 = new PdfPCell(new Paragraph(line11, fontBold12));
        cell11.setBorder(0);
        cell11.setHorizontalAlignment(1);
        tableLiveEdu.addCell(cell11);

        br.close();
        return tableLiveEdu;
    }

    private static PdfPTable msdnAA(Person person) throws FileNotFoundException, IOException {
        Font font = new Font();
        Font font2 = new Font();
        try {
            BaseFont baseFont = BaseFont.createFont(RESOURCE.resolve(FONT_LOCATION).toString(), "Identity-H", false);
            font = new Font(baseFont, 12F, 0);

            BaseFont baseFont2 = BaseFont.createFont(RESOURCE.resolve(FONT_LOCATION_BOLD).toString(), "Identity-H", false);
            font2 = new Font(baseFont2, 12F, 0);
        } catch (DocumentException ex) {
            Logger.getLogger(PDFManager.class.getName()).log(Level.SEVERE, "the font is invalid", ex);
        } catch (IOException ex) {
            Logger.getLogger(PDFManager.class.getName()).log(Level.SEVERE, "the font file could not be read", ex);
        }

        PdfPTable tableMSDNAA = new PdfPTable(1);
        tableMSDNAA.setWidthPercentage(100F);
        tableMSDNAA.getDefaultCell().setBorder(0);

        tableMSDNAA.addCell(loadPicture(RESOURCE.resolve("DreamSpark.jpg").toString()));

        FileInputStream fstream = new FileInputStream(RESOURCE.resolve(PAGE2_LOCATION).toFile());
        BufferedReader br = new BufferedReader(new InputStreamReader(fstream, "utf-8"));

        PdfPCell cell2 = new PdfPCell(new Paragraph(br.readLine(), font));
        cell2.setHorizontalAlignment(3);
        cell2.setBorder(0);
        cell2.setLeading(15F, 0.0F);
        tableMSDNAA.addCell(cell2);

        PdfPCell cell3 = new PdfPCell(new Paragraph(br.readLine(), font));
        cell3.setBorder(0);
        cell3.setHorizontalAlignment(3);
        cell3.setLeading(15F, 0.0F);
        tableMSDNAA.addCell(cell3);

        PdfPCell cell4 = new PdfPCell(new Paragraph(person.getEmail(), font2));
        cell4.setBorder(0);
        cell4.setHorizontalAlignment(1);
        tableMSDNAA.addCell(cell4);

        PdfPCell cell5 = new PdfPCell(new Paragraph(br.readLine(), font));
        cell5.setBorder(0);
        cell5.setHorizontalAlignment(3);
        cell5.setLeading(15F, 0.0F);
        tableMSDNAA.addCell(cell5);

        PdfPCell cell6 = new PdfPCell(new Paragraph(br.readLine(), font2));
        cell6.setBorder(0);
        cell6.setHorizontalAlignment(1);
        tableMSDNAA.addCell(cell6);

        PdfPCell cell7 = new PdfPCell(new Paragraph(br.readLine(), font2));
        br.close();
        cell7.setBorder(0);
        cell7.setHorizontalAlignment(1);
        tableMSDNAA.addCell(cell7);

        return tableMSDNAA;
    }

    private static PdfPTable office365(Person person) throws FileNotFoundException, IOException {
        Font font = new Font();
        Font font2 = new Font();
        try {
            BaseFont baseFont = BaseFont.createFont(RESOURCE.resolve(FONT_LOCATION).toString(), "Identity-H", false);
            font = new Font(baseFont, 12F, 0);

            BaseFont baseFont2 = BaseFont.createFont(RESOURCE.resolve(FONT_LOCATION_BOLD).toString(), "Identity-H", false);
            font2 = new Font(baseFont2, 12F, 0);

        } catch (DocumentException ex) {
            Logger.getLogger(PDFManager.class.getName()).log(Level.SEVERE, "the font is invalid", ex);
        } catch (IOException ex) {
            Logger.getLogger(PDFManager.class.getName()).log(Level.SEVERE, "the font file could not be read", ex);
        }

        PdfPTable tableOffice365 = new PdfPTable(1);
        tableOffice365.setWidthPercentage(100F);
        tableOffice365.getDefaultCell().setBorder(0);

        tableOffice365.addCell(loadPicture(RESOURCE.resolve("office-apps.jpg").toString()));

        FileInputStream fstream = new FileInputStream(RESOURCE.resolve(PAGE3_LOCATION).toFile());
        BufferedReader br = new BufferedReader(new InputStreamReader(fstream, "utf-8"));

        PdfPCell cell1 = new PdfPCell(new Paragraph(br.readLine(), font));
        cell1.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
        cell1.setBorder(0);
        cell1.setLeading(15F, 0.0F);
        tableOffice365.addCell(cell1);

//        String line31 = String.format(br.readLine(), new Object[]{person.getEmail().replaceAll(Person.EMAIL_DOMEN, "")});
        PdfPCell cell2 = new PdfPCell(new Paragraph(br.readLine(), font));
        cell2.setBorder(0);
        cell2.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
//        cell3.setLeading(15F, 0.0F);
        tableOffice365.addCell(cell2);

        String line3 = String.format(br.readLine(), new Object[]{person.getEmail().replaceAll(Person.EMAIL_DOMEN, "")});
        PdfPCell cell3 = new PdfPCell(new Paragraph(line3, font2));
        cell3.setBorder(0);
        cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
//        cell3.setLeading(15F, 0.0F);
        tableOffice365.addCell(cell3);

        String line4 = String.format(br.readLine(), new Object[]{person.getPassword()});
        PdfPCell cell4 = new PdfPCell(new Paragraph(line4, font2));
//        PdfPCell cell4 = new PdfPCell(new Paragraph(person.getEmail(), font2));
        cell4.setBorder(0);
        cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
        tableOffice365.addCell(cell4);

        PdfPCell cell5 = new PdfPCell(new Paragraph(br.readLine(), font));
        cell5.setBorder(0);
        cell5.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
        cell5.setLeading(15F, 0.0F);
        tableOffice365.addCell(cell5);

        PdfPCell cell6 = new PdfPCell(new Paragraph(br.readLine(), font2));
        cell6.setBorder(0);
        cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
        tableOffice365.addCell(cell6);

        PdfPCell cell7 = new PdfPCell(new Paragraph(br.readLine(), font2));
        cell7.setBorder(0);
        cell7.setHorizontalAlignment(Element.ALIGN_CENTER);
        tableOffice365.addCell(cell7);

        br.close();
        return tableOffice365;
    }

    private static PdfPTable empty(Person person) throws FileNotFoundException, IOException {
        Font font = new Font();
        Font font2 = new Font();
        try {
            BaseFont baseFont = BaseFont.createFont(RESOURCE.resolve(FONT_LOCATION).toString(), "Identity-H", false);
            font = new Font(baseFont, 12F, 0);

            BaseFont baseFont2 = BaseFont.createFont(RESOURCE.resolve(FONT_LOCATION_BOLD).toString(), "Identity-H", false);
            font2 = new Font(baseFont2, 12F, 0);

        } catch (DocumentException ex) {
            Logger.getLogger(PDFManager.class.getName()).log(Level.SEVERE, "the font is invalid", ex);
        } catch (IOException ex) {
            Logger.getLogger(PDFManager.class.getName()).log(Level.SEVERE, "the font file could not be read", ex);
        }

        PdfPTable tableOffice365 = new PdfPTable(1);
        tableOffice365.setWidthPercentage(100F);
        tableOffice365.getDefaultCell().setBorder(0);

//        tableOffice365.addCell(loadPicture(RESOURCE.resolve("office-apps.png").toString()));
//        FileInputStream fstream = new FileInputStream(RESOURCE.resolve(PAGE3_LOCATION).toFile());
//        BufferedReader br = new BufferedReader(new InputStreamReader(fstream, "utf-8"));
//
//        PdfPCell cell2 = new PdfPCell(new Paragraph(br.readLine(), font));
//        cell2.setHorizontalAlignment(3);
//        cell2.setBorder(0);
//        cell2.setLeading(15F, 0.0F);
//        tableOffice365.addCell(cell2);
//
//        String line3 = String.format(br.readLine(), new Object[]{person.getEmail().replaceAll(Person.EMAIL_DOMEN, "")});
//        PdfPCell cell3 = new PdfPCell(new Paragraph(line3, font2));
//        cell3.setBorder(0);
//        cell3.setHorizontalAlignment(1);
////        cell3.setLeading(15F, 0.0F);
//        tableOffice365.addCell(cell3);
//
//        String line4 = String.format(br.readLine(), new Object[]{person.getPassword()});
//        PdfPCell cell4 = new PdfPCell(new Paragraph(line4, font2));
////        PdfPCell cell4 = new PdfPCell(new Paragraph(person.getEmail(), font2));
//        cell4.setBorder(0);
//        cell4.setHorizontalAlignment(1);
//        tableOffice365.addCell(cell4);
//
//        PdfPCell cell5 = new PdfPCell(new Paragraph(br.readLine(), font));
//        cell5.setBorder(0);
//        cell5.setHorizontalAlignment(3);
//        cell5.setLeading(15F, 0.0F);
//        tableOffice365.addCell(cell5);
//
//        PdfPCell cell6 = new PdfPCell(new Paragraph(br.readLine(), font2));
//        cell6.setBorder(0);
//        cell6.setHorizontalAlignment(1);
//        tableOffice365.addCell(cell6);
//
//        PdfPCell cell7 = new PdfPCell(new Paragraph(br.readLine(), font2));        
//        cell7.setBorder(0);
//        cell7.setHorizontalAlignment(1);
//        tableOffice365.addCell(cell7);
//
//        br.close();
        return tableOffice365;
    }

    private static Jpeg loadPicture(String pathName) {
        byte[] buffer = new byte[0x186a0];
        File inputFile = new File(pathName);
        BufferedInputStream in = null;
        Jpeg picture = null;
        try {
            in = new BufferedInputStream(new FileInputStream(inputFile));
            if (in.read(buffer) != -1) {
                picture = new Jpeg(buffer);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(PDFManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PDFManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BadElementException ex) {
            Logger.getLogger(PDFManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                in.close();
            } catch (IOException ex) {
                Logger.getLogger(PDFManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return picture;
    }

    public static void createPDF_New(StudentInf student, Path pathDir, boolean msdnFlag, boolean office365Flag) {
        Document document;
        if (msdnFlag && office365Flag) {
            document = new Document(PageSize.A4, 15F, 15F, 15F, 15F);
        } else if (msdnFlag || office365Flag) {
            document = new Document(PageSize.A5.rotate(), 15F, 15F, 15F, 15F);
        } else {
            document = new Document(PageSize.A6, 15F, 15F, 15F, 15F);
        }

        try {
            String fileName = (new StringBuilder())
                    .append(student.getPersonInf().getLastname())
                    .append("_")
                    .append(student.getPersonInf().getFirstname())
                    .append(".pdf")
                    .toString();

            PdfWriter.getInstance(document, new FileOutputStream(pathDir.resolve(fileName).toFile()));
            document.open();

            int numberCol = (msdnFlag || office365Flag) ? 2 : 1;
            PdfPTable table = new PdfPTable(numberCol);

            table.getDefaultCell().setBorder(0);
            table.addCell(liveEdu(student));

            if (office365Flag) {
                table.addCell(office365(student));
            }

            if (msdnFlag) {
                table.addCell(msdnAA(student));
            }

            if (msdnFlag && office365Flag) {
                table.addCell(empty(student));
            }

//            table.setWidthPercentage((msdnFlag || office365Flag) ? 100F : 50F);
            table.setWidthPercentage(100F);
            document.add(table);
        } catch (DocumentException | IOException ex) {
            Logger.getLogger(PDFManager.class.getName()).log(Level.SEVERE, "message", ex);
        } finally {
            document.close();
        }
    }
    
    
    private static PdfPTable liveEdu(StudentInf student) throws BadElementException, IOException, DocumentException {
        BaseFont fontNormal = BaseFont.createFont(RESOURCE.resolve(FONT_LOCATION).toString(), "Identity-H", false);
        BaseFont fontBold = BaseFont.createFont(RESOURCE.resolve(FONT_LOCATION_BOLD).toString(), "Identity-H", false);

        Font fontNormal10 = new Font(fontNormal, 10F, 0);
        Font fontNormal12 = new Font(fontNormal, 12F, 0);
        Font fontBold12 = new Font(fontBold, 12F, 0);

        PdfPTable tableLiveEdu = new PdfPTable(1);

        tableLiveEdu.setWidthPercentage(100F);
        tableLiveEdu.getDefaultCell().setBorder(0);
        tableLiveEdu.addCell(loadPicture(RESOURCE.resolve("logo1.JPG").toString()));

        FileInputStream fstream = new FileInputStream(RESOURCE.resolve(PAGE1_LOCATION).toFile());
        BufferedReader br = new BufferedReader(new InputStreamReader(fstream, "utf-8"));

        // tableLiveEdu.addCell(new Paragraph(br.readLine(), font));
        // line 1
        String line1 = String.format(br.readLine());
        PdfPCell cell = new PdfPCell(new Paragraph(line1, fontNormal12));
        //cell.setLeading(15F, 0.0F);
        cell.setHorizontalAlignment(3);
        cell.setBorder(0);
        tableLiveEdu.addCell(cell);

        // line 2
        String line2 = String.format(br.readLine(), new Object[]{student.getPersonInf().getFirstname(), student.getPersonInf().getPatronymic(), student.getPersonInf().getLastname()});
        PdfPCell cell2 = new PdfPCell(new Paragraph(line2, fontBold12));
        cell2.setHorizontalAlignment(1);
        cell2.setBorder(0);
        //cell2.setLeading(15F, 0.0F);
        tableLiveEdu.addCell(cell2);

        // line 3
        String line3 = String.format(br.readLine());
        PdfPCell cell3 = new PdfPCell(new Paragraph(line3, fontNormal12));
        //cell.setLeading(15F, 0.0F);
        cell3.setHorizontalAlignment(3);
        cell3.setBorder(0);
        tableLiveEdu.addCell(cell3);

        // line 4
        String line4 = String.format(br.readLine());
        PdfPCell cell4 = new PdfPCell(new Paragraph(line4, fontNormal12));
        //cell.setLeading(15F, 0.0F);
        cell4.setHorizontalAlignment(3);
        cell4.setBorder(0);
        tableLiveEdu.addCell(cell4);

//        tableLiveEdu.addCell(new Paragraph(br.readLine(), fontNormal12));
        // line 5
        String line5 = String.format(br.readLine(), new Object[]{student.getPersonInf().getEmailCorporate()});
        PdfPCell cell5 = new PdfPCell(new Paragraph(line5, fontBold12));
        //cell.setLeading(15F, 0.0F);
        cell5.setHorizontalAlignment(1);
        cell5.setBorder(0);
        tableLiveEdu.addCell(cell5);

        // line 6
        String line6 = String.format(br.readLine());
        PdfPCell cell6 = new PdfPCell(new Paragraph(line6, fontNormal12));
        //cell.setLeading(15F, 0.0F);
        cell6.setHorizontalAlignment(3);
        cell6.setBorder(0);
        tableLiveEdu.addCell(cell6);

        // line 7
        String line7 = String.format(br.readLine(), new Object[]{student.getPersonInf().getEmailCorporate().replaceAll(Person.EMAIL_DOMEN, "")});
        PdfPCell cell7 = new PdfPCell(new Paragraph(line7, fontBold12));
        //cell.setLeading(15F, 0.0F);
        cell7.setHorizontalAlignment(1);
        cell7.setBorder(0);
        tableLiveEdu.addCell(cell7);

        // line 8
        String line8 = String.format(br.readLine());
        PdfPCell cell8 = new PdfPCell(new Paragraph(line8, fontNormal12));
        cell8.setBorder(0);
        cell8.setHorizontalAlignment(3);
        tableLiveEdu.addCell(cell8);

        // line 9
        String line9 = String.format(br.readLine());
        PdfPCell cell9 = new PdfPCell(new Paragraph(line9, fontBold12));
        cell9.setBorder(0);
        cell9.setHorizontalAlignment(1);
        tableLiveEdu.addCell(cell9);

        // line 10
        String line10 = String.format(br.readLine());
        PdfPCell cell10 = new PdfPCell(new Paragraph(line10, fontNormal12));
        cell10.setBorder(0);
        cell10.setHorizontalAlignment(3);
        tableLiveEdu.addCell(cell10);

        //line 11
        String line11 = String.format(br.readLine(), new Object[]{student.getPersonInf().getPassCorporate()});
        PdfPCell cell11 = new PdfPCell(new Paragraph(line11, fontBold12));
        cell11.setBorder(0);
        cell11.setHorizontalAlignment(1);
        tableLiveEdu.addCell(cell11);

        br.close();
        return tableLiveEdu;
    }

    private static PdfPTable msdnAA(StudentInf student) throws FileNotFoundException, IOException {
        Font font = new Font();
        Font font2 = new Font();
        try {
            BaseFont baseFont = BaseFont.createFont(RESOURCE.resolve(FONT_LOCATION).toString(), "Identity-H", false);
            font = new Font(baseFont, 12F, 0);

            BaseFont baseFont2 = BaseFont.createFont(RESOURCE.resolve(FONT_LOCATION_BOLD).toString(), "Identity-H", false);
            font2 = new Font(baseFont2, 12F, 0);
        } catch (DocumentException ex) {
            Logger.getLogger(PDFManager.class.getName()).log(Level.SEVERE, "the font is invalid", ex);
        } catch (IOException ex) {
            Logger.getLogger(PDFManager.class.getName()).log(Level.SEVERE, "the font file could not be read", ex);
        }

        PdfPTable tableMSDNAA = new PdfPTable(1);
        tableMSDNAA.setWidthPercentage(100F);
        tableMSDNAA.getDefaultCell().setBorder(0);

        tableMSDNAA.addCell(loadPicture(RESOURCE.resolve("DreamSpark.jpg").toString()));

        FileInputStream fstream = new FileInputStream(RESOURCE.resolve(PAGE2_LOCATION).toFile());
        BufferedReader br = new BufferedReader(new InputStreamReader(fstream, "utf-8"));

        PdfPCell cell2 = new PdfPCell(new Paragraph(br.readLine(), font));
        cell2.setHorizontalAlignment(3);
        cell2.setBorder(0);
        cell2.setLeading(15F, 0.0F);
        tableMSDNAA.addCell(cell2);

        PdfPCell cell3 = new PdfPCell(new Paragraph(br.readLine(), font));
        cell3.setBorder(0);
        cell3.setHorizontalAlignment(3);
        cell3.setLeading(15F, 0.0F);
        tableMSDNAA.addCell(cell3);

        PdfPCell cell4 = new PdfPCell(new Paragraph(student.getPersonInf().getEmailCorporate(), font2));
        cell4.setBorder(0);
        cell4.setHorizontalAlignment(1);
        tableMSDNAA.addCell(cell4);

        PdfPCell cell5 = new PdfPCell(new Paragraph(br.readLine(), font));
        cell5.setBorder(0);
        cell5.setHorizontalAlignment(3);
        cell5.setLeading(15F, 0.0F);
        tableMSDNAA.addCell(cell5);

        PdfPCell cell6 = new PdfPCell(new Paragraph(br.readLine(), font2));
        cell6.setBorder(0);
        cell6.setHorizontalAlignment(1);
        tableMSDNAA.addCell(cell6);

        PdfPCell cell7 = new PdfPCell(new Paragraph(br.readLine(), font2));
        br.close();
        cell7.setBorder(0);
        cell7.setHorizontalAlignment(1);
        tableMSDNAA.addCell(cell7);

        return tableMSDNAA;
    }

    private static PdfPTable office365(StudentInf student) throws FileNotFoundException, IOException {
        Font font = new Font();
        Font font2 = new Font();
        try {
            BaseFont baseFont = BaseFont.createFont(RESOURCE.resolve(FONT_LOCATION).toString(), "Identity-H", false);
            font = new Font(baseFont, 12F, 0);

            BaseFont baseFont2 = BaseFont.createFont(RESOURCE.resolve(FONT_LOCATION_BOLD).toString(), "Identity-H", false);
            font2 = new Font(baseFont2, 12F, 0);

        } catch (DocumentException ex) {
            Logger.getLogger(PDFManager.class.getName()).log(Level.SEVERE, "the font is invalid", ex);
        } catch (IOException ex) {
            Logger.getLogger(PDFManager.class.getName()).log(Level.SEVERE, "the font file could not be read", ex);
        }

        PdfPTable tableOffice365 = new PdfPTable(1);
        tableOffice365.setWidthPercentage(100F);
        tableOffice365.getDefaultCell().setBorder(0);

        tableOffice365.addCell(loadPicture(RESOURCE.resolve("office-apps.jpg").toString()));

        FileInputStream fstream = new FileInputStream(RESOURCE.resolve(PAGE3_LOCATION).toFile());
        BufferedReader br = new BufferedReader(new InputStreamReader(fstream, "utf-8"));

        PdfPCell cell1 = new PdfPCell(new Paragraph(br.readLine(), font));
        cell1.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
        cell1.setBorder(0);
        cell1.setLeading(15F, 0.0F);
        tableOffice365.addCell(cell1);

//        String line31 = String.format(br.readLine(), new Object[]{person.getEmail().replaceAll(Person.EMAIL_DOMEN, "")});
        PdfPCell cell2 = new PdfPCell(new Paragraph(br.readLine(), font));
        cell2.setBorder(0);
        cell2.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
//        cell3.setLeading(15F, 0.0F);
        tableOffice365.addCell(cell2);

        String line3 = String.format(br.readLine(), new Object[]{student.getPersonInf().getEmailCorporate().replaceAll(Person.EMAIL_DOMEN, "")});
        PdfPCell cell3 = new PdfPCell(new Paragraph(line3, font2));
        cell3.setBorder(0);
        cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
//        cell3.setLeading(15F, 0.0F);
        tableOffice365.addCell(cell3);

        String line4 = String.format(br.readLine(), new Object[]{student.getPersonInf().getPassCorporate()});
        PdfPCell cell4 = new PdfPCell(new Paragraph(line4, font2));
//        PdfPCell cell4 = new PdfPCell(new Paragraph(person.getEmail(), font2));
        cell4.setBorder(0);
        cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
        tableOffice365.addCell(cell4);

        PdfPCell cell5 = new PdfPCell(new Paragraph(br.readLine(), font));
        cell5.setBorder(0);
        cell5.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
        cell5.setLeading(15F, 0.0F);
        tableOffice365.addCell(cell5);

        PdfPCell cell6 = new PdfPCell(new Paragraph(br.readLine(), font2));
        cell6.setBorder(0);
        cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
        tableOffice365.addCell(cell6);

        PdfPCell cell7 = new PdfPCell(new Paragraph(br.readLine(), font2));
        cell7.setBorder(0);
        cell7.setHorizontalAlignment(Element.ALIGN_CENTER);
        tableOffice365.addCell(cell7);

        br.close();
        return tableOffice365;
    }

    private static PdfPTable empty(StudentInf student) throws FileNotFoundException, IOException {
        Font font = new Font();
        Font font2 = new Font();
        try {
            BaseFont baseFont = BaseFont.createFont(RESOURCE.resolve(FONT_LOCATION).toString(), "Identity-H", false);
            font = new Font(baseFont, 12F, 0);

            BaseFont baseFont2 = BaseFont.createFont(RESOURCE.resolve(FONT_LOCATION_BOLD).toString(), "Identity-H", false);
            font2 = new Font(baseFont2, 12F, 0);

        } catch (DocumentException ex) {
            Logger.getLogger(PDFManager.class.getName()).log(Level.SEVERE, "the font is invalid", ex);
        } catch (IOException ex) {
            Logger.getLogger(PDFManager.class.getName()).log(Level.SEVERE, "the font file could not be read", ex);
        }

        PdfPTable tableOffice365 = new PdfPTable(1);
        tableOffice365.setWidthPercentage(100F);
        tableOffice365.getDefaultCell().setBorder(0);
        return tableOffice365;
    }
}
