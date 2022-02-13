package clientSrc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;



public class Client {
	

	private static Connection con = null;
	private static String ip = "localhost";
	private static int PORT = 2021;
	private static ServerHandler serverhandler = null;

	public static void main(String[] args){
		
		if(args.length == 2)
		{
			ip = args[0];
			PORT = Integer.parseInt(args[1]);
		}
		
		
		
		connect();
		
		BufferedReader keyinput = new BufferedReader(new InputStreamReader(System.in));
		String input = null;
		
		while(true)
		{
			try {
				input = keyinput.readLine();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			if(input.equals("clear"))
			{
				clearcli();
				continue;
			}
			
			
			try {
				con.send(input);
			} catch (Exception e) {
				connect();
				continue;
			}
			
		}

		

	}

	public static void printdots() {
		
		for(int i = 0 ; i < 6 ; i++)
		{
			System.out.print(".");
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		
	}
	
	public static void connect()
	{
		clearcli();
		System.out.print("connecting");
		Thread t = new Thread(new Runnable() {
			
			@Override
			public void run() {
				con = new Connection(ip , PORT);
				
			}
		});
		
		t.start();
		
		int time = 0;
		
		while(t.isAlive())
		{
			time++;
			printdots();
			clearcli();
			System.out.print("connecting");
			
			if(time == 10)
			{
				clearcli();
				System.out.println("time out.");
				System.exit(0);
			}
			
		}
			
		
		clearcli();
		
		
		System.out.println("connected to the server");
		serverhandler = new ServerHandler(con);
		serverhandler.start();
		
	}


	public static void clearcli()
	{
		try {
			if (System.getProperty("os.name").contains("Windows"))
				new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
			else
				Runtime.getRuntime().exec("clear");
		} catch (Exception x) {} 
	}

}
