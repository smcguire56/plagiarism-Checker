package ie.gmit.sw;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Consumer implements Runnable {
	private BlockingQueue<Shingle> queue;
	private int k;
	private int[] minhashes; // The random stuff
	private Map<Integer, List<Integer>> map = new HashMap<Integer, List<Integer>>();
	private ExecutorService pool;
	private Shingle s = null;

	public Consumer(BlockingQueue<Shingle> q, int k, int poolSize) {
		this.queue = q;
		this.k = k;
		setPool(Executors.newFixedThreadPool(poolSize));
		init();
	}

	private void init() {
		Random random = new Random();
		minhashes = new int[k]; // k = 200 - 300
		for (int i = 0; i < minhashes.length; i++) {
			minhashes[i] = random.nextInt(0);
		}
	}// init

	public void run() {
		int docCount = 2; // FIX THIS
		while (docCount > 0) {
			try {
				s = queue.take();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} // Blocking method

			if (s instanceof Poison) {
				docCount--;
			} else {
				pool.execute(new Runnable() {

					@Override
					public void run() {
						int docCount = 2;// change
						while (docCount > 0) {
							try {
								Shingle s = queue.take();
								// do poison check
								if (s.getShingleHashCode() == -99) {
									docCount--;
								} else {
									pool.execute(new Runnable() {
										public void run() {
											List<Integer> list = map.get(s.getDocid());
											
											for (int i = 0; i < minhashes.length; i++) {
												int value = s.getShingleHashCode() ^ minhashes[i];
												if (list == null) {
													list = new ArrayList<Integer>(Collections.nCopies(k, Integer.MAX_VALUE));
													map.put(s.getDocid(), list);
												} else 
												{
													if (list.get(i) > value) 
													{
														list.set(i, value);
													}
												}
											}
										}
									});
								}
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				});

			} // Else
		} // While
	}// Run

	public ExecutorService getPool() {
		return pool;
	}

	public void setPool(ExecutorService pool) {
		this.pool = pool;
	}

	public Map<Integer, List<Integer>> getMap() {
		return map;
	}

	public void setMap(Map<Integer, List<Integer>> map) {
		this.map = map;
	}

}// Consumer