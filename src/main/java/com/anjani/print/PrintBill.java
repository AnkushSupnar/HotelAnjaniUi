package com.anjani.print;

import com.anjani.data.entity.Bill;
import com.anjani.data.entity.Transaction;
import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.time.format.DateTimeFormatter;

@Component
public class PrintBill {
    private Bill bill;
    Document doc;
    String fileName = "D:\\HotelAnjani\\bill.pdf";
    public static final String fontname = "D:\\HotelAnjani\\kiran.ttf";
    Font f1 = FontFactory.getFont(fontname, BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 30f, Font.BOLD);//, BaseColor.BLACK);
    Font f2 = FontFactory.getFont(fontname, BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 28f, Font.NORMAL, BaseColor.BLUE);
    Font f3 = FontFactory.getFont(fontname, BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 20f, Font.BOLD, BaseColor.BLACK);
    Font f4 = FontFactory.getFont(fontname, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED, 14f, Font.NORMAL, BaseColor.BLACK);
    Font f5 = FontFactory.getFont(fontname, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED, 16f, Font.NORMAL, BaseColor.BLACK);
    Font fe = FontFactory.getFont("Gill Sans Ultra Bold", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED, 18f, Font.BOLD, BaseColor.BLACK);
    Font fdate = FontFactory.getFont("Gill Sans Ultra Bold", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED, 18f, Font.NORMAL, BaseColor.BLACK);
    Font footer = FontFactory.getFont("Gill Sans Ultra Bold", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED, 12f, Font.NORMAL, BaseColor.BLACK);
    Font footer2 = FontFactory.getFont("Gill Sans Ultra Bold", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED, 8f, Font.NORMAL, BaseColor.BLACK);
    public void setBill(Bill bill){
        this.bill = bill;
    }
    public void print(){
        float left = 0;
        float right = 0;
        float top = 20;
        float bottom = 0;
        try {
            createDocument(addData());
            doc.close();
            System.out.println("Print Done");
            new PrintFile().openFile(fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void createDocument(PdfPTable table) throws FileNotFoundException, DocumentException {
        Rectangle pagesize = new Rectangle(table.getTotalWidth(), 150f+table.getTotalHeight());
        doc = new Document(pagesize, 0f, 0f, -1f, 1f);
        OutputStream out = new FileOutputStream(new File(fileName));
        PdfWriter write =  PdfWriter.getInstance(doc, out);
        doc.open();
        addHeader();
        Paragraph p = new Paragraph();
        p.add(table);
        p.setLeading(0.5f);
        doc.add(table);
        //doc.add(p);
    }
    private void addHeader() throws DocumentException {

        PdfPTable border = new PdfPTable(1);
        border.setWidthPercentage(100);
        PdfPTable table = new PdfPTable(1);
        table.setWidthPercentage(100);
        PdfPCell c = new PdfPCell(new Paragraph("ha^Tola AMjanaI",f1));
        c.setHorizontalAlignment(Element.ALIGN_CENTER);
       // c.setColspan(2);
        c.setBorder(PdfPCell.NO_BORDER);
        table.addCell(c);

        c = new PdfPCell(new Paragraph("rahurI raoD, saaona[-,taa.naovaasaa,ija.Ahmadnagar",f5));
        c.setHorizontalAlignment(Element.ALIGN_CENTER);
        //c.setColspan(2);
        c.setBorder(PdfPCell.NO_BORDER);
        table.addCell(c);

        c = new PdfPCell(new Paragraph("maao. 91 8552803030,91 9860419030",f5));
        c.setHorizontalAlignment(Element.ALIGN_CENTER);
        //c.setColspan(2);
        c.setBorder(PdfPCell.BOTTOM);
        table.addCell(c);

        c = new PdfPCell(new Paragraph("ibala",f3));
        c.setHorizontalAlignment(Element.ALIGN_CENTER);
        c.setBorder(PdfPCell.NO_BORDER);
        table.addCell(c);

        PdfPTable tableData = new PdfPTable(2);
        tableData.setWidthPercentage(100);
        c = new PdfPCell(new Paragraph("ibala naM: "+bill.getId(),f4));
        c.setHorizontalAlignment(Element.ALIGN_LEFT);
        c.setBorder(PdfPCell.NO_BORDER);
        tableData.addCell(c);

        c = new PdfPCell(new Paragraph("id. "+bill.getDate().format( DateTimeFormatter.ofPattern("dd:MM:yyyy")),f4));
        c.setHorizontalAlignment(Element.ALIGN_CENTER);
        c.setBorder(PdfPCell.NO_BORDER);
        tableData.addCell(c);



        c = new PdfPCell(tableData);
        c.setHorizontalAlignment(Element.ALIGN_CENTER);
        c.setBorder(PdfPCell.NO_BORDER);
        table.addCell(c);

        c = new PdfPCell(new Paragraph("ga`ahk :"+(bill.getCustomer().getId()!=1?bill.getCustomer().getName():""),f4));
        c.setHorizontalAlignment(Element.ALIGN_LEFT);
        c.setBorder(PdfPCell.BOTTOM);
        table.addCell(c);


        //border.addCell(table);
        c = new PdfPCell(table);
        c.setHorizontalAlignment(Element.ALIGN_RIGHT);
        c.setBorder(PdfPCell.NO_BORDER);
        border.addCell(c);


        table.addCell(c);


        doc.add(border);
    }
    private PdfPTable addData() throws DocumentException {

        PdfPTable table = new PdfPTable(4);
        table.setTotalWidth(new float[]{100, 30,30,50});
        table.setWidthPercentage(100);

        PdfPCell c1 = new PdfPCell(new Paragraph("maalaacao naava ", f4));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        c1.setBorder(PdfPCell.BOTTOM);
        table.addCell(c1);

        c1 = new PdfPCell(new Paragraph("naga", f4));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        c1.setBorder(PdfPCell.BOTTOM);
        table.addCell(c1);

        c1 = new PdfPCell(new Paragraph("Baava", f4));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        c1.setBorder(PdfPCell.BOTTOM);
        table.addCell(c1);

        c1 = new PdfPCell(new Paragraph("r@kma", f4));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        c1.setBorder(PdfPCell.BOTTOM);
        table.addCell(c1);


        int sr = 0;
        for (Transaction tr : bill.getTransactions()) {

            System.out.println(tr.getId());
            c1 = new PdfPCell(new Paragraph(tr.getItemname(), f4));
            c1.setHorizontalAlignment(Element.ALIGN_LEFT);
            c1.setBorder(PdfPCell.NO_BORDER);
            table.addCell(c1);

            c1 = new PdfPCell(new Paragraph("" + tr.getQuantity(), f4));
            c1.setHorizontalAlignment(Element.ALIGN_LEFT);
            c1.setBorder(PdfPCell.NO_BORDER);
            table.addCell(c1);

            c1 = new PdfPCell(new Paragraph("" + tr.getRate(), f4));
            c1.setHorizontalAlignment(Element.ALIGN_LEFT);
            c1.setBorder(PdfPCell.NO_BORDER);
            table.addCell(c1);

            c1 = new PdfPCell(new Paragraph("" + tr.getAmount(), f4));
            c1.setHorizontalAlignment(Element.ALIGN_LEFT);
            c1.setBorder(PdfPCell.NO_BORDER);
            table.addCell(c1);
        }
        PdfPTable tableFooter = new PdfPTable(4);
        tableFooter.setTotalWidth(new float[]{25,25,25,25});
        tableFooter.setWidthPercentage(100);

        c1 = new PdfPCell(new Paragraph("Tobala:", f4));
        c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
        c1.setBorder(PdfPCell.TOP);
        tableFooter.addCell(c1);

        c1 = new PdfPCell(new Paragraph(bill.getTable().getTablename(), fe));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        c1.setBorder(PdfPCell.TOP);
        tableFooter.addCell(c1);

        c1 = new PdfPCell(new Paragraph("ekuNa: ", f4));
        c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
        c1.setBorder(PdfPCell.TOP);
        tableFooter.addCell(c1);

        c1 = new PdfPCell(new Paragraph(""+bill.getNetamount(), f4));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        c1.setBorder(PdfPCell.TOP);
        tableFooter.addCell(c1);

        c1 = new PdfPCell(new Paragraph("vaoTr:", f4));
        c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
        c1.setBorder(PdfPCell.NO_BORDER);
        tableFooter.addCell(c1);

        c1 = new PdfPCell(new Paragraph(""+bill.getWaitor().getNickname(), f4));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        c1.setBorder(PdfPCell.NO_BORDER);
        tableFooter.addCell(c1);

        c1 = new PdfPCell(new Paragraph("jamaa:", f4));
        c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
        c1.setBorder(PdfPCell.NO_BORDER);
        tableFooter.addCell(c1);

        c1 = new PdfPCell(new Paragraph(""+bill.getPaid(), f4));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        c1.setBorder(PdfPCell.NO_BORDER);
        tableFooter.addCell(c1);

        c1 = new PdfPCell(new Paragraph("Thank you Visit Again", footer));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setVerticalAlignment(Element.ALIGN_CENTER);
        c1.setBorder(PdfPCell.TOP);
        c1.setColspan(4);
        tableFooter.addCell(c1);

        c1 = new PdfPCell(new Paragraph("Software Developed By Ankush Supnar (8329394603,9960855742)", footer2));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setVerticalAlignment(Element.ALIGN_CENTER);
        c1.setBorder(PdfPCell.TOP);
        c1.setColspan(4);
        tableFooter.addCell(c1);



        c1 = new PdfPCell(tableFooter);
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        c1.setBorder(PdfPCell.NO_BORDER);
        c1.setColspan(4);
        table.addCell(c1);


        return table;
    }


}
