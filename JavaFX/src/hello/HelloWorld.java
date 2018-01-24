package hello;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class HelloWorld extends Application {

	@Override
	public void start(Stage stage) {
		// TODO Auto-generated method stub
		Button btn = new Button();
		btn.setText("Say 'Hello World'");
		
		// LAMBDA!!!
		btn.setOnAction((ActionEvent e) -> {
			System.out.println("Hello World!");
		});
		
		StackPane root = new StackPane();
		root.getChildren().add(btn);
		
		Scene scene = new Scene(root, 300, 250);
		
		stage.setTitle("My first JavaFX app");
		stage.setScene(scene);
		stage.show();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Application.launch(args);
	}

}
