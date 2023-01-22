package com.terminato.moneymanager.core;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class ExcelWriter {

    private User user;
    public ExcelWriter(User _user, OutputStream outputStream) {
        user = _user;
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Bevétel");

        /*
            egy munkafüzet és azon belül egy lap létrehozása bevétel néven
        */

        HSSFRow row = sheet.createRow(0);
        row.createCell(0).setCellValue("Bevétel típusa");
        row.createCell(1).setCellValue("Bevétel leírása");
        row.createCell(2).setCellValue("Dátum");
        row.createCell(3).setCellValue("Fizetési módszer");
        row.createCell(4).setCellValue("Valuta");
        row.createCell(5).setCellValue("Összeg");

        /*
            táblázat fejléceinek létrehozása
        */

        addCell(user.getIncomes(), row, sheet);

        /*
            adatok hozzáadása a táblázathoz
        */

        sheet = workbook.createSheet("Kiadás");

        /*
            új lap léterohzása Kiadás néven
        */

        row = sheet.createRow(0);
        row.createCell(0).setCellValue("Kiadás típusa");
        row.createCell(1).setCellValue("Kiadás leírása");
        row.createCell(2).setCellValue("Dátum");
        row.createCell(3).setCellValue("Fizetési módszer");
        row.createCell(4).setCellValue("Valuta");
        row.createCell(5).setCellValue("Összeg");

        /*
            táblázat fejléceinek létrehozása
        */

        addCell(user.getExpenditures(), row, sheet);

         /*
            adatok hozzáadása a táblázathoz
        */

        sheet = workbook.createSheet("Számlák");

        /*
            új lap léterohzása Számlák néven
        */

        row = sheet.createRow(0);

        row.createCell(0).setCellValue("Típus");
        row.createCell(1).setCellValue("Valuta");
        row.createCell(2).setCellValue("Összeg");

        /*
            táblázat fejléceinek létrehozása
        */

        for(int i = 0; i < user.getMoneyStore().size(); i++) {
            row = sheet.createRow(i+1);
            row.createCell(0).setCellValue(user.getMoneyStore().get(i).getM_name());
            row.createCell(1).setCellValue(user.getMoneyStore().get(i).getCurrency());
            row.createCell(2).setCellValue(user.getMoneyStore().get(i).getValue());
        }

         /*
            adatok hozzáadása a táblázathoz
        */

        write(workbook, outputStream);
        try {
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Cellát lehet hozzáadni (kód rövidítés célja van)
     @param x : megkapja azt a listát amit be kell neki járnia
     @param row : az adott sor ahova be kell enki szúrni az aadatot
     @param sheet : az adott lap ahova a sort kell beszúrnia
     */
    private void addCell(ArrayList<MoneyEvent> x, HSSFRow row, HSSFSheet sheet) {
        for(int i = 0; i < x.size(); i++) {
            row = sheet.createRow(i+1);
            row.createCell(0).setCellValue(x.get(i).getType());
            row.createCell(1).setCellValue(x.get(i).getDesc());
            row.createCell(2).setCellValue(x.get(i).getDate());
            row.createCell(3).setCellValue(x.get(i).getPaymentMethod());
            row.createCell(4).setCellValue(x.get(i).getCurrency());
            row.createCell(5).setCellValue(x.get(i).getValue());
        }
    }

    /**
     * Kiírja az Excel fájlt.
     @param w : a WorkBook amit kell írnia
     @param outputStream : a folyamat ami a kiírást végzi
     */
    private void write(HSSFWorkbook w, OutputStream outputStream) {
        try {
            w.write(outputStream);
            outputStream.close();
            w.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}