package com.anjani.print;

import com.anjani.data.entity.Kirana;
import com.anjani.data.entity.KiranaTransaction;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.time.format.DateTimeFormatter;

@Component
public class PrintKiranaQuotation {
    private  Kirana kirana;
    Document doc;
    String fileName = "D:\\HotelAnjani\\bill.pdf";
    public static final String fontname = "D:\\Shopee\\kiran.ttf";
    Font f1 = FontFactory.getFont(fontname, BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 30f, Font.BOLD);//, BaseColor.BLACK);
    Font f2 = FontFactory.getFont(fontname, BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 28f, Font.NORMAL, BaseColor.BLUE);
    Font f3 = FontFactory.getFont(fontname, BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 20f, Font.BOLD, BaseColor.BLACK);
    Font f4 = FontFactory.getFont(fontname, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED, 20f, Font.NORMAL, BaseColor.BLACK);
    Font f5 = FontFactory.getFont(fontname, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED, 18f, Font.BOLD, BaseColor.BLACK);

    public PrintKiranaQuotation(){
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
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void setKirana(Kirana kirana){
        this.kirana = kirana;
    }
    private void createDocument(PdfPTable table) throws FileNotFoundException, DocumentException {
        Rectangle pagesize = new Rectangle(230f, 10000f + table.getTotalHeight());
        doc = new Document(pagesize, -1f, 0f, 15f, 180f);
        doc.setPageSize(PageSize.A4);
        OutputStream out = new FileOutputStream(new File(fileName));
        PdfWriter write =  PdfWriter.getInstance(doc, out);
        HeaderFooterPageEvent event = new HeaderFooterPageEvent();
        write.setPageEvent(event);
        doc.open();
        addHeader();
        Paragraph p = new Paragraph();
        //p.setLeading(25);
        // p.setIndentationLeft(1);
        p.add(table);
        doc.add(p);
       // doc.add(table);

    }
    private void addHeader() throws DocumentException {

        PdfPTable border = new PdfPTable(1);
        PdfPTable table = new PdfPTable(2);
        PdfPCell c = new PdfPCell(new Paragraph("ha^Tola AMjanaI",f1));
        c.setHorizontalAlignment(Element.ALIGN_CENTER);
        c.setColspan(2);
        c.setBorder(PdfPCell.NO_BORDER);
        table.addCell(c);

        c = new PdfPCell(new Paragraph("ikranaa yaadI",f2));
        c.setHorizontalAlignment(Element.ALIGN_CENTER);
        c.setColspan(2);
        c.setBorder(PdfPCell.BOTTOM);
        table.addCell(c);

        c = new PdfPCell(new Paragraph("ibala naM."+kirana.getId(),f3));
        c.setHorizontalAlignment(Element.ALIGN_LEFT);
        c.setBorder(PdfPCell.BOTTOM);
        table.addCell(c);

        c = new PdfPCell(new Paragraph("idnaaMk :"+kirana.getDate().format( DateTimeFormatter.ofPattern("dd:MM:yyyy")),f3));
        c.setHorizontalAlignment(Element.ALIGN_RIGHT);
        c.setBorder(PdfPCell.BOTTOM);
        table.addCell(c);

        c = new PdfPCell(new Paragraph("dukanaacao naava : "+kirana.getParty().getName(),f3));
        c.setHorizontalAlignment(Element.ALIGN_LEFT);
        c.setColspan(2);
        c.setBorder(PdfPCell.NO_BORDER);
        table.addCell(c);
        border.addCell(table);
        doc.add(border);
    }
    private PdfPTable addData() throws DocumentException {

        PdfPTable table = new PdfPTable(6);
        table.setTotalWidth(new float[]{35, 140, 60, 50, 70, 60});

        PdfPCell c1 = new PdfPCell(new Paragraph("k` ", f3));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(c1);

        c1 = new PdfPCell(new Paragraph("maalaacao naava", f3));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(c1);

        c1 = new PdfPCell(new Paragraph("yauinaT", f3));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(c1);

        c1 = new PdfPCell(new Paragraph("naga", f3));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(c1);

        c1 = new PdfPCell(new Paragraph("dr", f3));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(c1);

        c1 = new PdfPCell(new Paragraph("ikMmata", f3));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(c1);
        int sr=0;
        for(KiranaTransaction tr:kirana.getKiranaTransactions()){

            c1 = new PdfPCell(new Paragraph(""+(++sr), f4));
            c1.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(c1);

            c1 = new PdfPCell(new Paragraph(tr.getItemname(), f4));
            c1.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(c1);

            c1 = new PdfPCell(new Paragraph(tr.getUnit(), f4));
            c1.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(c1);

            c1 = new PdfPCell(new Paragraph(""+((tr.getQuantity().equals(0.0f))?"":tr.getQuantity()), f4));
            c1.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(c1);

            c1 = new PdfPCell(new Paragraph(""+((tr.getRate().equals(0.0f))?"":tr.getRate()), f4));
            c1.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(c1);

            c1 = new PdfPCell(new Paragraph(""+((tr.getAmount().equals(0.0f))?"":tr.getAmount()), f4));
            c1.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(c1);

        }

        c1 = new PdfPCell(new Paragraph("", f4));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        c1.setColspan(4);
        table.addCell(c1);

        c1 = new PdfPCell(new Paragraph("ekuNa", f3));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(c1);

        c1 = new PdfPCell(new Paragraph(""+((kirana.getNettotal().equals(0.0f))?"":kirana.getNettotal()), f4));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(c1);

        //
        c1 = new PdfPCell(new Paragraph("", f4));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        c1.setColspan(4);
        table.addCell(c1);

        c1 = new PdfPCell(new Paragraph("vaahtauk Kaca-", f3));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(c1);

        c1 = new PdfPCell(new Paragraph(""+((kirana.getTransaport().equals(0.0f))?"":kirana.getTransaport()), f3));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(c1);

        c1 = new PdfPCell(new Paragraph("", f3));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        c1.setColspan(4);
        table.addCell(c1);

        c1 = new PdfPCell(new Paragraph("[tar Kaca-", f3));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(c1);

        c1 = new PdfPCell(new Paragraph(""+((kirana.getOther().equals(0.0f))?"":kirana.getOther()), f3));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(c1);

        c1 = new PdfPCell(new Paragraph("", f3));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        c1.setColspan(4);
        table.addCell(c1);

        c1 = new PdfPCell(new Paragraph("sauT", f3));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(c1);

        c1 = new PdfPCell(new Paragraph(""+((kirana.getDiscount().equals(0.0f))?"":kirana.getDiscount()), f3));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(c1);


        c1 = new PdfPCell(new Paragraph("", f3));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        c1.setColspan(4);
        table.addCell(c1);

        c1 = new PdfPCell(new Paragraph("ekuNa r@kma", f3));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(c1);

        c1 = new PdfPCell(new Paragraph(""+((kirana.getGrandtotal().equals(0.0f))?"":kirana.getGrandtotal()), f4));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(c1);


        return table;
    }
}
