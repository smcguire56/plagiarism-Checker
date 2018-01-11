/**
 * 
 * @author g00330886
 * @author Sean McGuire
 * 
 */

package ie.gmit.sw;

import java.io.IOException;

public class Runner {
	// main method
	/**
	 * 
	 * @param args
	 *            main method
	 * @throws IOException
	 *             throws IO exception
	 * @throws InterruptedException
	 *             throws interrupted exception
	 */
	public static void main(String[] args) throws IOException, InterruptedException {
		// start a new user interface and call Show method.
		new UI().show();
	}
}