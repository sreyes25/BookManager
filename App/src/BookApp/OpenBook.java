package BookApp;

import java.util.ArrayList;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class OpenBook {

	public Pane book(ArrayList<String> book) {
		BorderPane bp = new BorderPane();
		bp.setId("pane-light");
		TextArea ta = new TextArea();
		ta.setPrefSize(1100, 400);
		ta.setEditable(false);
		
		Label lb = new Label();
		lb.setId("label-title");
		String title ="";
		
		for(int i = 0; i < 4; i++) {
			title += book.get(i)+"\n";
		}
		if(!book.isEmpty()) {
		lb.setText(title);
		}
		else {
			lb.setText("Book Not Downloaded");
		}
		String b = "\n\n";
		for(int i = 4; i < book.size(); i++) {
			b += book.get(i)+"\n";
		}
		ta.setText(b);
		
		HBox hb0 = new HBox();
		hb0.setAlignment(Pos.CENTER);
		hb0.getChildren().add(lb);
		hb0.setPadding(new Insets(20));
		
		HBox hb1 = new HBox();
		hb1.setAlignment(Pos.CENTER);
		hb1.getChildren().add(ta);
		hb1.setPadding(new Insets(20));
		
		bp.setTop(hb0);
		bp.setCenter(hb1);
		
		Button goBack = new Button("Back to Book Manager");
		goBack.setId("big-button");
		HBox hb2 = new HBox();
		hb2.setAlignment(Pos.CENTER_RIGHT);
		hb2.setPadding(new Insets(20));
		
		hb2.getChildren().add(goBack);
		
		goBack.setOnAction(e -> {
			Stage stage = (Stage) goBack.getScene().getWindow();
			stage.close();
		});
		
		bp.setBottom(hb2);
		
		return bp;
	}

}
