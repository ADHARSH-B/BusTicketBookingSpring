package com.ticketbooking.main.service;

//import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Collection;

import javax.servlet.http.HttpServletResponse;
import javax.swing.border.Border;

import org.hibernate.annotations.Table;
import org.hibernate.boot.registry.classloading.internal.TcclLookupPrecedence;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.util.Date;
import java.util.Map;

import com.itextpdf.text.Anchor;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Section;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.lowagie.text.Cell;
import com.lowagie.text.pdf.PdfCell;
import com.ticketbooking.main.models.BookingsModel;
import com.ticketbooking.main.models.BusSeatsModel;

@Service
public class PDFGeneratorService {
	String seatsBooked = "";
	String seatType = "";

	public void export(HttpServletResponse response, BookingsModel b) throws DocumentException, IOException {
		Document document = new Document(PageSize.A4);
		PdfWriter.getInstance(document, response.getOutputStream());
		document.open();
		Font font = FontFactory.getFont(FontFactory.HELVETICA, 11);
		Font font2 = FontFactory.getFont(FontFactory.HELVETICA, 12);
		Font font4 = FontFactory.getFont(FontFactory.HELVETICA, 12, Font.UNDERLINE);
		Font font3 = FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD);
		float[] pointColumnWidths = { 100F, 100F };
		PdfPTable headerTable = new PdfPTable(pointColumnWidths);
		System.out.println(b.getBookingDate().toString().split("T")[0]);
		PdfPCell cell = new PdfPCell(new PdfPCell(new Phrase(
				String.format("MyBus.com\n" + "\nBookingDate:%s", b.getBookingDate().toString().split("T")[0]), font)));
		cell.setBorder(0);
		headerTable.addCell(cell);
		PdfPCell cell1 = new PdfPCell(new PdfPCell(new Phrase("MyBusHelpline\n\n6382518063", font2)));

		cell1.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell1.setMinimumHeight(30f);
		cell1.setBorder(0);
		headerTable.addCell(cell1);
		PdfPCell cell2 = new PdfPCell(new PdfPCell(new Phrase("MyBus", font)));
		headerTable.addCell(cell2);
		document.add(headerTable);

		float[] pointColumnWidth1 = { 200F, 200F, 200F };
		PdfPTable detailsTable = new PdfPTable(pointColumnWidth1);

		detailsTable.setSpacingBefore(14);
		PdfPCell dcell = new PdfPCell(new PdfPCell(new Phrase(String.format("%s to %s",
				b.getBusDetails().getBoardingPoint(), b.getBusDetails().getdestinationPoint()), font)));
		dcell.setPaddingBottom(12);
		dcell.setPaddingTop(12);
		dcell.setBorderWidth(0);
		dcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		dcell.setBorderWidthTop(2);
		dcell.setBorderColor(BaseColor.ORANGE);
		dcell.setMinimumHeight(4);
		dcell.setBackgroundColor(new BaseColor(255, 229, 180));
		dcell.setBorderWidthBottom(2);
		detailsTable.addCell(dcell);
		PdfPCell dcell1 = new PdfPCell(new PdfPCell(new Phrase(b.getBusDetails().getDepartureDate().toString(), font)));
		dcell1.setBorderWidth(0);
		dcell1.setPaddingBottom(12);
		dcell1.setHorizontalAlignment(Element.ALIGN_CENTER);
		dcell1.setBorderColor(BaseColor.ORANGE);
		dcell1.setBackgroundColor(new BaseColor(255, 229, 180));
		dcell1.setPaddingTop(12);
		dcell1.setMinimumHeight(4);
		dcell1.setBorderWidthTop(2);
		dcell1.setBorderWidthBottom(2);
		detailsTable.addCell(dcell1);
		PdfPCell dcell2 = new PdfPCell(
				new PdfPCell(new Phrase(String.format("%s Travels", b.getBusDetails().getBusName()), font)));
		dcell2.setBorderWidth(0);
		dcell2.setBorderColor(BaseColor.ORANGE);
		dcell2.setHorizontalAlignment(Element.ALIGN_CENTER);
		dcell2.setBackgroundColor(new BaseColor(255, 229, 180));
		dcell2.setPaddingBottom(12);
		dcell2.setPaddingTop(12);
		dcell2.setBorderWidthTop(2);
		dcell2.setBorderWidthBottom(2);
		dcell2.setMinimumHeight(4);
		detailsTable.addCell(dcell2);
		detailsTable.setSpacingAfter(40);

		document.add(detailsTable);

//		for(int i=0;i<10;i++) {
//			
//			Chunk line = new Chunk("\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0");
//			
//			line.setUnderline(1,1);
//			document.add(line);
//		}
		float[] pointColumnWidth2 = { 200F, 200F, 200F, 200f, 200f };
		PdfPTable detailsTable2 = new PdfPTable(pointColumnWidth2);

		PdfPCell dcell4 = new PdfPCell(new PdfPCell(new Phrase("Passenger", font3)));
		dcell4.setBorderWidth(2);
		detailsTable2.addCell(dcell4);

		PdfPCell dcell5 = new PdfPCell(new PdfPCell(new Phrase("Ticket #", font3)));
		dcell5.setBorderWidth(2);
		dcell5.setHorizontalAlignment(Element.ALIGN_CENTER);
		detailsTable2.addCell(dcell5);
		PdfPCell dcell6 = new PdfPCell(new PdfPCell(new Phrase("SeatNumber(s)", font3)));
		dcell6.setBorderWidth(2);
		dcell6.setHorizontalAlignment(Element.ALIGN_CENTER);
		detailsTable2.addCell(dcell6);
		PdfPCell dcell7 = new PdfPCell(new PdfPCell(new Phrase("PNR #", font3)));
		dcell7.setBorderWidth(2);
		dcell7.setHorizontalAlignment(Element.ALIGN_CENTER);
		detailsTable2.addCell(dcell7);
		PdfPCell dcell8 = new PdfPCell(new PdfPCell(new Phrase("Trip#", font3)));
		dcell8.setHorizontalAlignment(Element.ALIGN_CENTER);
		dcell8.setBorderWidth(2);
		detailsTable2.addCell(dcell8);
		document.add(detailsTable2);

		PdfPTable detailsTable3 = new PdfPTable(pointColumnWidth2);

		PdfPCell dcell9 = new PdfPCell(new PdfPCell(new Phrase(b.getUserDetails().getName(), font)));
//		dcell4.setBorderWidth(0);
		detailsTable3.addCell(dcell9);

		PdfPCell dcell10 = new PdfPCell(new PdfPCell(new Phrase(b.getId().toString(), font)));
		detailsTable3.addCell(dcell10);

		b.getBookedSeats().forEach(c -> {
			seatsBooked += c.getId() + ",";
			seatType = c.getSeatType();
		});
		System.out.println(seatsBooked);
		PdfPCell dcell11 = new PdfPCell(new PdfPCell(new Phrase(seatsBooked, font)));
		this.seatsBooked = "";
		detailsTable3.addCell(dcell11);
		PdfPCell dcell12 = new PdfPCell(new PdfPCell(new Phrase("12129091211", font)));
		detailsTable3.addCell(dcell12);
		PdfPCell dcell13 = new PdfPCell(new PdfPCell(new Phrase("12198333892", font)));
		detailsTable3.addCell(dcell13);
		detailsTable3.setSpacingAfter(40);
		document.add(detailsTable3);

		PdfPTable busdetails = new PdfPTable(pointColumnWidth2);
		PdfPCell cellone = new PdfPCell(new PdfPCell(new Phrase("BusName", font3)));
		cellone.setBorderWidth(2);
		cellone.setHorizontalAlignment(Element.ALIGN_CENTER);
		busdetails.addCell(cellone);
		PdfPCell cellone1 = new PdfPCell(new PdfPCell(new Phrase("BusType", font3)));
		cellone1.setHorizontalAlignment(Element.ALIGN_CENTER);
		cellone1.setBorderWidth(2);
		busdetails.addCell(cellone1);
		PdfPCell cellone2 = new PdfPCell(new PdfPCell(new Phrase("SeatType", font3)));
		cellone2.setHorizontalAlignment(Element.ALIGN_CENTER);
		cellone2.setBorderWidth(2);
		busdetails.addCell(cellone2);
		PdfPCell cellone3 = new PdfPCell(new PdfPCell(new Phrase("DepartureTime", font3)));
		cellone3.setBorderWidth(2);
		cellone3.setHorizontalAlignment(Element.ALIGN_CENTER);
		busdetails.addCell(cellone3);
		PdfPCell cellone4 = new PdfPCell(new PdfPCell(new Phrase("ArrivalTime", font3)));
		cellone4.setHorizontalAlignment(Element.ALIGN_CENTER);
		cellone4.setBorderWidth(2);
		busdetails.addCell(cellone4);
		document.add(busdetails);

		PdfPTable busdetails1 = new PdfPTable(pointColumnWidth2);
		PdfPCell celloned1 = new PdfPCell(new PdfPCell(new Phrase(b.getBusDetails().getBusName(), font)));
		cellone.setHorizontalAlignment(Element.ALIGN_CENTER);
		busdetails1.addCell(celloned1);
		PdfPCell cellone1d2 = new PdfPCell(new PdfPCell(new Phrase(b.getBusDetails().getBusType(), font)));
		cellone1.setHorizontalAlignment(Element.ALIGN_CENTER);
		busdetails1.addCell(cellone1d2);
		PdfPCell celloned3 = new PdfPCell(new PdfPCell(new Phrase(seatType, font)));
		this.seatType = "";
		cellone2.setHorizontalAlignment(Element.ALIGN_CENTER);
		busdetails1.addCell(celloned3);
		PdfPCell celloned4 = new PdfPCell(
				new PdfPCell(new Phrase(b.getBusDetails().getDepartureTime().toString(), font)));
		cellone3.setHorizontalAlignment(Element.ALIGN_CENTER);
		busdetails1.addCell(celloned4);
		PdfPCell celloned5 = new PdfPCell(
				new PdfPCell(new Phrase(b.getBusDetails().getArrivalTime().toString(), font)));
		cellone4.setHorizontalAlignment(Element.ALIGN_CENTER);
		busdetails1.addCell(celloned5);
		busdetails1.setSpacingAfter(40);
		document.add(busdetails1);

		Paragraph p1 = new Paragraph("PaymentDetails", font4);
		p1.setAlignment(Element.ALIGN_CENTER);
		document.add(p1);

		float[] pointColumnWidth9 = { 200F, 200F };
		PdfPTable paymentTable = new PdfPTable(pointColumnWidth9);
		paymentTable.setSpacingBefore(20);
		PdfPCell cellonedp1 = new PdfPCell(new PdfPCell(new Phrase("TotalFare", font3)));
		cellonedp1.setBorderWidth(2);
		cellonedp1.setHorizontalAlignment(Element.ALIGN_CENTER);
		paymentTable.addCell(cellonedp1);
		PdfPCell cellone1p2 = new PdfPCell(new PdfPCell(new Phrase("Payment Status", font3)));
		cellone1p2.setBorderWidth(2);
		cellone1p2.setHorizontalAlignment(Element.ALIGN_CENTER);
		paymentTable.addCell(cellone1p2);
		document.add(paymentTable);

		PdfPTable paymentTabledetails = new PdfPTable(pointColumnWidth9);
		PdfPCell cellonedp1s1 = new PdfPCell(
				new PdfPCell(new Phrase(String.format("Rs %s/-", b.getTotalCost()), font3)));
		cellonedp1s1.setHorizontalAlignment(Element.ALIGN_CENTER);
		paymentTabledetails.addCell(cellonedp1s1);
		PdfPCell cellone1p2s2 = new PdfPCell(new PdfPCell(new Phrase("Paid", font3)));
		cellone1p2s2.setHorizontalAlignment(Element.ALIGN_CENTER);
		paymentTabledetails.addCell(cellone1p2s2);

		document.add(paymentTabledetails);

		document.close();

	}


}
