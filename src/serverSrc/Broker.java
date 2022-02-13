package serverSrc;


// class for broker

import java.io.IOException;







public class Broker extends Server implements Runnable{
	
	//CONSTRUCTER
	public Broker(int PORT, ItemMap item_map ) throws IOException {
		super(PORT , item_map);
	}
	
	
	public void run() {

		
		while(true) 
		{
			try {
				
				//listening to incoming connections to broker
				
				client = ss.accept();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			try {
				System.out.println("[SERVER]"+ "[ time:"+serverTime+"]" +" >> A new client connected to broker: " + client.getRemoteSocketAddress());
				
				
				//update logs in UI
				UiModel.rc.setlog("A new client connected to broker: " + client.getRemoteSocketAddress());
			} catch (Exception e1) {
				
			}

			
			
			//thread to handle publishes and subscribers
			PubSubHandler clientThread = null;
			try {
				clientThread = new PubSubHandler(client , item_map);
				new Thread(clientThread :: run).start();
				
				
				
			} catch (IOException e) {
				e.printStackTrace();
			}

			
			
		}
		
		
	}
	
	
	
	
	
	
}
