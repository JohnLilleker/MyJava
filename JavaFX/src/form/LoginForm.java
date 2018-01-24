package form;

// a lot of imports

// base class, this extends it
import javafx.application.Application;
// button event handler
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
// padding on grid
import javafx.geometry.Insets;
// locations i.e. CENTRE, BOTTOM_RIGHT etc
import javafx.geometry.Pos;
// The crux of the entire placement in JavaFX, I think synonymous with Swing ContentPane
import javafx.scene.Scene;
// elements in the form
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
// controlling the layout, grid style
import javafx.scene.layout.GridPane;
// a container for holing elements in a horizontal manner
import javafx.scene.layout.HBox;
// Heading of the form
import javafx.scene.text.Text;
// The main point from which the application is shown
import javafx.stage.Stage;

// need to extend Application
public class LoginForm extends Application {

	private final String USERNAME = "John";
	private final String PASSWORD = "7 Potatoes";
	
	@Override
	public void init() {
		// called after constructor and before start
		// optional override, default does nothing
		// useful for starting setup
	}
	
	@Override
	public void start(Stage primaryStage) {
				
		// The main 'entry point'
		// this needs an implementation
		
		// title, obviously
		primaryStage.setTitle("My second JavaFX app (with CSS)");
		
		// Grid layout, self explanatory
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		// gaps between elements
		grid.setHgap(10);
		grid.setVgap(10);
		// padding
		grid.setPadding(new Insets(25, 25, 25, 25));
		
		// Uneditable text field
		Text welcome = new Text("Welcome");
		// replaced with CSS
		// welcome.setFont(Font.font("Tahoma", FontWeight.BOLD, 20));
		// a CSS id for styling
		welcome.setId("welcome-text");
		// add(Node, columnIndex(x), rowIndex(y), colspan(width), rowspan(height))
		grid.add(welcome, 0, 0, 2, 1);
		
		// just an uneditable label
		Label uname = new Label("User name: ");
		// add(Node, columnIndex(x), rowIndex(y))
		grid.add(uname, 0, 1);
		
		// text input
		TextField unameInp = new TextField();
		grid.add(unameInp, 1, 1);
		
		Label pswrd = new Label("Password: ");
		grid.add(pswrd, 0, 2);
		
		// blanked out text input
		PasswordField pswrdInp = new PasswordField();
		grid.add(pswrdInp, 1, 2);
		
		// a button
		Button btn = new Button("Submit");
		// stores elements in a row, 10 means space between elements
		HBox hbox = new HBox(10);
		// on bottom right
		hbox.setAlignment(Pos.BOTTOM_RIGHT);
		hbox.getChildren().add(btn);
		grid.add(hbox, 1, 4);
		
		// text to show status, changed on click
		Text actionTarget = new Text();
		actionTarget.setId("actiontarget");
		grid.add(actionTarget, 1, 6, 1, 2);

		// Event handling
		// can replace with java 8 lambda
		btn.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				// Color is from javafx.scene.paint.Color
				// there is so much more than awt.Color
				// replaced by CSS
				// actionTarget.setFill(Color.FIREBRICK);
				
				String un = unameInp.getText();
				String pw = pswrdInp.getText();
								
				if (un.equals(USERNAME) && pw.equals(PASSWORD)) {
					actionTarget.setText("Sign in Successful\nWelcome John");
				}
				else {
					actionTarget.setText("Sign in Failed\nGo away");
				}
			}
		});
		
		
		Scene scene = new Scene(grid, 300, 275);
		primaryStage.setScene(scene);
		
		// css link
		// This is awesome
		scene.getStylesheets().add(LoginForm.class.getResource("style.css").toExternalForm());
		
		primaryStage.show();
		
	}
	
	@Override
	public void stop() {
		// called when either all windows closed, or Platform.exit()
		// optional override, default does nothing
		// useful to close everything
	}
	
	public static void main(String[] args) {
		Application.launch(args);
	}

}
