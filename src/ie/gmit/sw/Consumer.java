package ie.gmit.sw;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Consumer implements Runnable {
	private BlockingQueue<Shingle> queue;
	private int k;
	private int[] minhashes;
	private Map<Integer, List<Integer>> map = new ConcurrentHashMap<Integer, List<Integer>>();
	private ExecutorService pool;

	public Consumer(BlockingQueue<Shingle> q, int k, int poolSize) {
		this.queue = q;
		this.k = k;
		pool = Executors.newFixedThreadPool(poolSize);
		init();
	}

	private void init() {
		Random random = new Random();
		minhashes = new int[k]; // k = 200 - 300
		for (int i = 0; i < minhashes.length; i++) {
			minhashes[i] = random.nextInt();
		}
	}

	public void run() {
		int docCount = 2;
		while (docCount > 0) {
			Shingle s;
			try {
				s = queue.take();
				if (s instanceof Poison) {
					docCount--;
				} else {
					pool.execute(new Runnable() {
						@Override
						public void run() {
							List<Integer> list = map.get(s.getDocid());

							for (int i = 0; i < minhashes.length; i++) {
								int value = s.getShingleHashCode() ^ minhashes[i];
								if (list == null) {
									list = new ArrayList<Integer>(Collections.nCopies(k, Integer.MAX_VALUE));
									map.put(s.getDocid(), list);
								} else {
									if (list.get(i) > value)
										list.set(i, value);
								}
							}
							map.put(s.getDocid(), list);
						}
					});
				}
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}

		}

		List<Integer> intersection = map.get(1);
		intersection.retainAll(map.get(2));
		float jacquared = (float) intersection.size() / (k * 2 - (float) intersection.size());

		System.out.println("Jacquared Index: " + jacquared);
	}// run
}// Consumer class