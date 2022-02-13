package serverSrc;




/*
 * 
 * Class to update UI
 * 
 * */




import java.net.URL;
import java.util.ResourceBundle;


import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;

public class RootController implements Initializable {

	@FXML
    private TableView<Item> table;

    @FXML
    private TableColumn<Item, String> symbol;

    @FXML
    private TableColumn<Item, Float> price;

    @FXML
    private TableColumn<Item, Float> profit;
    
    @FXML
    private TableColumn<Item, Float> bider;
    
    @FXML
    private TableColumn<?, ?> c0;
    
    @FXML
    private Label L1;

    @FXML
    private Label L2;
    
    @FXML
    private TextArea log;
    
    @FXML
    private Label time;
    
    @FXML
    private Label clients;
    
    @FXML
    private Label pubsub;
    
    
    public RootController()
    {
    	//give access to javaFx controller thread for Main application
    	UiModel.rc = this;
    }
    

    private static ObservableList<Item> data = FXCollections.observableArrayList(Main.getItem_map().values());
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		

		
		symbol.setCellValueFactory(new PropertyValueFactory<Item , String>("symbol"));
		price.setCellValueFactory(new PropertyValueFactory<Item , Float>("price"));
		profit.setCellValueFactory(new PropertyValueFactory<Item , Float>("profit"));
		bider.setCellValueFactory(new PropertyValueFactory<Item , Float>("maxpricebider"));
		
		
		symbol.setReorderable(false);
		price.setReorderable(false);
		profit.setReorderable(false);
		bider.setReorderable(false);
		c0.setReorderable(false);
		
		table.setItems(data);
		table.getSortOrder().add(symbol);
		
		log.setEditable(false);
		
		//server status
		new Thread(() -> {
				
				while(true)
				{
					try {
						if(Main.getServer().isAlive())
						{
							Platform.runLater(() -> 
								{
									L1.setText("Active");
									L1.setTextFill(Color.BLACK);
								});
						}else {
							Platform.runLater(() -> 
							{
								L1.setText("inactive");
								L1.setTextFill(Color.RED);
							});
							
						}
						
					}catch(Exception e) {
						
					}
					
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
		}).start();
		
		//Broker status
		new Thread(() -> {
				
				while(true)
				{
					
					try{
						if(Main.getBroker().isAlive())
						{
							Platform.runLater(() -> 
								{
									L2.setText("Active");
									L2.setTextFill(Color.BLACK);
								});
						}else {
							Platform.runLater(() -> 
							{
								L2.setText("inactive");
								L2.setTextFill(Color.RED);
							});
							
						}
					}catch(Exception e) {
						
					}

					
					
					
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
		}).start();
		
		
		//time status
		new Thread(() -> {
				while(UiModel.timetrigger)
				{
					Platform.runLater(() -> {
						
						//refresh table  sametime
						
						table.refresh();
						if(UiModel.getTimeLeftInt() < 180)
						{
							time.setTextFill(Color.RED);
						}else {
							time.setTextFill(Color.BLACK);
						}
						time.setText("Time left: "+UiModel.getTimeleft());
						
					
					}  );
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					};
				}
				Platform.runLater(() -> {
					
					time.setText("Time is up");
				}  );
				
		}).start();
		
		

		
		//client count
		new Thread(() -> {
			
			while(true)
			{
				
				int count = ClientHandler.getClientthreads().size();
				Platform.runLater(() -> {
					clients.setText("Active clients: "+Integer.toString(count));
				}  );
				
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				};
			}
			
		}).start();	
		
		
		//publiser-subscriber count
		new Thread(() -> {
			
			while(true)
			{
				
				int count = PubSubHandler.getPubsubthreads().size();
				Platform.runLater(() -> {
					pubsub.setText("publishers & subscribers: "+Integer.toString(count));
				}  );
				
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				};
			}
			
		}).start();	

		
	}
    
    
    
	
	
	public void setlog(String str)
	{
		
		
		String time = Server.serverTime;
		
		
		Platform.runLater(() -> {
			if(log.getText() == null) {
				log.setText("[" + time + "]: " +str);
			}else {
				log.setText(log.getText()+"\n"+"[" + time + "]: "+str);
			}
			
			
		});
		
		
		
		

			
	}
	
	
	
	public void refrashTable(String str) {

		
		
		new Thread(() -> {
			
			data.forEach(d -> {
				
				if(d.getSymbol().equals(str))
				{
					
					Platform.runLater(() -> {
						table.getSelectionModel().select(d);
						table.scrollTo(d);
						
					});
					
					
				}
				
			});
			
			
			Platform.runLater(() -> {
				table.refresh();
				
			});
			
		}).start();
		
		
		

	}
	public void refrashTable() {

		
		
		new Thread(() -> {
			
			Platform.runLater(() -> {
				table.refresh();
				
			});
			
		}).start();
		
		
		

	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
