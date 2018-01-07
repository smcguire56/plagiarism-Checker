package ie.gmit.oo;

import java.io.IOException;
import java.util.Scanner;

public class UI {
	private Scanner s= new Scanner(System.in);
	private boolean keepMenuAlive = false;

	public void Show() throws IOException {
		do {
			System.out.println("Please enter Document 1's Name: ");
			String doc1 = s.nextLine();
			System.out.println("Please enter Document 2's Name: ");
			String doc2 = s.nextLine();
			
			Thread t1 = new Thread(new FileParser(doc1 + ".txt"));
			Thread t2 = new Thread(new FileParser(doc2 + ".txt"));
			
			System.out.println("starting threads");
			t1.start();
			t2.start();
		} while (keepMenuAlive);
		
	}

}