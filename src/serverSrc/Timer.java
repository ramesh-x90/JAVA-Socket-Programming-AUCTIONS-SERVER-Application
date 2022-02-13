package serverSrc;


//thread to count time


import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Timer extends Thread{
	
	//data formate
	DateTimeFormatter formatter;
	
	int i = 0 ;
	
	public Timer(DateTimeFormatter formatter) {
		
		this.formatter = formatter;
	}

	@Override
	public void run() 
	{	
		String currentTime;
		while(true)
		{
			
			currentTime = LocalTime.now().format(formatter);
			//update server local time from surrentTime
			Server.setServerTime(currentTime);
			try {
				Thread.sleep(1000);
				i++; // how many seconds passed from beginning of the server
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			}
			
			
			
		}
	}
	
	public int getPassedSec()
	{
		//return passed seconds
		return i;
		
	}

	
	
}
