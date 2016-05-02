import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class WebServer {
	
	@SuppressWarnings("resource")
	public static void main(String[] args) throws Exception{
	ServerSocket serverSocket = new ServerSocket(8080);
	Socket clientSocket;
	String request = null;
	 BufferedReader in;
    PrintWriter out;
    
	while(true) {
		
		System.out.println("WAITING...");
		
		clientSocket = serverSocket.accept();
		
		in= new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		out= new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
		 
		 
		 request = in.readLine();
		 
		 out.print("HTTP/1.1 200 OK" + "\n");
       out.print("Content-Length: " + request.length() + "\n");
       out.print("Content-Type: " + "text/html" + "\n");
       out.print("\n");
       
        clientSocket.getOutputStream().write(request.getBytes());
        out.flush();
        out.close();

	}
	}
}
