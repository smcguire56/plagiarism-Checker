/**
 * 
 * @author g00330886
 * @author Sean McGuire
 * 
 */

package ie.gmit.sw;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Consumer implements Runnable {
	// declare local variables
	private BlockingQueue<Shingle> queue;
	private int k;
	private int[] minhashes;
	private ConcurrentMap<Integer, List<Integer>> map = new ConcurrentHashMap<Integer, List<Integer>>();
	private float jaccard;
	private long checks = 0;
	private ExecutorService pool;

	/**
	 * @param q
	 *            q is the blocking queue
	 * @param k
	 *            is the size of amount of min-hashes
	 * @param poolSize
	 *            the number of threads
	 */
	public Consumer(BlockingQueue<Shingle> q, int k, int poolSize) {
		this.queue = q;
		this.k = k;
		pool = Executors.newFixedThreadPool(poolSize);
		init();
	}

	/**
	 * 
	 * @return map
	 */
	public ConcurrentMap<Integer, List<Integer>> getMap() {
		return map;
	}

	private void init() {
		// generate a random amount of min-hashes initially
		Random random = new Random();
		minhashes = new int[k];
		for (int i = 0; i < minhashes.length; i++) {
			minhashes[i] = random.nextInt();
		}
	}

	public void run() {
		// for each document being passed in, as long as there is at least one
		// file,
		// try to access the shingles being generated from the first threads.
		int docCount = 2;
		while (docCount > 0) {
			try {
				// retrieve and remove from the head of the shingles queue and
				// use Shingle s to access it's specific hash code
				// to later do an XOR calculation on it.
				Shingle s = queue.take();
				if (s instanceof Poison) {
					// if a file is complete, decrement the docCount variable.
					docCount--;
				} else {
					// as long as there is still a document, generate a new
					// list.
					pool.execute(new Runnable() {
						@Override
						public void run() {
							// declare the new list, initially by accessing the
							// shingle s and get it's document ID.
							List<Integer> list = map.get(s.getDocid());

							// enter this loop to generate new values and
							// increment counter checks.
							for (int i = 0; i < minhashes.length; i++) {
								// value is calculated by getting the current
								// shingle hashcode and performing a XOR
								// calculation on the minhashes which was
								// generated from random set of numbers.
								int value = s.getShingleHashCode() ^ minhashes[i];
								// increment checks.
								setChecks(getChecks() + 1);
								if (list == null) {
									// if the list is empty, set it to a new
									// array list with size of k and
									// initializing it all to 0.
									list = new ArrayList<Integer>(Collections.nCopies(k, 0));
									// set the map with the documents Id and the
									// list.
									map.put(s.getDocid(), list);
								} else {
									// the list is already got values, make sure
									// it's current value is greater than value
									// and set it to that value.
									if (list.get(i) > value) {
										list.set(i, value);
									}
								}
							}
							// set the map with the documents Id and the list.
							map.put(s.getDocid(), list);
						}
					}); // runnable
				} // else
					// catch the interrupted exception
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}

		} // while

		// after the thread is done, shutdown the pool to complete the program.
		pool.shutdown();
		try {
			pool.awaitTermination(Long.MAX_VALUE, TimeUnit.MINUTES);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// calculate the jaccard value.
		List<Integer> intersection = map.get(1);
		intersection.retainAll(map.get(2));
		setJaccard((float) intersection.size() / (k * 2 - (float) intersection.size()));
	}// run

	/**
	 * 
	 * @return checks returns checks
	 */
	public long getChecks() {
		return checks;
	}

	/**
	 * 
	 * @param checks
	 *            sets the checks value
	 * 
	 */
	public void setChecks(long checks) {
		this.checks = checks;
	}

	/**
	 * 
	 * @return jaccard returns jaccard
	 */
	public float getJaccard() {
		return jaccard;
	}

	/**
	 * 
	 * @param jaccard
	 *            sets the jaccard value
	 */
	public void setJaccard(float jaccard) {
		this.jaccard = jaccard;
	}
}// Consumer class