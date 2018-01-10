package ie.gmit.sw;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.BlockingQueue;

public class FileParser implements Runnable {

	private BlockingQueue<Shingle> queue;
	private Deque<String> buffer = new LinkedList<>();
	private String f;
	private int shingleSize, docId;

	public FileParser() {
		super();
	}

	public FileParser(String doc1, BlockingQueue<Shingle> queue, int shingleSize, int docId) {
		this.f = doc1;
		this.queue = queue;
		this.shingleSize = shingleSize;
		this.docId = docId;
	}

	@Override
	public void run() {
		BufferedReader br = null;
		String line = null;

		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
			while ((line = br.readLine()) != null) {
				if (line.length() > 0) {
					line = line.toUpperCase();
					String words[] = line.split("\\s+");
					addWordsToBuffer(words);
					Shingle s = getNextShingle();
					queue.put(s);

					System.out.println(words[0] + ", HashCode: " + words[0].hashCode() + ", file: " + f);
				}
			}

			flushBuffer();
			br.close();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void flushBuffer() throws InterruptedException {
		while (buffer.size() > 0) {
			Shingle s = getNextShingle();
			if (s != null) {
				queue.put(s);
			} else {
				queue.put(new Poison(docId, 0));
			}
		}
	}

	private Shingle getNextShingle() {
		StringBuffer sb = new StringBuffer();
		int counter = 0;
		while (counter < shingleSize) {
			if (buffer.peek() != null) {
				sb.append(buffer.poll());
				counter++;
			} else {
				counter = shingleSize;
			}
		}
		if (sb.length() > 0) {
			return (new Shingle(docId, sb.toString().hashCode()));
		} else {
			return (null);
		}
	}

	private void addWordsToBuffer(String[] words) {
		for (String s : words) {
			buffer.add(s);
		}

	}

}