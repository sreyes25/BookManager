package BookApp;
import java.util.Optional;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Login extends Application {
	public static final double WIDTH = 400;
	public static final double HEIGHT = 550;
	public ObservableList<Node> pnodes;
	// public static final String MainS = "myCss.css";

	@Override
	public void start(Stage stage) throws Exception {
		// TODO Auto-generated method stub
 
		Pane root = stackPane();

		Scene sc1 = new Scene(root, WIDTH, HEIGHT);

		sc1.getStylesheets().add("myLoginCss.css");
		
		stage.setScene(sc1);
		stage.setTitle("Login");
		stage.show();

	}

	public Pane stackPane() {

		BorderPane bp = new BorderPane();
		StackPane sp = new StackPane();
		HBox hb = new HBox();

		hb.setAlignment(Pos.CENTER);
		hb.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

		Button btnSignIn = new Button("Sign In");
		btnSignIn.setPrefSize(WIDTH / 2, 70);
		Button btnSignUp = new Button("Sign Up");
		btnSignUp.setPrefSize(WIDTH / 2, 70);

		Pane loginPane = loginGUI();
		// loginPane.setStyle("-fx-background-color: rgba(58,72,91, 1.0);");
		loginPane.isResizable();
		Pane signUp = signUp();
		//signUp.setStyle("-fx-background-color: rgba(58,72,91, 1.0);");

		hb.getChildren().addAll(btnSignIn, btnSignUp);
		sp.getChildren().addAll(signUp, loginPane);

		pnodes = sp.getChildren();

		bp.setTop(hb);
		bp.setCenter(sp);

		btnSignIn.setOnAction(e -> {
			ObservableList<Node> nodes = sp.getChildren();

			if (nodes.size() > 1) {
				Node node = nodes.get(nodes.size() - 1);
				if (node == signUp) {
					node.toBack();
				}
			}
		});

		btnSignUp.setOnAction(e -> {
			ObservableList<Node> nodes = sp.getChildren();

			if (nodes.size() > 1) {
				Node node = nodes.get(nodes.size() - 1);
				if (node == loginPane) {
					node.toBack();
				}
			}
		});

		return bp;
	}

	public Pane signUp() {

		VBox vb = new VBox();
		vb.setId("vbox");
		
		HBox header = new HBox();
		header.setPadding(new Insets(60,0, 40, 0));
		header.setAlignment(Pos.CENTER);

		Label lb0 = new Label("Create an Account");
		lb0.setId("label-title");
		header.getChildren().add(lb0);
		
		GridPane gp = new GridPane();
		gp.setId("grid");
		gp.setPadding(new Insets(20));
		gp.setAlignment(Pos.CENTER);
		gp.setHgap(8);
		gp.setVgap(8);

		
		TextField tf = new TextField();
		tf.setPromptText("Enter Username");
		
		PasswordField pf = new PasswordField();
		pf.setPromptText("Enter Password");
		
		PasswordField pcf = new PasswordField();
		pcf.setPromptText("Confirm Password");

		Label passReq = new Label("Password must contain: "
				+ "\n-At least 6 characters\n"
				+ "-At least 1 uppercase\n");
		passReq.setId("");
		HBox hb0 = new HBox();
		hb0.setAlignment(Pos.CENTER);
		hb0.getChildren().add(passReq);
		
		gp.add(tf, 1, 0);

		
		gp.add(pf, 1, 2);

	
		gp.add(pcf, 1, 4);

		HBox hb = new HBox();
		hb.setAlignment(Pos.CENTER);
		hb.setSpacing(10);
		
		hb.setPadding(new Insets(25));

		Button createLogin = new Button("Create User");
		createLogin.setId("button-3");
		createLogin.setDisable(true);
		hb.getChildren().addAll(createLogin);

		createLogin.setOnAction(e -> {
			String user = tf.getText();
			String pass = pf.getText();
			String confirmPass = pcf.getText();
			
			if(!pass.equals(confirmPass)) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setContentText(
						"Renter password ");
				alert.setHeaderText("Password does not match");
				alert.showAndWait();
				return;
			}

			if (pass.charAt(0) < 91 && DataCenter.getInstance().addUserIO(user, pass, confirmPass)) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setContentText("Sign Up Successful");
				alert.showAndWait();
			} else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setContentText(
						"Username is already taken or password do not fit criteria: min 6 characters and 1 uppercase character");
				alert.setHeaderText("Sign Up Failed");
				alert.showAndWait();
			}
		});

		tf.setOnKeyTyped(e -> {
			String user = tf.getText();
			String pass = pf.getText();
			createLogin.setDisable(!validateLogin(user, pass));
		});

		pf.setOnKeyPressed(e -> {
			String user = tf.getText();
			String pass = pf.getText();
			createLogin.setDisable(!validateLogin(user, pass));
		});	
		
		vb.getChildren().addAll(header, gp, hb, hb0);
		

		return vb;
	}

	public Pane loginGUI() {

		VBox vb = new VBox();
		vb.setId("vbox");
		
		HBox welcome = new HBox();
		welcome.setPadding(new Insets(60,0, 40, 0));
		welcome.setAlignment(Pos.CENTER);

		Label lb0 = new Label("BookManager");
		lb0.setId("label-title");
		welcome.getChildren().add(lb0);
		
		GridPane gp = new GridPane();
		gp.setId("grid");
		gp.setPadding(new Insets(20));
		gp.setAlignment(Pos.CENTER);
		gp.setHgap(30);
		gp.setVgap(5);

		
		TextField tf = new TextField();
		tf.setPromptText("Username");
		
		PasswordField pf = new PasswordField();
		pf.setPromptText("Password");
		gp.add(tf, 0, 1);

		gp.add(pf, 0, 3);

		HBox hb = new HBox();
		hb.setAlignment(Pos.CENTER);
		hb.setSpacing(10);
		hb.setPadding(new Insets(25,0,2,0));

		Button admin = new Button("Admin");
		admin.setId("button-2");
		/////////////////////////
		admin.setDisable(false);
		////////////////////////
		Button login = new Button("Login");
		login.setId("button-3");
		Button close = new Button("Close");
		close.setId("button-2");
		login.setDisable(true);
		hb.getChildren().addAll(admin, close);
		

		admin.setOnAction(e -> {
			if (DataCenter.getInstance().findUser("Sergio", "TickleTib22")) {

				Stage stage = (Stage) login.getScene().getWindow();
				DataCenter.getInstance().loadUserDataIO("Sergio", "TickleTib22");
				BookApp x = new BookApp();
				Pane app = x.app();
				Scene sc2 = new Scene(app, 1200, 850);
				sc2.getStylesheets().add("myAppCss.css");
				stage.setScene(sc2);
				stage.setTitle("Book Manager");
				stage.centerOnScreen();

				stage.setOnCloseRequest(ae -> {
					// DataCenter.getInstance().save();
				});
				stage.show();

			}
		});

		pf.setOnKeyPressed(event -> {

			if (event.getCode() == KeyCode.ENTER) {

				String user = tf.getText();
				String pass = pf.getText();

				if (DataCenter.getInstance().findUser(user, pass)) {
					DataCenter.getInstance().loadUserDataIO(user, pass);
					Stage stage = (Stage) login.getScene().getWindow();
					BookApp x = new BookApp();
					Pane app = x.app();
					Scene sc2 = new Scene(app, 1200, 850);
					sc2.getStylesheets().add("myAppCss.css");
					stage.setScene(sc2);
					stage.setTitle("Book Manager");
					stage.centerOnScreen();

					stage.setOnCloseRequest(ae -> {
						// DataCenter.getInstance().save();
					});
					stage.show();

				} else {
					Alert alert = new Alert(AlertType.ERROR, "default Dialog", ButtonType.CLOSE);
					alert.getDialogPane().setMinHeight(180);
					alert.setHeaderText("Login Failed");
					alert.setContentText("Cannot Find the User");
					ButtonType signUpButtonType = new ButtonType("Sign Up");
					alert.getButtonTypes().addAll(signUpButtonType);

					Optional<ButtonType> result = alert.showAndWait();
					if (result.get() == signUpButtonType) {

						ObservableList<Node> nodes = pnodes;

						if (nodes.size() > 1) {
							Node node = nodes.get(nodes.size() - 1);

							node.toBack();
						}
					}

				}
			}
		});

		login.setOnAction(e -> {

			String user = tf.getText();
			String pass = pf.getText();

			if (DataCenter.getInstance().findUser(user, pass)) {
				DataCenter.getInstance().loadUserDataIO(user, pass);
				Stage stage = (Stage) login.getScene().getWindow();
				BookApp x = new BookApp();
				Pane app = x.app();
				Scene sc2 = new Scene(app, 1200, 850);
				sc2.getStylesheets().add("myAppCss.css");
				stage.setScene(sc2);
				stage.setTitle("Book Manager");
				stage.centerOnScreen();

				stage.setOnCloseRequest(ae -> {
					// DataCenter.getInstance().save();
				});
				stage.show();

			} else {
				Alert alert = new Alert(AlertType.ERROR, "default Dialog", ButtonType.CLOSE);
				alert.getDialogPane().setMinHeight(180);
				alert.setHeaderText("Login Failed");
				alert.setContentText("Cannot Find the User");
				ButtonType signUpButtonType = new ButtonType("Sign Up");
				alert.getButtonTypes().addAll(signUpButtonType);

				Optional<ButtonType> result = alert.showAndWait();
				if (result.get() == signUpButtonType) {

					ObservableList<Node> nodes = pnodes;

					if (nodes.size() > 1) {
						Node node = nodes.get(nodes.size() - 1);

						node.toBack();
					}
				}

			}

		});

		close.setOnAction(e -> {
			Stage stage = (Stage) close.getScene().getWindow();
			stage.close();
		});

		// KeyEvents

		tf.setOnKeyTyped(e -> {
			String user = tf.getText();
			String pass = pf.getText();
			login.setDisable(!validateLogin(user, pass));
		});

		pf.setOnKeyPressed(e -> {
			String user = tf.getText();
			String pass = pf.getText();
			login.setDisable(!validateLogin(user, pass));
		});
		
		HBox hb1 = new HBox();
		hb1.setAlignment(Pos.CENTER);
		hb1.getChildren().add(login);

		vb.getChildren().addAll(welcome, gp, hb, hb1);
	
		

		return vb;
	}

	private boolean validateLogin(String user, String pass) {
		return user.length() >= 3 && pass.length() >= 5;
	}

	public static void main(String[] args) {
		Application.launch(args);
	}

}
