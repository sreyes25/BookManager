package BookApp;
import javafx.beans.property.SimpleStringProperty;

public class MyTableRow {
	private SimpleStringProperty author;
	private SimpleStringProperty title;
	private SimpleStringProperty isbn;
	private SimpleStringProperty genre;
	
	public MyTableRow(String author, String title, String isbn, String genre) {
		
		this.author = new SimpleStringProperty(author);
		this.title = new SimpleStringProperty(title);
		this.isbn = new SimpleStringProperty(isbn);
		this.genre = new SimpleStringProperty(genre);
		
	}

	public String getAuthor() {
		return author.get();
	}

	public void setAuthor(String author) {
		this.author.set(author);
	}

	public String getTitle() {
		return title.get();
	}

	public void setTitle(String title) {
		this.title.set(title);
	}

	public String getIsbn() {
		return isbn.get();
	}

	public void setIsbn(String isbn) {
		this.isbn.set(isbn);
	}
	
	public String getGenre() {
		return genre.get();
	}

	public void setGenre(String genre) {
		this.genre.set(genre);
	}
	
}
