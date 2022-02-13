package serverSrc;


//Main application is not a javaFx application
// this class used to connect with Main application as a UI
//run as a thread in Main method


import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Ui extends Application {


	@Override
	public void start(Stage stage)  {
		
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Root.fxml"));
			AnchorPane  root = loader.load();
			
			
			Scene scene = new Scene(root,800,600);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			root.requestFocus();
			
			stage.setScene(scene);
			stage.setTitle("Server");
			try {
				stage.getIcons().add(new Image("31986.png"));
			} catch (Exception e) {
			}
			
			stage.setResizable(false);
			stage.setFullScreen(false);
			stage.show();
			stage.setAlwaysOnTop(true);
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		
	}
	
	public void run()
	{
		launch();
	}

	public void close()
	{

		Platform.exit();
	}

	@Override
	public void stop() throws Exception {
		super.stop();
	}
	
	

	
}
