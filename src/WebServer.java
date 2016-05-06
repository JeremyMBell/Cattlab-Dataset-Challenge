import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collections;

public class WebServer {
	HashMap<String, String> data = new HashMap<String, String>();

	private String readHeder() {
		String data = "";
		try {
			FileInputStream stream = new FileInputStream(new File("header.csv"));
			BufferedReader br = new BufferedReader(new InputStreamReader(stream));
			String line = null;

			while ((line = br.readLine()) != null)
			data += line;

			br.close();
			stream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return data;
	}

	private void extractQuery(String name) {
		String queryType = name.substring((name.indexOf('?') + 1), name.indexOf('='));
		String query = name.substring((name.indexOf('=') + 1));
		if (query.contains("&")) {
			String queryData = query.substring(0, query.indexOf('&'));
			data.put(queryType, queryData);
			queryData = query.substring(query.indexOf('&') + 1);
			queryData = "?" + queryData;
			extractQuery(queryData);
		} else {
			data.put(queryType, query);
		}
	}

	private boolean validate(String line) {
		String[] entries = line.split(",");
		boolean[] ent = new boolean[entries.length];

		for (String query : data.keySet()) {
			String[] header = readHeder().split(",");
			for (int i = 0; i < header.length; i++) {
			// System.out.print(query + " " + header[i] + " " + entries[i]);
			if (header[i].equals(query)) {
				if (data.get(query).equals(entries[i]))
					ent[i] = true;

			}
			// System.out.println(ent[i]);
			}
		}
		ArrayList<Boolean> entList = new ArrayList<Boolean>();
		for (int i = 0; i < ent.length; i++)
			entList.add(ent[i]);
		if (Collections.frequency(entList, true) == data.size())
			return true;
		else
			return false;
	}

	@SuppressWarnings("resource")
	public static void main(String[] args) throws Exception {

		ServerSocket serverSocket = new ServerSocket(9000);
		Socket clientSocket;
		HTMLReader html;
		String request;
		BufferedReader in;
		PrintWriter out;
		String fileName, contentType;
		int endOfRequest;
		File file;
		FileInputStream fileInputStream;

		while (true) {
			WebServer object = new WebServer();
			System.out.println("WAITING...");

			clientSocket = serverSocket.accept();

			long time = System.currentTimeMillis();
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			out = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

			request = in.readLine();

			endOfRequest = request.indexOf(" HTTP/", 5);

			if (request.indexOf("GET /") == 0 && endOfRequest != -1) {

			String name = request.substring(5, endOfRequest);
			if (!name.contains("="))
				clientSocket.getOutputStream().write("NOT VALID QUERY".getBytes());
			else {
				int startQuery = name.indexOf('?');

				fileName = name.substring(0, startQuery);
				object.extractQuery(name);
				System.out.println(object.data.toString());

				file = new File(fileName);
				contentType = URLConnection.getFileNameMap().getContentTypeFor(file.toString());

				fileInputStream = new FileInputStream(file);

				out.print("HTTP/1.1 200 OK" + "\n");
				out.print("Content-Length: " + file.length() + "\n");
				out.print("Content-Type: " + contentType + "\n");
				out.print("\n");
				out.flush();
				BufferedReader br = new BufferedReader(new InputStreamReader(fileInputStream));

				String line = null;

				while ((line = br.readLine()) != null) {

					// System.out.println(line);
					if (object.validate(line))
						clientSocket.getOutputStream().write((line + "\n" + "\n" + "\n").getBytes());

				}

				out.flush();
				out.close();
				fileInputStream.close();

			}
			}
			System.out.println("TIME TAKEN FOR REQUEST: " + (System.currentTimeMillis() - time) + " Milli Seconds");
		}
	}

}
