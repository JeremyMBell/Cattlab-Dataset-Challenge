import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class HTMLReader {
	final String html;
	public HTMLReader(String fileName) throws FileNotFoundException, IllegalArgumentException {
		if(!fileName.endsWith(".html")) {
			throw new IllegalArgumentException("This is not an html file.");
			
		}
		BufferedReader br = new BufferedReader(new FileReader(new File(fileName)));
		StringBuffer sb = new StringBuffer();
		String line;
		try {
			while((line = br.readLine()) != null) {
				sb.append(line).append('\n');
			}
		} catch(IOException io) {
			System.out.println("An error has occurred when trying to read " + fileName);
		}
		
		html = sb.toString();
	}
}
