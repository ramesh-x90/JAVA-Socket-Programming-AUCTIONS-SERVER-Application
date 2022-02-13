package serverSrc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Server extends Thread{
	
	//variable to track server time(updating per second)
	public static String serverTime;
	
	
	//create objects 
	protected ServerSocket 	 ss 		= null;
	protected BufferedReader br			= null;
	protected PrintWriter 	 pw 		= null;
	protected Socket 		 client 	= null;
	protected ItemMap 		 item_map	= null;
	static 	  Timer 		 timer 		= null;
	private	  ServerHandler	 sh 		= null;
	private ClientHandler clientThread  = null;
	
	private int t = 0 ;
	
	
	//buffer to store keyboard inputs
	public static BufferedReader keyinput = new BufferedReader(new InputStreamReader(System.in));
	
	//creating object of DateTimeFormatter to format date
	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
	
	private ArrayList<PrintWriter> pws = new ArrayList<PrintWriter>();
	
	//class constructor
	Server(int PORT, ItemMap item_map , int t) throws IOException
	{
		this.t = t ; 
		ss = new ServerSocket(PORT);
		serverTime = LocalTime.now().format(formatter);
		
		this.item_map = item_map;
		
		
		//thread for track time
		timer = new Timer(formatter);
		timer.start();
		
	}
	
	Server(int PORT, ItemMap item_map ) throws IOException
	{
		
		ss = new ServerSocket(PORT);
		serverTime = LocalTime.now().format(formatter);
		
		this.item_map = item_map;
	}
	
	@Override
	public void run()
	{
		
		//still not finalized
		sh = new ServerHandler(t , timer, pws , item_map);
		sh.start();
		//
		
		
		while(true) 
		{
			try {
				client = ss.accept();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			try {
				System.out.println("[SERVER]"+ "[ time:"+serverTime+"]" +" >> A new client connected to client server: " + client.getRemoteSocketAddress());
				UiModel.rc.setlog(" A new client connected to client server : " + client.getRemoteSocketAddress());
				
			} catch (Exception e1) {
				
			}
			
			try {
				//for broadcast if we need
				pw 	= new PrintWriter(client.getOutputStream());
				pws.add(pw);
			} catch (IOException e) {
				e.printStackTrace();
			}

			
			
			
			
			
			try {
				clientThread = new ClientHandler(client , item_map );
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			//References need to operate client thread
			// when deal with time
			clientThread.setServerHandler(sh);
			clientThread.setTimer(timer);
			
			//initiate client thread
			new Thread(clientThread :: run).start();
			
			
		}
		
		
	}
	
	static void setServerTime(String t)
	{
		serverTime = t;
	}
	
	static String getServerTime()
	{
		return serverTime;
	}


	ServerSocket getServerSocket()
	{
		return ss;
	}
	
	
	

	public ClientHandler getClientThread() {
		return clientThread;
	}

	public void setClientThread(ClientHandler clientThread) {
		this.clientThread = clientThread;
	}

	@SuppressWarnings("deprecation")
	public void close() throws IOException
	{
		ss.close();
		sh.stop();
		timer.stop();
		
		
	}
	



}
