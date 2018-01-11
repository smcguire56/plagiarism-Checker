/**
 * 
 * @author g00330886
 * @author Sean McGuire
 * 
 */

package ie.gmit.sw;

import java.io.IOException;
import java.util.Scanner;

public class UI {
	// declare scanner and menu boolean local variables
	private Scanner s = new Scanner(System.in);
	private boolean keepMenuAlive = false;

	/**
	 * show method displays the menu and asks user for their input.
	 * 
	 * @throws IOException
	 *             throws IO
	 * @throws InterruptedException
	 *             throws Interrupted
	 */
	public void show() throws IOException, InterruptedException {

		do {
			System.out.println("========================================");
			System.out.println("Starting new Document Similarity Checker");
			System.out.println("========================================\n");

			// ask user for their input values.
			System.out.println("Please enter Document 1's Name: ");
			String doc1 = s.next();
			System.out.println("Please enter Document 2's Name: ");
			String doc2 = s.next();
			System.out.println("Please enter k size: ");
			int k = s.nextInt();
			System.out.println("Please enter shingle size: ");
			int shingleSize = s.nextInt();
			System.out.println("Please enter thread pool size: ");
			int poolSize = s.nextInt();

			// append ".txt" to the documents name string
			if (!doc1.contains(".txt")) {
				doc1 += ".txt";
			}
			if (!doc2.contains(".txt")) {
				doc2 += ".txt";
			}

			// declare a new launcher instance and launch it with the parameters
			Launcher l = new Launcher();
			l.launch(doc1, doc2, k, shingleSize, poolSize);

		} while (keepMenuAlive);

	}
}