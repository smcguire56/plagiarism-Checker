package ie.gmit.sw;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class UrlParser extends FileParser implements Runnable {
	private URL myUrl;
	private BlockingQueue<Shingle> b = new LinkedBlockingQueue<Shingle>();
	private int shingleSize = 1;
	
	public UrlParser() {
		super();
	}
	
	public UrlParser(String url) throws MalformedURLException {
		this.myUrl = new URL(url);
		System.out.println(myUrl);
	}
	

	@Override
	public void run() {
		try {
	        URL url = myUrl;
			BufferedReader in = new BufferedReader(
	        new InputStreamReader(url.openStream()));	
	        
	        String inputLine;
	        while ((inputLine = in.readLine()) != null) {
	            //System.out.println(inputLine);
	            String words[] = inputLine.split("\\s+");
	            
	            for (int i = 0; i < words.length; i++) {
					//System.out.println(words[i].hashCode() + " word: " + words[i] + " Url: " + myUrl.hashCode());
	            	System.out.printf("%s ", words[i]);
					int shingle = words[i].hashCode();
					Shingle s = new Shingle(myUrl.hashCode(), shingle);
				
					try {
						b.put(s);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	            }
	            System.out.println();
	 
	        }
	        in.close();
	        } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println();
	}

}
