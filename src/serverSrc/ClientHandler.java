package serverSrc;


// thread for handle client-server connection
//this class initiate in server by feeding class reference to Thread class

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ClientHandler {
	
	//objects of socket , buffeedReader and print-writer classes
	protected final Socket 			socket 	;
	protected final BufferedReader	br 		;
	protected final PrintWriter 	pw 		;
	
	
	//reference to item map
	protected final ItemMap 		item_map;
	
	//variable to store clients id
	private String ID = null;
	
	//reference to server handler thread
	private ServerHandler sh = null;
	
	//reference to timer threads
	private Timer timer		 = null;
	
	//object of client
	private Client client = null;
	
	// store all the clients connected to server (only clients who successfully login to service)
	private static ConcurrentHashMap<String , Client> clients = new ConcurrentHashMap<String , Client> ();
	
	//store all connections clientHandler
	private static ArrayList<ClientHandler> clientthreads = new ArrayList<ClientHandler>();
	
	//return threads of all connections
	public static ArrayList<ClientHandler> getClientthreads() {
		return clientthreads;
	}

	public static void setClientthreads(ArrayList<ClientHandler> clientthreads) {
		ClientHandler.clientthreads = clientthreads;
	}

	private final int extendtime = 60;
	
	//class constructor
	public ClientHandler(Socket client, ItemMap item_map) throws IOException {
		
		this.socket		= 	client;
		this.item_map	=  	item_map;
		this.br = new BufferedReader(new InputStreamReader(client.getInputStream()));
		this.pw 	= new PrintWriter(client.getOutputStream());
		
		
	}
	
	//get reference to server handler
	public void setServerHandler(ServerHandler sh)
	{
		this.sh = sh;
	}

	//get reference to timer
	public void setTimer(Timer timer) 
	{
		this.timer = timer;
		
	}

	//initiate thread
	public void run() {
		
		//add this thread to connected connections to client-server
		clientthreads.add(this);
		
		//variable to store client's incoming messages
		String str = null;
		
		//LOGIN
		try {
			
			sendln("-login-");
			sendln("enter id:");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		
		
		try {
			
			//get client respond
			while((str = getRec()) != null)
			{
				//login
				if(str.equals(""))
				{
					sendln("-1");
					sendln("-login-");
					sendln("enter id:");
					continue;
					
				}
				
				if(str.equals("quit"))
				{
					
					sendln(" closing connection...");
					sendln("quit");
					System.out.println("Client got Disconnected.....");
					clientthreads.remove(this);
					close();
				}
				
				//ID CHECK from clients list (hash map<client'ID , client object>)
				if(clients.containsKey(str))
				{
					//case where client is used some one else id password or
					//same client from another device or another session
					if(clients.get(str).isActive())
					{
						//this client can not login to service
						sendln("Another user is accessing this account");
						continue;
					}
					
					//case 2 client who reconnection to server after disconnect
					ID = str;
					sendln("Enter password:");
					str = getRec();
					
					//PASSWORD CHECK
					if(str.equals(clients.get(ID).getPassword())   )
					{
						// login success
						//set clients online status to active
						clients.get(ID).setActive(true);
						//set clients new output stream to current thread output stream
						clients.get(ID).setMypw(this.pw);
						sendln("loging sucsess");
						
						sendln("previous bids done by you:");
						String out;
						
						//sending all the bids done by client previously
						
						for (String avalue : clients.get(ID).getClientBids().keySet()) 
						{
							out = avalue + " "+Float.toString(clients.get(ID).getClientBids().get(avalue));
							
							sendln(out);
							
						}
						
						//things that can be happen after successful login
						//biding service provide by server
						function(clients.get(ID));

						
						
					}
					sendln("password incorrect");
					continue;
				}
				
				//what happen if new id
				//new client handled in here 
				//didn't registered to service before
				sendln("register as new client..");
				sendln("Enter a password to complete registration.");
				sendln("or Enter X to go back to loging.");
				
				//set ID 
				ID = str;
				str = getRec();
				
				//case 1: empty input | lengthy input | x to go back
				if(str.toUpperCase().equals("X") || str.equals("") || str.length() > 10)
				{
					sendln("-login-");
					sendln("enter id:");
					continue;
				}
				
				//check if Id is available to use
				if(!clients.containsKey(ID))
				{
					//if ID is available new client object will created and 
					// store to client-serve clients list
					client = new Client(ID, str, pw);
					clients.put(ID, client);
					
				}else {
					//if Id is not available go back
					sendln("ID is taken");
					sendln("enter id:");
					continue;
				}
				
				function(client);
				
				
				
			}
		} catch (IOException e) {
			System.out.println(">> client Disconnected");
			
		}finally{
			
			try {
				//if connection is lost remove this thread instance from thread pool
				clientthreads.remove(this);
				close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
		
	}
	
	private int function(Client client) {
		
		
		// all services done by server
		
		//allowed sending input formats
		try {
			sendln("[SERVER]"+ "[ time:"+Server.getServerTime()+"] >> " + "U can bid now");
			sendln("           to ckeck bid values : <item-symble>"); 
			sendln("           to bid              : <item-symble> <bid>");
		} catch (IOException e1) {
			e1.printStackTrace();
		} 
		
		
		
		String str = null;
		
		try {
			while((str = getRec()) != null)
			{
				//if client want to leave
				if(str.equals("quit"))
				{
					
					sendln(" closing connection...");
					sendln("quit");
					System.out.println("Client got Disconnected.....");
					clientthreads.remove(this);
					clients.get(ID).setActive(false);
					close();
					break;
				}

					

				
				
				if(ID != null)
				{
					
				  
					//token user commands
					String[] line = str.strip().split(" ");
					
					if(item_map.containsKey(line[0]) && line.length < 3)
					{
						// user need current bid value
						if(line.length == 1)
						{
							sendln(" bid :" + item_map.getPrice(line[0]));
							continue;
							
						}
						
						
						float bid = 0;
						
						// exception when user input != a number
						try {
							bid = Float.parseFloat(line[1]);
						} catch (NumberFormatException e) {
							sendln("-1 invalid symbol");
							continue;
						}
						
						//rounding value
						bid = (float) (Math.round(bid*100)/100.0);
						
						//SUCCESSFUL BID within time frame
						if(bid > item_map.getPrice(line[0]) && sh.getDeadline() > timer.getPassedSec())
						{
							//update item current max bid value form item map item objects
							item_map.update(line[0], bid , ID);
							sendln("bid accepted");
							
							//sending new price value to subscribers
							notify("new bid value of "+line[0]+" is " + Float.toString(bid)  , line[0]);
							
							//add client's bids to client's HashMap
							client.getClientBids().put(line[0], bid);
							try {
								UiModel.rc.setlog("new bid value of "+line[0]+" is " + Float.toString(bid) +" " +  " -> updated by " +ID);
								
								///////////update TableView
								UiModel.rc.refrashTable(line[0]);
								
								
							} catch (Exception e) {
								
							}
							
							//case where if client bid in last 60 seconds before server stop its biding service
							if(sh.getDeadline()  - timer.getPassedSec() < 60 && sh.getDeadline() - timer.getPassedSec() > 0)
							{
								//server going to extends service time (how long service is going to active) by constant as a requirement
								sh.extendDeadline(extendtime);
							}
							
						}
						else
						{
							
							sendln("-2 bid is lower than or equal to the current bid or bid time has expired");
						}
						
					}
					
					//sending list of available items for bid
					else if(line[0].toLowerCase().equals("list"))
					{
						item_map.getItems().forEach(i -> {
							try {
								sendln(i);
							} catch (IOException e) {
								e.printStackTrace();
							}
						});
						
						
					}
					else
					{
						sendln( "-1 invalid symbol");
					}
					 
				}
				else
				{
					sendln("unknown request.....");
				}
				
			}
		} catch (IOException e) {

			//client got disconnected
			clientthreads.remove(this);
			
			//remove this client from clients thread pool
			clients.get(ID).setActive(false);
			System.out.println("client disconnected");
			
		}
		return -1;
		
		
		
		
		
		
	}
	

	private void notify(String str, String item) throws IOException {
		
		//send notifications to subscribers of a specific item
		
		
		for(Client c : item_map.get(item).getBid_subs())
		{
			c.sendln(str);
		}
		
		 
		
	}

	public void close() throws IOException {
		br.close();
		pw.close();
		socket.close();
		
	}

	public String getRec() throws IOException
	{
		//have to implement decryption
		String in;
		in = br.readLine();
		return in;	
		
	}
	
	public void sendln(String str) throws IOException
	{
		//have to implement encryption
		pw.println(str);
		pw.flush();
	}
	public void send(String str) throws IOException
	{
		//have to implement encryption
		pw.print(str);
		pw.flush();
	}

	public void sendln(String str , PrintWriter pw) throws IOException
	{
		//have to implement encryption
		pw.println(str);
		pw.flush();
	}
	

	
	
	
}
