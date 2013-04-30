package org.icmwind.core.impl;

import org.icmwind.core.FileProcess;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		FileProcess fp = FileProcessImpl.getInstance();
		fp.readFile("C:\\ICMWIND_DATA\\Marpingen1_komplet_NaN.csv");

		
	}

}
