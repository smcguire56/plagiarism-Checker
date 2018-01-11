/**
 * 
 * @author g00330886
 * @author Sean McGuire
 * 
 */

package ie.gmit.sw;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.BlockingQueue;

public class FileParser implements Runnable, FileParserInterface {
	// declare all local variables used in the file parser class
	private String file;
	private int shingleSize;
	private BlockingQueue<Shingle> q;
	private Deque<String> buffer = new LinkedList<>();
	private int docId;

	// empty constructor
	public FileParser() {
		super();
	}

	/**
	 * 
	 * @param file
	 *            takes in the file name
	 * @param shingleSize
	 *            takes in the size of shingles from user
	 * @param q
	 *            blocking queue of shingles
	 * @param docId
	 *            takes in value of docId
	 */
	public FileParser(String file, int shingleSize, BlockingQueue<Shingle> q, int docId) {
		super();
		this.file = file;
		this.shingleSize = shingleSize;
		this.q = q;
		this.docId = docId;
	}

	@Override
	public void run() {
		try {
			// try to read in the file specified by the user.
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			String line = null;
			while ((line = br.readLine()) != null) {
				// read in each line of this file, making sure the line length
				// is greater than 0
				if (line.length() > 0) {
					// change the specific line to upper-case
					String uLine = line.toUpperCase();
					// split the line into separate words
					String[] words = uLine.split("\\w+\\.?");
					// add the words to the buffer
					addWordsToBuffer(words);
					// get the next shingle
					Shingle s = getNextShingle();
					// add s to the blocking queue
					q.put(s);
				}
			}
			// flush the buffer
			flushBuffer();
			// close the reader
			br.close();

			// catch all the exceptions
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param words
	 *            adds the words to the buffer
	 */
	private void addWordsToBuffer(String[] words) {
		for (String s : words) {
			buffer.add(s);
		}
	}

	/**
	 * 
	 * @return Shingle returns the next shingle value
	 */
	private Shingle getNextShingle() {
		// declare a new string builder containing the buffers values
		StringBuilder sb = new StringBuilder();
		int counter = 0;
		// if counter is less than the size of shingles and increment counter
		while (counter < shingleSize) {
			if (buffer.peek() != null) {
				sb.append(buffer.poll());
				counter++;
			} else {
				counter = shingleSize;
			}
		}
		if (sb.length() > 0) {
			// return the new shingle
			return (new Shingle(docId, sb.toString().hashCode()));
		} else {
			return null;
		}
	}

	/**
	 * 
	 * @throws InterruptedException
	 */
	private void flushBuffer() throws InterruptedException {
		while (buffer.size() > 0) {
			Shingle s = getNextShingle();
			if (s != null) {
				q.put(s);
			}
		}
		q.put(new Poison(docId, 0));
	}

}