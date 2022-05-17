package BookApp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Optional;
import Book.Word;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class BookApp {
	private ObservableList<String> lvData = FXCollections.observableArrayList();
	private ObservableList<MyTableRow> tvData = FXCollections.observableArrayList();
	private ObservableList<String> lvBookData = FXCollections.observableArrayList();
	private ObservableList<PieChart.Data> list = FXCollections.observableArrayList();
	private ArrayList<BookList> bookCollection;
	private ArrayList<String> bookDetails;
	private StackPane sp2;
	private User user;
	private ImageView imageView;
	private Label lb1;
	private Label lb2;
	private Button statBtn;
	private BorderPane borderPane;

	public Pane app() {
		BorderPane bp = new BorderPane();
		bp.setId("pane-light");
		bp.setLeft(leftPane());
		bp.setCenter(stackPane());
		return bp;
	}

	public Pane stackPane() {
		BorderPane bp = new BorderPane();
		StackPane sp = new StackPane();

		VBox vb = new VBox();
		vb.setId("pane-light");

		HBox hb = new HBox();
		hb.setSpacing(20);
		hb.setAlignment(Pos.CENTER);

		HBox hb1 = new HBox();
		hb1.setMaxHeight(5);
		hb1.setAlignment(Pos.CENTER);

		lb1 = new Label(user.getBookList().get(0).getPath());
		lb1.setId("label-title");

		lb2 = new Label("Mastering JavaFX 10");
		lb2.setVisible(false);

		lb1.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> ov, String t, String t1) {
				tvData.clear();
				tableView();
			}
		});

		hb1.getChildren().addAll(lb1);

		Button booksBtn = new Button("Book List");
		statBtn = new Button("Book List Data");
		statBtn.setDisable(true);
		Region spacer = new Region();
		HBox.setHgrow(spacer, Priority.ALWAYS);
		Button logOut = new Button("LogOut");

		Pane bookPane = tableView();
		Pane statInfo = buildPieChart();

		hb.getChildren().addAll(booksBtn, statBtn, lb2, spacer, logOut);
		vb.getChildren().addAll(hb, hb1);
		sp.getChildren().addAll(statInfo, bookPane);

		// pfp
		sp2 = new StackPane();
		sp2.setVisible(false);
		sp2.getChildren().add(pfPane());

		// Buttons for stack
		booksBtn.setOnAction(e -> {

			ObservableList<Node> nodes = sp.getChildren();
			if (nodes.size() > 1) {
				Node node = nodes.get(nodes.size() - 1);
				if (node == statInfo) {
					node.toBack();
				}
			}
		});

		statBtn.setOnAction(e -> {

			list.clear();
			borderPane.setCenter(buildPieChart());
			ObservableList<Node> nodes = sp.getChildren();

			if (nodes.size() > 1) {
				Node node = nodes.get(nodes.size() - 1);
				if (node == bookPane) {
					node.toBack();
				}
			}
		}); 

		logOut.setOnAction(e -> {
			Stage stage = (Stage) logOut.getScene().getWindow();

			tvData.clear();
			lvBookData.clear();
			bookDetails.clear();

			stage.close();

			Stage stage1 = (Stage) logOut.getScene().getWindow();
			Login x = new Login();
			Pane root = x.stackPane();
			Scene sc2 = new Scene(root, 400, 550);
			sc2.getStylesheets().add("myLoginCss.css");
			stage1.setScene(sc2);
			stage1.setTitle("Login");
			stage1.setOnCloseRequest(ae -> {
				// DataCenter.getInstance().save();
			});
			stage1.show();

		});

		bp.setTop(vb);
		bp.setCenter(sp);
		bp.setBottom(sp2);

		return bp;
	}

	public ListView<String> listViewBook() {

		ListView<String> lv = new ListView<>();
		lv.setId("list-view");
		lv.setMaxHeight(190);
		lv.setPrefWidth(350);
		bookDetails = DataCenter.getInstance().getBookDetails();

		for (int i = 0; i < bookDetails.size(); i++) {
			String item = bookDetails.get(i);
			lvBookData.add(item);
		}

		lv.setItems(lvBookData);

		return lv;

	}

	public Pane listView() {

		BorderPane bp = new BorderPane();

		ListView<String> lv = new ListView<>();
		lv.setId("list-view");
		bookCollection = DataCenter.getInstance().getCollection();

		for (int i = 0; i < bookCollection.size(); i++) {
			String item = bookCollection.get(i).getPath();
			lvData.add(item);
		}

		lv.setItems(lvData);
		bp.setCenter(lv);

		VBox vb1 = new VBox();
		vb1.setId("vbox-m-light");
		vb1.setSpacing(10);
		vb1.setPadding(new Insets(30));
		vb1.setAlignment(Pos.CENTER);
		vb1.setPrefWidth(500);

		HBox hb = new HBox();
		hb.setPadding(new Insets(15, 0, 5, 0));
		hb.setSpacing(10);
		hb.setAlignment(Pos.CENTER);

		Button newColl = new Button("+  New Collection");
		newColl.setId("button-2");
		Button deleteColl = new Button("Delete Collection");
		deleteColl.setDisable(true);
		deleteColl.setId("button-2");
		hb.getChildren().addAll(newColl, deleteColl);

		HBox hb2 = new HBox();
		hb2.setAlignment(Pos.CENTER_LEFT);
		hb2.setSpacing(20);

		TextField tf = new TextField();
		tf.setId("text-field-light");
		tf.setPromptText("Collection Name");
		Button add = new Button("Add");
		add.setId("button-2");
		add.setVisible(false);
		add.setDisable(false);
		tf.setVisible(false);

		hb2.getChildren().addAll(tf, add);
		vb1.getChildren().addAll(hb, hb2);
		bp.setBottom(vb1);

		lv.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				lb1.setText(newValue);
				deleteColl.setDisable(false);
			}
		});

		newColl.setOnMouseClicked(e -> {
			// count++;
			if (tf.isVisible()) {
				tf.setVisible(false);
				add.setVisible(false);
				add.setDisable(true);
				tf.clear();
			} else
				tf.setVisible(true);
				tf.setOnKeyTyped(onKey -> {
				add.setVisible(true);
				
				add.setDisable(tf.getText().length() < 1);

				add.setOnAction(ae -> {
					if (!(tf.getText().equals("")) && DataCenter.getInstance().addNewCollectionIO(tf.getText())) {

						String item = tf.getText();
						lvData.add(item);

						deleteColl.setDisable(false);
						add.setVisible(false);
						add.setDisable(false);
						tf.setVisible(false);
						tf.clear();
					}
				});

			});

		});
		
		deleteColl.setOnAction(e -> {
			if (lvData.size() <= 1) {
				deleteColl.setDisable(true);
				return;
			}
			
			else if (!lv.getSelectionModel().getSelectedItems().toString().equals("[]")) {
				Alert alert = new Alert(AlertType.WARNING, "default Dialog", ButtonType.YES);
				alert.getDialogPane().setMinHeight(180);
				alert.setHeaderText("Delete Collection?");
				String item = lv.getSelectionModel().getSelectedItems().toString();
				item = item.substring(1, item.length() - 1);
				alert.setContentText("Are you sure you want to delete:  " + item + "\n\nAll books from " + item
						+ " will be deleted.");
				ButtonType no = new ButtonType("No");
				alert.getButtonTypes().addAll(no);
				Optional<ButtonType> result = alert.showAndWait();

				if (!(result.get() == no)) {
					if (lvData.size() <= 1) {
						deleteColl.setDisable(true);
					}
					if (DataCenter.getInstance().deleteCollectionIO(lb1.getText())) {
						lv.getItems().removeAll(lv.getSelectionModel().getSelectedItems());
						lb1.setText(user.getBookList().get(0).getPath());
					}

				}
			}
		});

		return bp;

	}

	public Pane pfPane() {
		InputStream stream = null;

		HBox hb0 = new HBox();
		hb0.setStyle(" -fx-background-color: RGB(39, 39, 39);");
		hb0.setSpacing(30);
		hb0.setPrefWidth(350);
		hb0.setPrefHeight(75);
		hb0.setAlignment(Pos.CENTER);
		File d = new File(System.getProperty("user.dir")); 
		File pfpFile = new File(d.toString()+"/bin/pfp/");
		ArrayList<String> pf = Util.getFiles(pfpFile.toString());

		for (int i = 0; i < pf.size(); i++) {
			try {
				stream = new FileInputStream(pf.get(i));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Image image = new Image(stream);
			ImageView imageView1 = new ImageView();
			imageView1.setImage(image);
			imageView1.setId(String.valueOf(i));
			imageView1.setX(15);
			imageView1.setY(15);
			imageView1.setFitWidth(50);
			imageView1.setPreserveRatio(true);
			imageView1.setPickOnBounds(true);

			imageView1.setOnMouseClicked(e -> {

				imageView.setImage(imageView1.getImage());
				DataCenter.getInstance().setUserPfp(imageView1.getId());
				sp2.setVisible(false);

			});

			hb0.getChildren().add(imageView1);
		}
		return hb0;
	}

	public Pane leftPane() {
		BorderPane bp = new BorderPane();
		bp.setId("pane-dark");
		// Top
		VBox vb1 = new VBox();
		vb1.setAlignment(Pos.CENTER);
		vb1.setPrefWidth(350);

		HBox hb0 = new HBox();
		hb0.setPrefWidth(350);
		hb0.setAlignment(Pos.CENTER_LEFT);
		hb0.setPadding(new Insets(10, 10, 10, 0));

		TextField tf = new TextField();
		tf.setPromptText(" Search for Book Online");
		tf.setId("text-field-light");
		tf.setPrefWidth(290);
		hb0.getChildren().add(tf);
		
		HBox hb = new HBox();
		Button addBook = new Button("Add");
		addBook.setId("button-2");
		addBook.setDisable(true);

		tf.setOnKeyPressed(event -> {
			if (event.getCode() == KeyCode.ENTER) {

				if (!bookDetails.isEmpty()) {
					bookDetails.clear();
				}

				try {
					DataCenter.getInstance().webSrcapePrice(tf.getText());
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				lvBookData.clear();
				listViewBook();
				addBook.setDisable(false);
			}
		});

		HBox bDetails = new HBox();
		bDetails.setPrefWidth(350);
		bDetails.setAlignment(Pos.CENTER);

		bDetails.getChildren().add(listViewBook());

		HBox hb1 = new HBox();
		hb1.setPrefWidth(350);
		hb1.setAlignment(Pos.CENTER_LEFT);

		Label lb1 = new Label("Book Info");
		lb1.setId("label-title-light");
		
		hb.getChildren().add(addBook);

		hb1.getChildren().add(lb1);
		vb1.getChildren().addAll(hb0, hb1, bDetails, addBook);
		bp.setTop(vb1);

		// Center
		VBox vb2 = new VBox();
		vb2.setAlignment(Pos.CENTER);
		vb2.setPrefSize(350, 800);

		HBox hb2 = new HBox();
		hb2.setAlignment(Pos.CENTER_LEFT);
		Label lb2 = new Label("My Collections");
		lb2.setId("label-title-light");
		hb2.getChildren().add(lb2);
		vb2.getChildren().addAll(hb2, listView());
		
		bp.setCenter(vb2);

		// Bottom
		user = DataCenter.getInstance().getUserInfo();
		InputStream stream = null;
		File d = new File(System.getProperty("user.dir")); 
		File pfpFile = new File(d.toString()+"/bin/pfp/");
		ArrayList<String> pf = Util.getFiles(pfpFile.toString());

		try {
			stream = new FileInputStream(pf.get(DataCenter.getInstance().getUserInfo().getPfp()));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Image image = new Image(stream);
		// Creating the image view
		imageView = new ImageView();
		// Setting image to the image view
		imageView.setImage(image);
		// imageView.setId("image-view");
		// Setting the image view parameters
		imageView.setX(10);
		imageView.setY(10);
		imageView.setFitWidth(60);
		imageView.setPreserveRatio(true);
		imageView.setPickOnBounds(true); // allows click on transparent areas

		// ON/OFF PFP
		imageView.setOnMouseClicked(e -> {

			if (!sp2.isVisible()) {
				sp2.setVisible(true);
			} else if (sp2.isVisible()) {
				sp2.setVisible(false);
			} else {
				imageView.setOnMouseClicked(ae -> {
					sp2.setVisible(true);
				});
			}
		});

		Group root = new Group(imageView);
		VBox vb3 = new VBox();
		vb3.setId("vbox-m-light");
		vb3.setAlignment(Pos.CENTER);
		vb3.setPrefWidth(350);

		HBox hb3 = new HBox();
		hb3.setSpacing(15);
		hb3.setPrefWidth(350);
		hb3.setAlignment(Pos.CENTER_LEFT);

		Label lb3 = new Label(user.getUsername());
		lb3.setId("label-title-light");
		hb3.getChildren().addAll(root, lb3);
		vb3.getChildren().add(hb3);

		bp.setBottom(vb3);
		return bp;
	}

	@SuppressWarnings("unchecked")
	public Pane tableView() {

		VBox vb = new VBox();
		vb.setId("pane-light");
		vb.setAlignment(Pos.CENTER);

		HBox hb0 = new HBox();
		hb0.setAlignment(Pos.CENTER_LEFT);
		hb0.setSpacing(20);
		hb0.setPadding(new Insets(0, 0, 5, 0));
		Label searchBookLbl = new Label(" Serach For Book");
		searchBookLbl.setId("label-reg");
		TextField searchBook = new TextField();
		
		searchBook.setPromptText("Advanced Search");
		searchBook.setPrefWidth(500);

		hb0.getChildren().addAll(searchBookLbl, searchBook);

		ArrayList<BookList> bl = DataCenter.getInstance().getUserInfo().getBookList();
		// Table view
		TableView<MyTableRow> tv = new TableView<>();
		tv.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		tv.setEditable(true);
		tv.setPrefHeight(600);

		// table colunm
		TableColumn<MyTableRow, String> col0 = new TableColumn<>("Author");
		col0.setCellValueFactory(new PropertyValueFactory<>("author"));
		TableColumn<MyTableRow, String> col1 = new TableColumn<>("Title");
		col1.setCellValueFactory(new PropertyValueFactory<>("title"));
		TableColumn<MyTableRow, String> col2 = new TableColumn<>("ISBN");
		col2.setCellValueFactory(new PropertyValueFactory<>("isbn"));
		TableColumn<MyTableRow, String> col3 = new TableColumn<>("Genre");
		col3.setCellValueFactory(new PropertyValueFactory<>("genre"));

		tv.getColumns().addAll(col0, col1, col2, col3);
		tv.setItems(tvData);

		BookList books = new BookList();
		// existing bookList *One book list first///
		for (int j = 0; j < bl.size(); j++) {
			if (bl.get(j).getPath().equals(lb1.getText())) {
				books = bl.get(j);
				break;
			}
		}

		for (int i = 0; i < books.size(); i++) {

			tvData.add(new MyTableRow(books.getBook(i).getAuthor(), books.getBook(i).getTitle(),
					books.getBook(i).getIsbn(), books.getBook(i).getGenre()));
		}

		FilteredList<MyTableRow> filteredData = new FilteredList<MyTableRow>(tvData, b -> true);

		searchBook.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredData.setPredicate(myTableRow -> {

				if (newValue == null || newValue.isEmpty()) {
					return true;
				}
				String toLowerCase = newValue.toLowerCase();
				if (myTableRow.getIsbn().toLowerCase().indexOf(toLowerCase) != -1) {
					return true;
				} else if (myTableRow.getAuthor().toLowerCase().indexOf(toLowerCase) != -1) {
					return true;
				} else if (myTableRow.getTitle().toLowerCase().indexOf(toLowerCase) != -1) {
					return true;
				} else if (myTableRow.getGenre().toLowerCase().indexOf(toLowerCase) != -1) {
					return true;
				} else
					return false;
			});
		});

		SortedList<MyTableRow> sortedData = new SortedList<>(filteredData);
		sortedData.comparatorProperty().bind(tv.comparatorProperty());
		tv.setItems(sortedData);

		HBox hb = new HBox();
		hb.setAlignment(Pos.CENTER);
		hb.setSpacing(20);

		Button add = new Button("Add");
		Button delete = new Button("Delete");
		Button addBook = new Button("Add Book");
		addBook.setVisible(false);

		hb.getChildren().addAll(add, delete);

		Button bookInfo = new Button("Book Info");
		bookInfo.setVisible(false);
		Button openBook = new Button("Read");
		openBook.setId("small-button");
		openBook.setVisible(false);
		openBook.setDisable(true);

		HBox hb1 = new HBox();
		hb1.setAlignment(Pos.CENTER);
		hb1.setSpacing(20);

		hb1.getChildren().addAll(bookInfo, openBook);

		//// Adding new Book
		GridPane gp = new GridPane();
		gp.setVisible(false);
		gp.setAlignment(Pos.CENTER);
		gp.setHgap(45);
		gp.setVgap(10);
		Label author = new Label("Author");
		TextField authorTf = new TextField();
		
		Label title = new Label("Title");
		TextField titleTf = new TextField();
	
		Label isbn = new Label("ISBN");
		TextField isbnTf = new TextField();
		
		Label genre = new Label("Genre");
		TextField genreTf = new TextField();
	

		gp.add(author, 0, 0);
		gp.add(authorTf, 1, 0);

		gp.add(title, 2, 0);
		gp.add(titleTf, 3, 0);

		gp.add(isbn, 0, 1);
		gp.add(isbnTf, 1, 1);

		gp.add(genre, 2, 1);
		gp.add(genreTf, 3, 1);
		////

		BorderPane bp = new BorderPane();
		Button spacer = new Button("spacer");
		spacer.setVisible(false);
		spacer.setId("big-button");

		bp.setLeft(spacer);
		bp.setCenter(hb);
		bp.setRight(hb1);

		vb.getChildren().addAll(hb0, tv, bp, gp, addBook);

		tv.setRowFactory(tV -> {

			TableRow<MyTableRow> row = new TableRow<>();

			row.setOnMouseClicked(event -> {
				MyTableRow clickedRow = row.getItem();
				statBtn.setDisable(true);
				bookInfo.setVisible(false);
				openBook.setVisible(false);
				openBook.setDisable(true);

				if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {

					bookInfo.setVisible(true);
					openBook.setVisible(true);
	
					lb2.setText(clickedRow.getTitle());
					
					if(DataCenter.getInstance().analyzeBook(lb2.getText())) {
						statBtn.setDisable(false);
						openBook.setDisable(false);
					}

					bookInfo.setOnAction(e -> {

						if (!bookDetails.isEmpty()) {
							bookDetails.clear();
						}
						try {
							DataCenter.getInstance().webSrcapePrice(clickedRow.getIsbn());
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						lvBookData.clear();
						listViewBook();
						bookInfo.setVisible(false);
						openBook.setVisible(false);

					});

					openBook.setOnAction(e -> {
						ArrayList<String> book = DataCenter.getInstance().getbook(lb1.getText(), clickedRow.getIsbn());

						Stage stage = new Stage();

						OpenBook x = new OpenBook();
						Pane app = x.book(book);

						Scene sc2 = new Scene(app, 1200, 850);
						sc2.getStylesheets().add("myAppCss.css");
						stage.setScene(sc2);
						stage.setTitle("Reader View");
						stage.centerOnScreen();
						stage.show();
					});
					
				}
			});
			return row;
		});

		add.setOnMouseClicked(e -> {

			if (gp.isVisible()) {
				gp.setVisible(false);
				addBook.setVisible(false);
				authorTf.clear();
				titleTf.clear();
				titleTf.clear();
				isbnTf.clear();
				genreTf.clear();
			} else if (!gp.isVisible()) {

				gp.setVisible(true);
				addBook.setVisible(true);

				addBook.setOnAction(ae -> {

					if (Util.valadateAddBook(titleTf.getText(), isbnTf.getText(), genreTf.getText(),
							authorTf.getText())) {

						MyTableRow row = new MyTableRow(authorTf.getText(), titleTf.getText(), isbnTf.getText(),
								genreTf.getText());

						DataCenter.getInstance().addBookIO(user.getUsername(), lb1.getText(), authorTf.getText(),
								titleTf.getText(), isbnTf.getText(), genreTf.getText());

						tvData.add(row);
						delete.setDisable(false);
						gp.setVisible(false);
						addBook.setVisible(false);
						authorTf.clear();
						titleTf.clear();
						titleTf.clear();
						isbnTf.clear();
						genreTf.clear();
					}
				});
			}
		});

		delete.setOnAction(e -> {
			if (tvData.size() <= 1) {
				delete.setDisable(true);
			}
			// Remove.

			TableViewSelectionModel<MyTableRow> selectionModel = tv.getSelectionModel();
			String rowIsbn = selectionModel.getSelectedItem().getIsbn();

			if (DataCenter.getInstance().deleteBookIO(lb1.getText(), rowIsbn)) {
				// Source index of master data.
				// When removing a selected item from a sorted list you will get an unsupported
				// message
				// Have to call the selected index and find the source index --> remove from
				// master data
				int visibleIndex = tv.getSelectionModel().getSelectedIndex();
				int sourceIndex = sortedData.getSourceIndexFor(tvData, visibleIndex);
				tvData.remove(sourceIndex);
			}
		});

		return vb;
	}

	private Pane buildPieChart() {
		borderPane = new BorderPane();
		borderPane.setStyle("-fx-background-color: rgb(241, 244, 248);;");

		// Preparing ObservbleList object
		DataCenter.getInstance().analyzeBook(lb2.getText());
		ArrayList<Word> book = DataCenter.getInstance().getBookData();

		for (int i = 0; i < book.size(); i++) {
			list.add(new PieChart.Data(book.get(i).getWord(), book.get(i).getWordCount()));
		}
		PieChart pc = new PieChart(); // Creating a Pie chart
		pc.getData().addAll(list);
		
		// attach tooltips
		createToolTips(pc);
		pc.setTitle(lb2.getText());
		pc.setClockwise(true); // setting the direction to arrange the data
		pc.setLabelLineLength(50); // Setting the length of the label line
		pc.setLabelsVisible(true); // Setting the labels of the pie chart visible
		pc.setLegendVisible(true);
		pc.setStartAngle((int) Math.random()*200);

		// bind value and label on each pie slice to reflect changes
		list.forEach(
				data -> data.nameProperty().bind(Bindings.concat(data.getName(), " ", data.pieValueProperty(), " ")));
		borderPane.setCenter(pc);
		return borderPane;
	}
	
	//For Pie chart
	private void createToolTips(PieChart pc) {

		for (PieChart.Data data : pc.getData()) {
			String msg = Double.toString(data.getPieValue());

			Tooltip tp = new Tooltip(msg);
			tp.setShowDelay(Duration.seconds(0));

			Tooltip.install(data.getNode(), tp);

			// update tooltip data when changed
			data.pieValueProperty().addListener((observable, oldValue, newValue) -> {
				tp.setText(newValue.toString());
			});
		}
	}

}
