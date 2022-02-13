package clientSrc;

import java.io.IOException;

public class ServerHandler extends Thread{

	private Connection con = null;
	
	public ServerHandler(Connection con) {
		
		this.con = con;
		
		
	}

	@Override
	public void run() {
		
		String inStr = null ;
		try {
			while((inStr = con.getRec()) != null)
			{
				System.out.print( ">" );

				if(inStr.equals("quit"))
				{
					System.exit(0);
				}
				
				System.out.println( inStr );
			}
		} catch (IOException e) {
			System.out.println("connection lost.");
			System.out.println("Enter any key");
			
		}
		con.close();
		
	}
	
	
	
	
}



