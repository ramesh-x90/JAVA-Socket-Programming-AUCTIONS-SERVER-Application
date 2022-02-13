package serverSrc;


//this class implemented in both client server and broker 
//to create object from connected clients
//work as a account
//helps clients to reconnect to same session 


import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;

public class Client {

	//clients iD
	private String id;
	
	//password
	private String password;
	
	//current output stream of the client 
	//helps to send information to this client only
	private PrintWriter mypw;
	
	//clients online status
	//if disconnected will be false
	private boolean active;
	
	//check online status
	public boolean isActive() {
		return active;
	}

	//set online status
	public void setActive(boolean active) {
		this.active = active;
	}

	//client's currently subscribed bids (symbols only)
	private HashSet<String> bids = new HashSet<String>();
	
	//client's currently subscribed profits (symbols only)
	private HashSet<String> prfts = new HashSet<String>();
	
	//list of bids done by clients
	private HashMap<String , Float> ClientBids = new HashMap<String , Float>();
	
	//return all bids done by client
	public HashMap<String, Float> getClientBids() {
		return ClientBids;
	}

	//set all bids done by client
	public void setClientBids(HashMap<String, Float> clientBids) {
		ClientBids = clientBids;
	}

	//Constructor
	public Client(String id , String password , PrintWriter mypw ) {
		this.id = id;
		this.password = password;
		this.mypw = mypw;
		this.active = true;
		
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public PrintWriter getMypw() {
		return mypw;
	}


	public void setMypw(PrintWriter mypw) {
		this.mypw = mypw;
	}


	public HashSet<String> getBids() {
		return bids;
	}

	//add client's subscribed bids
	public void setBids(String item) {
		this.bids.add(item);
	}

	//add client's subscribed profits
	public HashSet<String> getPrfts() {
		return prfts;
	}

	//set client's subscribed profits
	public void setPrfts(String name) {
		this.prfts.add(name);
	}

	//send to client
	public void sendln(String str)
	{
		mypw.println(str);
		mypw.flush();
	}

	public void send(String str)
	{
		mypw.print(str);
		mypw.flush();
	}

	//clear clients profit subscriptions
	public void clearPrfts() {

		prfts.clear();
		
		
	}

	//clear clients bids subscriptions
	public void clearBids() {
		
		bids.clear();
		
	}


	
	
	

}
