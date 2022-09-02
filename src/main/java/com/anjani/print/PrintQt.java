package com.anjani.print;

import com.anjani.data.entity.TempTransaction;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

@Component
public class PrintQt {
    private List<TempTransaction>list;
    Document doc;
    String fileName = "D:\\HotelAnjani\\qt.pdf";
    public static final String fontname = "D:\\HotelAnjani\\kiran.ttf";
    Font f1 = FontFactory.getFont(fontname, BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 30f, Font.BOLD);//, BaseColor.BLACK);
    Font f2 = FontFactory.getFont(fontname, BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 28f, Font.NORMAL, BaseColor.BLUE);
    Font f3 = FontFactory.getFont(fontname, BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 20f, Font.BOLD, BaseColor.BLACK);
    Font f4 = FontFactory.getFont(fontname, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED, 20f, Font.NORMAL, BaseColor.BLACK);
    Font f5 = FontFactory.getFont(fontname, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED, 18f, Font.BOLD, BaseColor.BLACK);
    Font fe = FontFactory.getFont("Gill Sans Ultra Bold", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED, 18f, Font.BOLD, BaseColor.BLACK);
    public PrintQt(){}
    public void printQt(List<TempTransaction> list){
        this.list = list;
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
//        Rectangle pagesize = new Rectangle(130f, 10000f + table.getTotalHeight());
//        doc = new Document(pagesize, -1f, 0f, 15f, 180f);
        Rectangle pagesize = new Rectangle(table.getTotalWidth(), 150f+table.getTotalHeight());

         doc = new Document(pagesize, 1f, 1f, 2f, 1f);
       // doc.setPageSize(pagesize);
        OutputStream out = new FileOutputStream(new File(fileName));
        PdfWriter write =  PdfWriter.getInstance(doc, out);
      //  HeaderFooterPageEvent event = new HeaderFooterPageEvent();
        //write.setPageEvent(event);
        doc.open();
        addHeader();
        Paragraph p = new Paragraph();
        //p.setLeading(25);
        // p.setIndentationLeft(1);
        p.add(table);
        p.setLeading(0.5f);
        doc.add(p);
        // doc.add(table);

    }
    private void addHeader() throws DocumentException {

        PdfPTable border = new PdfPTable(1);
        PdfPTable table = new PdfPTable(1);
        PdfPCell c = new PdfPCell(new Paragraph("ha^Tola AMjanaI",f1));
        c.setHorizontalAlignment(Element.ALIGN_CENTER);
        c.setColspan(2);
        c.setBorder(PdfPCell.NO_BORDER);
        table.addCell(c);

        c = new PdfPCell(new Paragraph("ikcana kaoToSana",f2));
        c.setHorizontalAlignment(Element.ALIGN_CENTER);
        c.setColspan(2);
        c.setBorder(PdfPCell.BOTTOM);
        table.addCell(c);

        c = new PdfPCell(new Paragraph("vaoTrcao naava: "+ list.get(0).getEmployee().getNickname(),f3));
        c.setHorizontalAlignment(Element.ALIGN_LEFT);
        c.setBorder(PdfPCell.NO_BORDER);
        table.addCell(c);

        c = new PdfPCell(new Paragraph("Table :"+list.get(0).getTableMaster().getTablename(),fe));
        c.setHorizontalAlignment(Element.ALIGN_LEFT);
        c.setBorder(PdfPCell.BOTTOM);
        table.addCell(c);


        //border.addCell(table);
        c = new PdfPCell(table);
        c.setHorizontalAlignment(Element.ALIGN_RIGHT);
        c.setBorder(PdfPCell.NO_BORDER);
        border.addCell(c);

        doc.add(border);
    }
    private PdfPTable addData() throws DocumentException {

        PdfPTable table = new PdfPTable(2);
        table.setTotalWidth(new float[]{140, 35});

        PdfPCell c1 = new PdfPCell(new Paragraph("maalaacao naava ", f3));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        c1.setBorder(PdfPCell.BOTTOM);
        table.addCell(c1);

        c1 = new PdfPCell(new Paragraph("naga", f3));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        c1.setBorder(PdfPCell.BOTTOM);
        table.addCell(c1);



        int sr = 0;
        for (TempTransaction tr : list) {


            c1 = new PdfPCell(new Paragraph(tr.getItemname(), f4));
            c1.setHorizontalAlignment(Element.ALIGN_LEFT);
            c1.setBorder(PdfPCell.NO_BORDER);
            table.addCell(c1);

            c1 = new PdfPCell(new Paragraph("" + tr.getPrintqty(), f4));
            c1.setHorizontalAlignment(Element.ALIGN_LEFT);
            c1.setBorder(PdfPCell.NO_BORDER);
            table.addCell(c1);

        }
        return table;
    }


    }
