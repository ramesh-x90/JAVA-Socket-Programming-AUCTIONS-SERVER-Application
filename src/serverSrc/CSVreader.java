package serverSrc;


// class for read CSV file 
//and populate item map

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class CSVreader {
	
	//feed to file-reader
	private final String csvpath ;
	//
	
	//objects for file-reader , buffered-reader and ItemMap (HashMap) 
	private FileReader csvFile;
	private BufferedReader buffer;
	private ItemMap item_map;
	//
	
	public CSVreader(String filepath , ItemMap item_map){
		this.csvpath = filepath;
		this.item_map = item_map;
		
		try {
			csvFile = new FileReader(csvpath);
			
			//put all data into a buffer , going to read data from this buffer
			buffer = new BufferedReader(csvFile);
			
		} catch (FileNotFoundException e) {
			System.out.println("data file not found....");
		}
		
		
	}
	
	//read data and populate item map
	public void read() {
		
		try {
			if(!buffer.ready())
				System.out.println("File buffer error....");
		} catch (Exception e) {
			System.out.println("buffer is empty");
			
		}
		
			
		//create object of item class (ItemMap<Sring , ItemMap>)
		Item item = null; 
		//
		
		//skip header line
		String line;
		try {
			line = buffer.readLine();
		} catch (IOException e1) {
			System.out.println("can't read buffer");
		}
		
		try {
			
			while((line = buffer.readLine()) != null)
			{
				//split line string 
				String[] cellvalues = line.split(",");
				//
				
				//create object of item class from line string token
				//create object of item Item(symbol ,  symbol ,  price,  security,  profit)
				item = new Item(cellvalues[0] , Float.parseFloat(cellvalues[1]) , Integer.parseInt(cellvalues[2]) , Float.parseFloat(cellvalues[3]) );	
				//
				
				//put key and object inside HashMap
				item_map.put(cellvalues[0] , item);
				//
				
			}
			
			//close streams
			if(csvFile != null)
				csvFile.close();
				
			if(buffer != null)
				buffer.close();
				
			//
		} catch (IOException e) {
		}
		
		

		
	}

	
	
	
}
