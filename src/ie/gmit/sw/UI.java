package ie.gmit.sw;

import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

public class UI {
	private Scanner s= new Scanner(System.in);
	private boolean keepMenuAlive = false;

	public void Show() throws IOException, InterruptedException {
		do {
			System.out.println("Please enter Document 1's Name: ");
			String doc1 = s.nextLine();
			System.out.println("Please enter Document 2's Name: ");
			String doc2 = s.nextLine();
			
			Thread t1;
			if(doc1.toLowerCase().contains("www.") 
					|| doc1.toLowerCase().contains( ".com") 
					|| doc1.toLowerCase().contains( "http")) {
				System.out.println("url");
				t1 = new Thread(new UrlParser(doc1));
			}
			else {
				t1 = new Thread(new FileParser(doc1 + ".txt"));
			}
			Thread t2;
			if(doc2.toLowerCase().contains("www.") 
					|| doc2.toLowerCase().contains( ".com") 
					|| doc2.toLowerCase().contains( "http")) {
				System.out.println("url");
				t2 = new Thread(new UrlParser(doc2));
			}
			else {
				t2 = new Thread(new FileParser(doc2 + ".txt"));
			}			
		
			System.out.println("starting threads");
			t1.start();
			t2.start();
			
			t1.join();
			t2.join();
			
			System.out.println("done");
		} while (keepMenuAlive);
		
	}

}