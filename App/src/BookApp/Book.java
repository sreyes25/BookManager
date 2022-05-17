package BookApp;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Book implements Serializable{

	private String author;
	private String genre;
	//private String language;
	private String title;
	private String isbn;
	//author, genre (science, novel, music, …); title; ISBN; language; 
	private String filePath;
	
	public Book(String author, String title, String isbn, String genre) {
		super();
		this.author = author;
		this.title = title;
		this.isbn = isbn;
		this.genre = genre;
		this.filePath = (title+".txt");
	}
	
	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	@Override
	public String toString() {
		return author + ","+title+","+ isbn+","+ genre;
	}

}
