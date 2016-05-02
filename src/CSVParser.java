import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class CSVParser {
	public static Map<String, String>[] CSVToMap(String csvFile, String[] headers) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(csvFile)));
			String line;
			ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
			while((line = br.readLine()) != null) {
				String[] values = line.split(",");
				HashMap<String, String> map = new HashMap<String, String>();
				for(int i = 0; i < values.length && i < headers.length; i++) {
					map.put(headers[i], values[i]);
				}
				list.add(map);
			}
			Map<String, String>[] maps = new Map[list.size()];
			for(int i = 0; i < list.size(); i++) {
				maps[i] = list.get(i);
			}
			return maps;
		} catch(Exception e) {
			return null;
		}
	}
}
