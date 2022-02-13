package serverSrc;



import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/*
 * 
 * class for track time related actions 
 * such as extending time
 * Class for broadcast server decisions to clients
 * 
 * */


import java.io.PrintWriter;
import java.util.ArrayList;




public class ServerHandler extends Thread {

	
	

	private int deadline ;
	private Timer timer;
	private  ItemMap item_map = null;
	boolean tc = true;
	
	private ArrayList<PrintWriter> pws;
	
	public ServerHandler(int t, Timer timer, ArrayList<PrintWriter> pws ) 
	{
		this.deadline = t;
		this.timer = timer;
		this.pws = pws;
	}
	
	
	
	public ServerHandler(int t, Timer timer, ArrayList<PrintWriter> pws, ItemMap item_map) {
		this.deadline = t;
		this.timer = timer;
		this.pws = pws;
		this.item_map = item_map;
	}



	@Override
	public void run() 
	{
		
		int t = 0;
		
		//if passed seconds is less than time (this.time = total seconds server supposed to continue it's service)
		//passed seconds = totals seconds spend from the initiation of the server
		//loop will terminate after t == total seconds server supposed to continue it's service
		while((t=timer.getPassedSec()) < deadline)
		{
				
			try
			{
				UiModel.setTimeleft(deadline-timer.getPassedSec());
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			//broadcasting message to all clients when left time is 30%
			if(t >= deadline*0.7 && tc) 
			{
				tellToAll(Float.toString(  (float)(Math.round(deadline*0.3*100)/100.0)	 ) + "sec left");
				tc = false;
			}
			
			
		}
		
		
		
		createlog();
		
		
		
		
		UiModel.setTimeleft(0);
		UiModel.timetrigger = false;
		tellToAll("biding time has end");
		
	}
	
	//creating a CSV file with biding results
	private void createlog() {
		
		
		File file = new File("biding_results.csv");
		
		if(!file.exists())
		{
			try {
				file.createNewFile();
			} catch (IOException e) {
				System.out.println("log file create falied");
			}
		}
		
		FileWriter fWriter = null;
		try {
			fWriter = new FileWriter(file);
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		try {
			fWriter.write("item,current profit,max_bid,bid_winner\n");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		
		for(Item item : item_map.values())
		{
			if(!item.getMaxpricebider().equals(""))
			{
				try {
					fWriter.write(item.getSymbol() +","+ item.getProfit() +","+ item.getPrice() +","+ item.getMaxpricebider() + "\n");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			
		}
		
		try {
			fWriter.close();
			System.out.println("biding results file created");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}



	private void tellToAll(String str)
	{
		for(PrintWriter apw : pws)
		{
			apw.println("[SERVER]"+ "[ time:"+Server.getServerTime()+"] >>" + str);
			apw.flush();
			
		}

		System.out.println("[SERVER]"+ "[ time:"+Server.getServerTime()+"] >>" + str);
	}
		
	public void extendDeadline (int time)
	{
		//extends time that server
		this.deadline += time;
		
		//broadcast when time has extends
		tellToAll("biding time extended by " + time + "sec");
		tc = true;
	}
	
	
	public int getDeadline()
	{
		//return time that server suppose to provide biding service
		return deadline;
	}



		
}