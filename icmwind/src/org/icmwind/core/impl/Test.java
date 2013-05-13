package org.icmwind.core.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.icmwind.core.FileProcess;

public class Test {

	/**
	 * @param args
	 * @throws ParseException 
	 */
	public static void main(String[] args) throws ParseException {
//		FileProcess fp = FileProcessImpl.getInstance();
//		fp.readFile("C:\\ICMWIND_DATA\\Marpingen1_komplet_NaN.csv");
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
		Date test = sdf.parse("06.09.2008 20:00:00");
		
		Calendar c = Calendar.getInstance();
		c.setTime(test);
		c.add(Calendar.DATE, 1);
		
		test = c.getTime();
		
		System.out.println(test);
		
//		while(fp.existsRecord()) {
//			Date temp = sdf.parse(fp.readRecord("Zeit"));
//			if(temp.before(test)) {
//				System.out.println(temp.toString());
//			}
//		}
			
	
		
		
//		int i = 0;
//		
//		
//		while(fp.existsRecord()) {
////			System.out.println("Reached");
//			i++;
//		}
		
//		System.out.println(fp.getNumberOfObservations() + " =? " + i);

/*		
//		System.out.println(fp.getFileName() + " " + fp.getNumberOfObservations() + " " + fp.getBeginDate().toString() + "-" + fp.getEndDate().toString()  );
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
		Date date = sdf.parse("04.09.2008 13:10:21");
		System.out.println(date);
		
		Date pdate = new SimpleDateFormat("EEE MMM dd hh:mm:ss zzz yyyy").parse(date.toString());
		System.out.println(pdate);
		
//		SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy"); 
//		System.out.println(format.format(date));
//		
//		dow mon dd hh:mm:ss zzz yyyy
*/		
		
	}

}
