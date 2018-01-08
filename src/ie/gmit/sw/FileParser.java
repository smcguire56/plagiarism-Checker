package ie.gmit.sw;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class FileParser implements Runnable {
	private File f;
	private BlockingQueue<Shingle> b = new LinkedBlockingQueue<Shingle>(1000);

	private BlockingQueue<Shingle> queue = new LinkedBlockingQueue<Shingle>(1000);

	private Deque<String> buffer = new LinkedList<>();

	private int shingleSize = 1, k , docId;

	public FileParser() {
		super();
	}

	public FileParser(File file, BlockingQueue<Shingle> q, int shingleSize, int k) {
		this.f = file;
		this.queue = q;
		this.shingleSize = shingleSize;
		this.k = k;
	}

	public FileParser(String file) throws IOException {
		this.f = new File(file);
		System.out.println(f);
	}

	@Override
	public void run() {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
		} catch (FileNotFoundException e) {
			System.out.println("File not found");
		}
		String line = null;

		try {
			while ((line = br.readLine()) != null) {
				String words[] = line.split("\\s+");
				addWordsToBuffer(words);
				Shingle s = getNextShingle();
				queue.put(s);
				System.out.println(words[0] +", HashCode: " + words[0].hashCode() + ", file: " + f);

				for (int i = 0; i < shingleSize; i++) {
					System.out.println(words[i].hashCode() + " word: " + words[i] + " file: " + f.hashCode());
					int shingle = words[i].hashCode();
					s = new Shingle(f.hashCode(), shingle);

					try {
						b.put(s);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}				    
			}
			
			flushBuffer();
			br.close();
		} catch (IOException | InterruptedException e) {
			System.out.println("File not found");
		}
	}
	
	private void flushBuffer() throws InterruptedException {
		while(buffer.size() > 0) {
			Shingle s = getNextShingle();
			if(s != null) {
				queue.put(s);
			}
			else {
				queue.put(new Poison(docId, 0));
			}
		}
	}

	private Shingle getNextShingle() {
		StringBuffer sb = new StringBuffer();
		int counter = 0;
		while(counter < shingleSize) {
			if(buffer.peek() != null) {
				sb.append(buffer.poll());
				counter++;
			}
		}  
		if (sb.length() > 0) {
			return(new Shingle(docId, sb.toString().hashCode()));
		}
		else {
			return(null);
		}
  	} // Next shingle

	private void addWordsToBuffer(String[] words) {
		for (String s : words) {
			buffer.add(s);
		}

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((b == null) ? 0 : b.hashCode());
		result = prime * result + ((f == null) ? 0 : f.hashCode());
		result = prime * result + shingleSize;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FileParser other = (FileParser) obj;
		if (b == null) {
			if (other.b != null)
				return false;
		} else if (!b.equals(other.b))
			return false;
		if (f == null) {
			if (other.f != null)
				return false;
		} else if (!f.equals(other.f))
			return false;
		if (shingleSize != other.shingleSize)
			return false;
		return true;
	}

	public BlockingQueue<Shingle> getQueue() {
		return queue;
	}

	public void setQueue(BlockingQueue<Shingle> queue) {
		this.queue = queue;
	}

}