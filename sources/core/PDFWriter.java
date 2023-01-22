package com.terminato.moneymanager.core;

import java.io.OutputStream;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class PDFWriter {
    private Document document = new Document(PageSize.A3, 10, 10, 5 ,0);
    private User user;
    private BaseFont basefont = BaseFont.createFont("assets/times.ttf", BaseFont.CP1250, BaseFont.EMBEDDED);
    private BaseFont basefont2 = BaseFont.createFont("assets/courier.ttf", BaseFont.CP1250, BaseFont.EMBEDDED);
    private Font alap = new Font(basefont, 14, Font.BOLD | Font.UNDERLINE, BaseColor.BLACK);
    private Font ftable = new Font(basefont, 14, Font.BOLD | Font.UNDERLINE, BaseColor.RED);
    private Font fdata = new Font(basefont, 14, Font.NORMAL, BaseColor.BLACK);
    private PdfPCell cell = null;
    private float osszbev = 0f, osszki = 0f;

    public PDFWriter(User _user, OutputStream outputStream) throws Exception{
        user = _user;
        //PdfWriter.getInstance(document, new FileOutputStream(user.getUserName() + ".pdf"));

        PdfWriter.getInstance(document, outputStream);
        document.open();

        ////////////////////////dokumentum fejléc létrehozása////////////////////////
        document.add(addPara(user.getUserDisplayName() + " kimutatása", new Font(basefont, 35, Font.BOLD | Font.UNDERLINE, BaseColor.RED), Paragraph.ALIGN_CENTER));
        ////////////////////////////////////////////////////////////////////////////
        document.add(spacer(25f));
        ////////////////////////alap adatok név, email stb./////////////////////////////
        document.add(addPara("Név: " + user.getUserDisplayName(), alap, Paragraph.ALIGN_LEFT));
        document.add(addPara("E-mail: " + user.getUserEmail(), alap, Paragraph.ALIGN_LEFT));
        document.add(addPara("Összes bevétel száma: " + user.getIncomes().size() + "DB", alap, Paragraph.ALIGN_LEFT));
        document.add(addPara("Összes kiadás száma: " + user.getExpenditures().size() + "DB", alap, Paragraph.ALIGN_LEFT));


        for(int i = 0; i < user.getIncomes().size(); i++) { osszbev += user.getIncomes().get(i).getValue(); }
        document.add(addPara("Összes bevétel összege: " + osszbev, alap, Paragraph.ALIGN_LEFT));
        for(int i = 0; i < user.getExpenditures().size(); i++) { osszki += user.getExpenditures().get(i).getValue(); }
        document.add(addPara("Összes kiadás összege: " + osszki, alap, Paragraph.ALIGN_LEFT));

        document.add(spacer(50f));

        ///////////////////////////////bevételek//////////////////////////////////////
        document.add(addPara("Bevételek: ", alap, Paragraph.ALIGN_LEFT));

        PdfPTable table = createTable(6);
        table.setSpacingAfter(25f);
        table.setWidthPercentage(100);
        table.addCell(addCell("Bevétel típusa", ftable, Paragraph.ALIGN_CENTER));
        table.addCell(addCell("Bevétel leírása", ftable, Paragraph.ALIGN_LEFT));
        table.addCell(addCell("Dátum", ftable, Paragraph.ALIGN_LEFT));
        table.addCell(addCell("Fizetési módszer", ftable, Paragraph.ALIGN_LEFT));
        table.addCell(addCell("Valuta", ftable, Paragraph.ALIGN_LEFT));
        table.addCell(addCell("Összeg", ftable, Paragraph.ALIGN_LEFT));


        for(int i = 0; i < user.getIncomes().size(); i++) {
            table.addCell(addCell(user.getIncomes().get(i).getType(), fdata, Paragraph.ALIGN_CENTER));
            table.addCell(addCell(user.getIncomes().get(i).getDesc(), fdata, Paragraph.ALIGN_CENTER));
            table.addCell(addCell(user.getIncomes().get(i).getDate(), fdata, Paragraph.ALIGN_CENTER));
            table.addCell(addCell(user.getIncomes().get(i).getPaymentMethod(), fdata, Paragraph.ALIGN_CENTER));
            table.addCell(addCell(user.getIncomes().get(i).getCurrency(), fdata, Paragraph.ALIGN_CENTER));
            table.addCell(addCell(user.getIncomes().get(i).getValue(), fdata, Paragraph.ALIGN_CENTER));
        }
        table.addCell(addCell("Összesen: ", ftable, Paragraph.ALIGN_CENTER));
        PdfPCell osszeg = new PdfPCell(addPara(osszbev + " Ft", fdata, Paragraph.ALIGN_CENTER));
        osszeg.setVerticalAlignment(Element.ALIGN_RIGHT);
        osszeg.setColspan(6);
        osszeg.setVerticalAlignment(Element.ALIGN_MIDDLE);
        osszeg.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(osszeg);

        document.add(table);
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////
        document.add(spacer(75f));
        ///////////////////////////////////////kiadások///////////////////////////////////////////////////////////////
        document.add(addPara("Kiadások:", alap, Paragraph.ALIGN_LEFT));
        table = createTable(6);
        table.setSpacingAfter(25f);
        table.setWidthPercentage(100);
        table.addCell(addCell("Bevétel típusa", ftable, Paragraph.ALIGN_CENTER));
        table.addCell(addCell("Bevétel leírása", ftable, Paragraph.ALIGN_LEFT));
        table.addCell(addCell("Dátum", ftable, Paragraph.ALIGN_LEFT));
        table.addCell(addCell("Fizetési módszer", ftable, Paragraph.ALIGN_LEFT));
        table.addCell(addCell("Valuta", ftable, Paragraph.ALIGN_LEFT));
        table.addCell(addCell("Összeg", ftable, Paragraph.ALIGN_LEFT));


        for(int i = 0; i < user.getExpenditures().size(); i++) {
            table.addCell(addCell(user.getExpenditures().get(i).getType(), fdata, Paragraph.ALIGN_CENTER));
            table.addCell(addCell(user.getExpenditures().get(i).getDesc(), fdata, Paragraph.ALIGN_CENTER));
            table.addCell(addCell(user.getExpenditures().get(i).getDate(), fdata, Paragraph.ALIGN_CENTER));
            table.addCell(addCell(user.getExpenditures().get(i).getPaymentMethod(), fdata, Paragraph.ALIGN_CENTER));
            table.addCell(addCell(user.getExpenditures().get(i).getCurrency(), fdata, Paragraph.ALIGN_CENTER));
            table.addCell(addCell(user.getExpenditures().get(i).getValue(), fdata, Paragraph.ALIGN_CENTER));
        }
        table.addCell(addCell("Összesen: ", ftable, Paragraph.ALIGN_CENTER));

        osszeg = new PdfPCell(addPara(osszki + " Ft", fdata, Paragraph.ALIGN_CENTER));
        osszeg.setVerticalAlignment(Element.ALIGN_RIGHT);
        osszeg.setColspan(6);
        osszeg.setVerticalAlignment(Element.ALIGN_MIDDLE);
        osszeg.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(osszeg);

        document.add(table);
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        document.add(spacer(25f));
        /////////////////////////////////////számlák///////////////////////////////////////////////////////////////////////
        document.add(addPara("Számlák: ", alap, Paragraph.ALIGN_LEFT));
        table = createTable(3);

        table.addCell(addCell("Típus", ftable, Paragraph.ALIGN_CENTER));
        table.addCell(addCell("Valuta", ftable, Paragraph.ALIGN_CENTER));
        table.addCell(addCell("Összeg", ftable, Paragraph.ALIGN_CENTER));
        table.setWidthPercentage(100);

        for(int i = 0; i < user.getMoneyStore().size(); i++) {
            table.addCell(addCell(user.getMoneyStore().get(i).getM_name(), fdata, Paragraph.ALIGN_CENTER));
            table.addCell(addCell(user.getMoneyStore().get(i).getCurrency(), fdata, Paragraph.ALIGN_CENTER));
            table.addCell(addCell(user.getMoneyStore().get(i).getValue(), fdata, Paragraph.ALIGN_CENTER));
        }

        osszeg = new PdfPCell(addPara(user.getSumOfCash() + " Ft", fdata, Paragraph.ALIGN_CENTER));
        osszeg.setVerticalAlignment(Element.ALIGN_RIGHT);
        osszeg.setColspan(3);
        osszeg.setVerticalAlignment(Element.ALIGN_MIDDLE);
        osszeg.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(addCell("Összesen: ", ftable, Paragraph.ALIGN_CENTER));
        table.addCell(osszeg);


        document.add(table);
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        document.close();
    }

    private Paragraph addPara(String text, Font font, int aligment) {
        Paragraph p = new Paragraph(text, font);
        p.setAlignment(aligment);
        p.setSpacingAfter(10f);
        return p;
    }

    private Paragraph addPara(float text, Font font, int aligment) {
        Paragraph p = new Paragraph(Float.toString(text), font);
        p.setAlignment(aligment);
        p.setSpacingAfter(10f);
        return p;
    }

    private Paragraph spacer(float space) {
        Paragraph p = new Paragraph("");
        p.setSpacingAfter(space);
        return p;
    }

    private PdfPCell addCell(String text, Font font, int aligment) {
        PdfPCell p = new PdfPCell(addPara(text, font, aligment));
        p.setVerticalAlignment(Element.ALIGN_MIDDLE);
        p.setHorizontalAlignment(Element.ALIGN_CENTER);
        p.setFixedHeight(50f);
        return p;
    }

    private PdfPCell addCell(float text, Font font, int aligment) {
        PdfPCell p = new PdfPCell(addPara(text, font, aligment));
        p.setVerticalAlignment(Element.ALIGN_MIDDLE);
        p.setHorizontalAlignment(Element.ALIGN_CENTER);
        p.setFixedHeight(50f);
        return p;
    }

    private PdfPTable createTable(int col) {
        PdfPTable p = new PdfPTable(col);
        return p;
    }

}