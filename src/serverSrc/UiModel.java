package serverSrc;


//work as bride between Main application and UI (javaFX application)

public class UiModel {

	public static int timeleft;
	
	public static int hours;
	public static int minutes;
	public static int seconds;
	
	public static String timestring;
	public static boolean timetrigger = true;
	
	
	//get access to javaFx controller thread to Main application from controller class
	public static RootController rc = null;

	
	//Format time to show in UI
	
	public static String getTimeleft() {
		
		UiModel.hours = timeleft/3600;
		UiModel.minutes = timeleft/60 - hours*60;
		UiModel.seconds = timeleft - hours*3600 - minutes*60;
		
		timestring = Integer.toString(hours)+" h " + Integer.toString(minutes)+" m " + Integer.toString(seconds)+" s";
		return (timestring);
	}
	
	public static int getTimeLeftInt()
	{
		return timeleft;
	}

	public static void setTimeleft(int timeleft) {
		UiModel.timeleft = timeleft;
	}







	public UiModel()
	{
		
	}
	
	
	
	
	
	
	
	
}
