package serverSrc;



import java.io.IOException;
import java.net.BindException;
import javafx.application.Application;


public class Main {
	

	private static ItemMap item_map;
	private static Server server = null;
	private static Broker broker;
	
	

	//printing dots
	public static void printdots() throws InterruptedException 
	{
		for(int i = 0 ; i < 6 ; i++) {
			System.out.print(".");
			Thread.sleep(10);
		}
		System.out.println("");
	}

	
	//main driver
	public static void main(String[] args) throws IOException, InterruptedException
	{
		//check CLI arguments
		
		if(args.length != 1){
			System.out.println("<Server.Main> <seconds>");
			System.exit(0);
		}
		
		
				
				
		//creating objects of hash-map and CSVreader to read csv and store data in to custom hash-map
		
		item_map = new ItemMap(100 , (float) 0.7 , 20);
		String DBfilepath = "stocks.csv";
		CSVreader stockcsv = null;
		
		
		try
		{
			//read CSV file and create data base
			stockcsv = new CSVreader(DBfilepath,item_map);
		}
		catch(Exception e)
		{
			System.out.println("data file not found.");
			System.exit(0);
		}
		
		
		// reading csv file
		System.out.println("reading CSV file");
		
		try {
			stockcsv.read();
		} catch (Exception e2) {
			System.out.println("can't read file");
			System.exit(0);
		}
		printdots();
		
		
		
		Thread.sleep(100);
		
		
		
		System.out.print("Creating Data Base");
		printdots();
		Thread.sleep(100);
	
		System.out.println("DataBase created");
		
		
		
		//define PORT to server
		int PORT = 2021;
		System.out.print("Creating server");
		
		// create object from Server class / client-server
		try {
			server = new Server(PORT , item_map , Integer.parseInt(args[0]));
			printdots();
			
			System.out.print("Starting Client-Server");
			//initiate server thread
			server.start();
			
			printdots();
			
			
			
			
			
		} catch (BindException e) {
			System.out.println("Server binding failure");
			Thread.sleep(3000);
			System.exit(0);
		}catch(Exception e)
		{
			System.out.println("Something wrong");
		}
		
		System.out.println("Client-Server created");
		
		
		
		
		//ui
		runui();
		
		

		System.out.print("creating broker");
		
		
		//broker thread
		//port
		int BRPORT = 2022;
		
		try {
			
			broker = new Broker(BRPORT, item_map);
			printdots();
			//initiate broker thread
			broker.start();
			
			System.out.println("broker is running");
		} catch (Exception e1) {
			System.out.println("Broker error.");
		
		}
		
		
		//stop main thread to prevent exiting program
		//wait until server and broker finish
		broker.join();
		server.join();
		
		
		try {
			server.close();
		} catch (IOException e) {
			System.out.println("Error");
		}
		System.out.println("server closing");
		printdots();
		
	}
	public static Server getServer() {
		return server;
	}

	public static void setServer(Server server) {
		Main.server = server;
	}

	public static Broker getBroker() {
		return broker;
	}

	public static ItemMap getItem_map() {
		return item_map;
	}

	public static void setItem_map(ItemMap item_map) {
		Main.item_map = item_map;
	}
	

	
	//separate thread for initiate UI
	public static void runui()
	{
		
		//UI
		try{
			
			new Thread(() -> {
				
				Application.launch(Ui.class);
			}).start();
			
		
			
		}catch(Exception e) {
			System.out.println("Ui error");
			
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
