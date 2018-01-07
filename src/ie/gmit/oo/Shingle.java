package ie.gmit.oo;

import java.util.Random;

public class Shingle {
	private int docid = 0;
	private int shingleHashCode = 0;

	public Shingle(int docid, int shingleHashCode) {
		super();
		this.docid = docid;
		this.shingleHashCode = shingleHashCode;
	}

	public Shingle() {
		super();
	}
	
	public int getDocid() {
		return docid;
	}

	public void setDocid(int docid) {
		this.docid = docid;
	}

	public int getShingleHashCode() {
		return shingleHashCode;
	}

	public void setShingleHashCode(int shingleHashCode) {
		this.shingleHashCode = shingleHashCode;
	}

	public int[] getMinHashSet(int size) {
		Random r = new Random();
		int[] hashes = new int[size];
		
		for (int i = 0; i < size; i++) {
			hashes[i] = r.nextInt();
		}
		return hashes;
	}

	public boolean equals(Object o) {
		Shingle other = (Shingle) o;
		return this.shingleHashCode == other.shingleHashCode;
	}

}
