package ie.gmit.sw;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Launcher {
	private BlockingQueue<Shingle> q = new LinkedBlockingQueue<Shingle>(100);

	public void launch(String doc1, String doc2, int k, int shingleSize, int poolSize) throws InterruptedException {
		Thread t1 = new Thread(new FileParser(doc1, shingleSize, k, q, 1), "th1");
		Thread t2 = new Thread(new FileParser(doc2, shingleSize, k, q, 2), "th2");
		
		Consumer c;
		
		Thread t3 = new Thread(c = new Consumer(q, k, poolSize), "th3");

		t1.start();
		System.out.println("t1 start");
		t2.start();
		System.out.println("t2 start");
		t3.start();

		System.out.println("t3 start");

		t1.join();
		System.out.println("t1 finished");

		t2.join();
		System.out.println("t2 finished");

		t3.join();
		System.out.println("t3 finished");	
		
		// print Jacquared
		System.out.println("Jacquared: " +c.getJacquared() * 100);
	}
}
