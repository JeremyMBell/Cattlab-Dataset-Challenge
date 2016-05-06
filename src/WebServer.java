import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLConnection;
import java.util.HashMap;

public class WebServer {
	HashMap<String,String> data = new  HashMap<String,String>();
	
	private void extractQuery(String name) {
	String queryType = name.substring((name.indexOf('?')+1),name.indexOf('='));
    String query = name.substring((name.indexOf('=')+1));
    if(query.contains("&")) {
   	 String queryData = query.substring(0, query.indexOf('&'));
   	 data.put(queryType,queryData);
   	 queryData = query.substring(query.indexOf('&') + 1);
   	 queryData = "?" + queryData;
   	 extractQuery(queryData);
    }
    else {
   	data.put(queryType, query);
    }
	}
	@SuppressWarnings("resource")
	public static void main(String[] args) throws Exception{
		
	ServerSocket serverSocket = new ServerSocket(8000);
	Socket clientSocket;
	HTMLReader html ;
	String request ;
	 BufferedReader in;
    PrintWriter out;
    String fileName, contentType;
    int endOfRequest;
    File file;
    int byteOfData;
    FileInputStream fileInputStream;
    WebServer object = new WebServer();
    
	while(true) {
		
		System.out.println("WAITING...");
		
		
		
		clientSocket = serverSocket.accept();

		long time = System.currentTimeMillis();
		in= new BufferedReader(new InputStreamReader(clientSocket
		.getInputStream()));
		out= new PrintWriter(new OutputStreamWriter(clientSocket
		.getOutputStream()));
		 
		 request = in.readLine();
		 
		 System.out.println(request);
		 
	   
	

        endOfRequest= request.indexOf(" HTTP/", 5);
       

        if (request.indexOf("GET /") == 0 && endOfRequest != -1) {

  
          String name= request.substring(5, endOfRequest);
          int startQuery = name.indexOf('?');
          
          fileName = name.substring(0, startQuery);
        object.extractQuery(name);
        System.out.println(object.data.toString());
          
          if(fileName.contains("kl") ) {
         	 html = new HTMLReader(fileName);
             
             out.print("HTTP/1.1 200 OK" + "\n");
             out.print("Content-Length: " + html.html.length() + "\n");
             out.print("Content-Type: " + "text/html" + "\n");
             out.print("\n");
             out.flush();
             
             clientSocket.getOutputStream().write(html.html.getBytes());
             
             out.flush();
             out.close();
         /////////////////////////////////////////////////////////////
          }
          else {
         	file = new File(fileName);
         	contentType= URLConnection.getFileNameMap().getContentTypeFor(
                  file.toString());
         	
         	fileInputStream= new FileInputStream(file);
            
            
            out.print("HTTP/1.1 200 OK" + "\n");
            out.print("Content-Length: " + file.length() + "\n");
            out.print("Content-Type: " + contentType + "\n");
            out.print("\n");
            out.flush();

          
            byteOfData= fileInputStream.read();
            while (byteOfData != -1) {
              clientSocket.getOutputStream().write(byteOfData);
              byteOfData= fileInputStream.read();
            }

            out.flush();
            out.close();
            fileInputStream.close();
          }
          
        }
        System.out.println("TIME TAKEN FOR REQUEST: " 
        + (System.currentTimeMillis()-time) + " Milli Seconds");
	}
	}
	

}
