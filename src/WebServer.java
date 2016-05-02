import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLConnection;

public class WebServer {
	
	@SuppressWarnings("resource")
	public static void main(String[] args) throws Exception{
	ServerSocket serverSocket = new ServerSocket(8118);
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
	while(true) {
		
		System.out.println("WAITING...");
		
		clientSocket = serverSocket.accept();

		in= new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		out= new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
		 
		 request = in.readLine();
		 
		 System.out.println(request);
		 
		 
	

        endOfRequest= request.indexOf(" HTTP/", 5);

        if (request.indexOf("GET /") == 0 && endOfRequest != -1) {

          // malformed requests just get ignored, and the web server goes back
          // to waiting for the next incoming request

          fileName= request.substring(5, endOfRequest);
         
          if(fileName.contains("html") ) {
         	 html = new HTMLReader(fileName);
             
             out.print("HTTP/1.1 200 OK" + "\n");
             out.print("Content-Length: " + html.html.length() + "\n");
             out.print("Content-Type: " + "text/html" + "\n");
             out.print("\n");
             out.flush();
             
             clientSocket.getOutputStream().write(html.html.getBytes());
             
             out.flush();
             out.close();

          }
          else {
         	file = new File(fileName);
         	contentType= URLConnection.getFileNameMap().getContentTypeFor(
                  file.toString());
         	
         	fileInputStream= new FileInputStream(file);

            // send the HTTP header to the client (first part of the response)
            out.print("HTTP/1.1 200 OK" + "\n");
            out.print("Content-Length: " + file.length() + "\n");
            out.print("Content-Type: " + contentType + "\n");
            out.print("\n");
            out.flush();

            // read and send the actual data to the client (either the text
            // file, the PDF, or the JPEG)
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
	}
	}
}
