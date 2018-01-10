package ie.gmit.sw;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class UI {
	private Scanner s = new Scanner(System.in);
	private boolean keepMenuAlive = false;

	private BlockingQueue<Shingle> q = new LinkedBlockingQueue<Shingle>(100);

	public void Show() throws IOException, InterruptedException {
		do {
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

			Thread t1 = new Thread(new FileParser(doc1, q, shingleSize, 1));
			Thread t2 = new Thread(new FileParser(doc2, q, shingleSize, 2));
			Thread t3 = new Thread(new Consumer(q, k, poolSize));

			t1.start();
			t2.start();
			t3.start();

			t1.join();
			t2.join();
			t3.join();
		} while (keepMenuAlive);

	}
	
	public boolean isKeepMenuAlive() {
		return keepMenuAlive;
	}

	public void setKeepMenuAlive(boolean keepMenuAlive) {
		this.keepMenuAlive = keepMenuAlive;
	}

}