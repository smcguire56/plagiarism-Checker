/**
 * 
 * @author g00330886
 * @author Sean McGuire
 * 
 */

package ie.gmit.sw;

public class Shingle {
	// declare local variables and set to 0.
	private int docid = 0;
	private int shingleHashCode = 0;

	/**
	 * 
	 * @param docid
	 *            doc id
	 * @param shingleHashCode
	 *            hashcode of shingles
	 */
	public Shingle(int docid, int shingleHashCode) {
		super();
		this.docid = docid;
		this.shingleHashCode = shingleHashCode;
	}

	public Shingle() {
		super();
	}

	/**
	 * 
	 * @return docid
	 */
	public int getDocid() {
		return docid;
	}

	/**
	 * 
	 * @param docid
	 *            sets the doc id
	 */
	public void setDocid(int docid) {
		this.docid = docid;
	}

	/**
	 * 
	 * @return hashcode value returns
	 */
	public int getShingleHashCode() {
		return shingleHashCode;
	}

	/**
	 * 
	 * @param shingleHashCode
	 *            sets the shingle hash code
	 */
	public void setShingleHashCode(int shingleHashCode) {
		this.shingleHashCode = shingleHashCode;
	}
}
