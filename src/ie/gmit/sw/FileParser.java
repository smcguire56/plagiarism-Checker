package ie.gmit.sw;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class FileParser implements Runnable {
	private File f;
	private BlockingQueue<Shingle> b = new LinkedBlockingQueue<Shingle>(100);
	private BlockingQueue<Shingle> queue = new LinkedBlockingQueue<Shingle>(100);

	private int shingleSize = 1;
		
	public FileParser() {
		super();
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
			while((line = br.readLine()) != null) {
				String words[] = line.split("\\s+");
				
				for (int i = 0; i < shingleSize; i++) {
					System.out.println(words[i].hashCode() + " word: " + words[i] + " file: " + f.hashCode());
					int shingle = words[i].hashCode();
					Shingle s = new Shingle(f.hashCode(), shingle);

					try {
						b.put(s);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
			}
		} catch (IOException e) {
			System.out.println("File not found");
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