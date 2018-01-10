package ie.gmit.sw;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class UI {
	private Scanner s = new Scanner(System.in);
	public void Show() throws IOException, InterruptedException {
		
			System.out.println("Please enter Document 1's Name: ");
			String doc1 = s.nextLine();
			System.out.println("Please enter Document 2's Name: ");
			String doc2 = s.nextLine();
			System.out.println("Please enter k size: ");
			int k = s.nextInt();
			System.out.println("Please enter shingle size: ");
			int shingleSize = s.nextInt();
			System.out.println("Please enter thread pool size: ");
			int poolSize = s.nextInt();

			if (!doc1.contains(".txt")) {
				doc1 += ".txt";
			}

			if (!doc2.contains(".txt")) {
				doc2 += ".txt";
			}

			Launcher l = new Launcher();
			l.launch(doc1, doc2, k, shingleSize, poolSize);
		
			

	}
}