/**
 * 
 * @author g00330886
 * @author Sean McGuire
 * 
 */
package ie.gmit.sw;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Launcher {
	// local variables
	private BlockingQueue<Shingle> q = new LinkedBlockingQueue<Shingle>(100);

	/**
	 * 
	 * @param doc1
	 *            document 1
	 * @param doc2
	 *            document 2
	 * @param k
	 *            size of amount of min-hashes
	 * @param shingleSize
	 *            size of shingles
	 * @param poolSize
	 *            number of threads
	 * @throws InterruptedException
	 *             throws interrupted
	 */
	public void launch(String doc1, String doc2, int k, int shingleSize, int poolSize) throws InterruptedException {

		// declare the new threads taking in the local variables
		Thread t1 = new Thread(new FileParser(doc1, shingleSize, q, 1), "T1");
		Thread t2 = new Thread(new FileParser(doc2, shingleSize, q, 2), "T2");
		// declare a new Consumer c
		Consumer c;
		// declare a thread on c for the consumer
		Thread t3 = new Thread(c = new Consumer(q, k, poolSize), "T3");

		// start all threads
		t1.start();
		t2.start();
		t3.start();

		// threads complete after they all die
		t1.join();
		t2.join();
		t3.join();

		// print Jaccard and number of checks
		System.out.println("Jaccard: " + c.getJaccard() * 100 + " %.");
		System.out.println("Checks: " + c.getChecks() + " times.");
	}
}
