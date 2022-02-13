package clientSrc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class Connection{

	private Socket 			server 	= null;
	private BufferedReader	receive = null;
	private PrintWriter 	send	= null;
	
	
	public Connection(String ip , int socket)
	{
		while(server == null)
		{
			try {
				server	= new Socket(ip , socket);
			}catch (Exception e) {
				continue;
			}
		}
		
		try {
			receive = new BufferedReader(new InputStreamReader(server.getInputStream() , StandardCharsets.UTF_8));
			send	= new PrintWriter(server.getOutputStream() , true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public String getRec() throws IOException 
	{
		return receive.readLine();
	}
	
	
	public void send(String str)throws Exception 
	{
	
		send.println(str);
		
		
	}
		
	public void close()
	{
		try {
			server.close();
			receive = null;
			send = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public Socket getServerPort()
	{
		return server;
		
	}

	
	public Socket getClient()
	{
		return server;
	}
	
	
}