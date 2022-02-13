package serverSrc;


//class for handle publishers and subscribers

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class PubSubHandler extends ClientHandler{
	
	//HashMap for store all publishers and subscribers 
	private static HashMap<String , Client> clients = new HashMap<String , Client> ();
	
	//HashMap for store all publishers and subscribers connections threads
	private static ArrayList<PubSubHandler> pubsubthreads = new  ArrayList<PubSubHandler>();
	
	//id
	private String id;

	public PubSubHandler(Socket client, ItemMap item_map) throws IOException {
		super(client, item_map);
	}

	
	public static ArrayList<PubSubHandler> getPubsubthreads() {
		return pubsubthreads;
	}


	public static void setPubsubthreads(ArrayList<PubSubHandler> pubsubthreads) {
		PubSubHandler.pubsubthreads = pubsubthreads;
	}

	
	public void run() {
		
		//login 
		//same as client-server implementation
		
		pubsubthreads.add(this);
		
		String str = null;
		
		
		try {
			
			sendln("-login-");
			sendln("enter id:");
			
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		
		
		try {
			
			while((str = getRec()) != null)
			{
				
				if(str.equals(""))
				{
					sendln("-1");
					sendln("-login-");
					sendln("enter id:");
					continue;
					
				}
				if(clients.containsKey(str))
				{
					if(clients.get(str).isActive())
					{
						sendln("Another user accessing this account");
						continue;
					}
					
					
					id = str;
					sendln("Enter password:");
					str = getRec();
					
					if(str.equals(clients.get(id).getPassword()))
					{
						clients.get(id).setActive(true);
						clients.get(id).setMypw(this.pw);
						sendln("loging sucsess");
						
						
						function(clients.get(id));

						
					}
					sendln("password incorrect");
					continue;
				}
				
				//what happen if new id
				
				sendln("register as new client..");
				sendln("Enter a password to complete registration.");
				sendln("or Enter X to go back to loging.");
				
				id = str;
				str = getRec();
				
				if(str.toUpperCase().equals("X") || str.equals(""))
				{
					sendln("-login-");
					sendln("enter id:");
					continue;
				}
				
				Client client = new Client(id, str, pw);
				if(!clients.containsKey(id))
				{
					clients.put(id, client);
				}else {
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
				pubsubthreads.remove(this);
				close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
		
		
		
	}
	
	
	
	//broker services
	public void function(Client client)
	{
		try {
			sendln("current bids:" + clients.get(id).getBids());
			sendln("current profit updates:" + clients.get(id).getPrfts());
			
			sendln("Subscribe to get updates on profits and bids :");
			sendln("	<BID > <Symbol> <Symbol>");
			sendln("	<PRFT> <Symbol> <Symbol>");
			sendln("Change profit :");
			sendln("	<Symbol> <security code> <value>");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		String str;
		
		try {
			while((str = getRec()) != null)
			{
				String line[] = str.strip().split(" ");
				
				
				
				//SUBSCRIBE TO PROFIT CHANGES
				
				if(line[0].toUpperCase().equals("PRFT") && line.length > 1)
				{
					
					//remove this subscriber from the all previous subscribed items 	(profits)
					for(String symbolofsuscribedprft : client.getPrfts())
					{
						//remove this client from all items profit subscribers list
						//where this client had subscribed
						item_map.get(symbolofsuscribedprft).getPrft_subs().remove(client);
						
						
					}
					//remove all previous profit subscriptions from client's account
					client.clearPrfts();
					
					
					//subscribe to set of items(profits)
					for(int i = 1 ; i < line.length ; i++)
					{
						//check if substring is equals to valid symbol
						if(item_map.containsKey(line[i]))
						{
							//set subscribed symbols in client's account
							client.setPrfts(line[i]);
							
							//add this client into selected item, subscribed clients list
							item_map.get(line[i]).addPrft_subs(client);
							
							
							send("0 ");
						}else {
							send("-1 ");
						}
					}
					
					
					
					sendln("");
					sendln("subscribed to:"+ client.getPrfts());
					try {
						//set UI log
						UiModel.rc.setlog(id + " subscribed to:"+  client.getPrfts()+ " profits updates");
					} catch (Exception e) {
					}
				}
				
				
				//SUBSCRIBE TO BIDING CHANGES
				
				//line = buffer.readline.spilt(" ")
				else if(line[0].toUpperCase().equals("BID") && line.length > 1)
				{
					
					//remove this subscriber from the all previous subscribed items (bids)
					for(String bids : client.getBids())
					{
						//remove this client from all items bids subscribers list
						//where this client had subscribed
						
						item_map.get(bids).getBid_subs().remove(client);
						
					}
					
					//remove all previous bids subscriptions from client's account
					client.clearBids();
					

					//subscribe to set of items(bids)
					for(int i = 1 ; i < line.length ; i++)
					{
						//check if substring is equals to valid symbol
						if(item_map.containsKey(line[i]))
						{
							//set subscribed symbols in client's account
							client.setBids(line[i]);
							
							//add this client into selected item, subscribed clients list(bids)
							item_map.get(line[i]).addBid_subs(client);
							send("0 ");
						}else {
							send("-1 ");
						}
					}
					sendln("");
					
					//sending subscribed items to client
					sendln("subscribed to:"+ client.getBids());
					try {
						UiModel.rc.setlog(id + " subscribed to:"+ client.getBids() + " bids updates");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
				
				// CHANGE PROFIT VALUE
				
				//check input is equals to valid item symbol
				else if(item_map.containsKey(line[0]))
				{
					int sec = 0;
					
					//ignore non integer inputs
					try {
						sec = Integer.parseInt(line[1]);
					}catch(Exception e) {
						sendln("-1");
						continue;
					}
					
					
					
					// check input is valid with security key of the item
					if(sec == item_map.get(line[0]).getSecurity()){
						
						float update;
						try {
							update = Float.parseFloat(line[2]);
						}catch(Exception e ) {
							sendln("-1");
							continue;
							
						}
						
						//assume profit can not be negative value
						if(update < 0)
						{
							sendln("-1");
							continue;
						}
						
						//rounding value
						update = (float) (Math.round(update*100)/100.0);
						
						//update profit of the item
						item_map.get(line[0]).setProfit(update);
						UiModel.rc.setlog(line[0] + " new profit update :" + update);
						
						
						try {
							//update TableView - UI
							UiModel.rc.refrashTable(line[0]);
							
						} catch (Exception e) {
							
						}					
						
						
						//sending profit updates to all the subscriber of that item (profits)
						for(Client aclient : item_map.get(line[0]).getPrft_subs())
						{
							aclient.sendln(line[0] +" " + "PRFT " + line[2]);
							
						}
						sendln("0");
						
						
						
						
						
					}

					
				}
				else
					sendln("-1");
				
				
				
			}
		} catch (IOException e) {

			//remove this thread from thread pool
			pubsubthreads.remove(this);
			//set clients online status to false
			clients.get(id).setActive(false);
			System.out.println("disconnected.");
			
		}
	}
	
	
	

}
