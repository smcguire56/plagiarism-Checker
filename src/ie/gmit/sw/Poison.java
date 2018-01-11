/**
 * 
 * @author g00330886
 * @author Sean McGuire
 * 
 */
package ie.gmit.sw;

public class Poison extends Shingle {

	public Poison() {
		super();
	}

	/**
	 * Poison instance which extends Shingle class
	 * 
	 * @param docid
	 *            doc id
	 * @param shingleHashCode
	 *            hash code
	 */
	public Poison(int docid, int shingleHashCode) {
		super(docid, shingleHashCode);
	}

}
