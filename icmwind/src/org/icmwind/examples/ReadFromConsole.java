package org.icmwind.examples;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class ReadFromConsole {

	public static void main(String[] args) throws IOException {
		BufferedReader read = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("Type something: ");
		String line = read.readLine();
		System.out.println(line);

	}
	

	
}

