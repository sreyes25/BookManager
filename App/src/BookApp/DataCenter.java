package BookApp;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Scanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import Book.ReadBook;
import Book.Word;
import Book.WordAnalyzer;
import javafx.application.Application;

public class DataCenter {
	private static DataCenter instance = null;
	private ArrayList<User> userList;
	private User user;
	private ArrayList<String> bookDetails;
	private ArrayList<Word> bookData;
	private String mainDir;

	private DataCenter() {
		downloadFolders();
		userList = new ArrayList<>();
		userList = Util.loadUsersIO();
		bookDetails = new ArrayList<>();
	}

	public static DataCenter getInstance() {
		if (instance == null) {
			instance = new DataCenter();
		}
		return instance;
	}
	
	public boolean downloadFolders() {
		boolean created = false;
		mainDir = Util.findMainDir();
		File AppDir = new File(mainDir+"/ManageBookApp/Users/");
		
		if (!AppDir.exists()) {
			AppDir.mkdirs();
			created = true;
		}
		return created;
	}

	// Add Methods
	public boolean addBookIO(String user, String collection, String author, String title, String isbn, String genre) {
		boolean created = false;

		String isbnCheck =isbn.replaceAll("-", "");	
		Book book = new Book(author, title, isbnCheck, genre);
		int path = 0;
		
		for (int i = 0; i < this.user.getBookList().size(); i++) {
			if (this.user.getBookList().get(i).getPath().equals(collection)) {
				path = i;
				File bookFile = new File(mainDir+"/ManageBookApp/Users/" + user + "/BookList/" + collection + ".txt");
				
				try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(bookFile))) {

					for (int j = 0; j < this.user.getBookList().get(i).size(); j++) {
						oos.writeObject(this.user.getBookList().get(i).getBook(j));
					}
					oos.writeObject(book);

				} catch (IOException e) {
					e.printStackTrace();
				}
				created = true;
				break;
			}
		}
		this.user.getBookList().get(path).add(book);
		return created;
	}

	public boolean addUserIO(String username, String password, String confirm) {
		boolean created = false;
		File userFile = new File(
				mainDir+"/ManageBookApp/Users/" + username);
		if (findUser(username, password) || userFile.exists()) {
			System.out.println("User alreay exists");
			return created;
		} else if (password.equals(confirm)) {

			User user = new User(username, password);
			user.setPfp(0);
			userList.add(user);
			// String userString = user.toString() + "\n";

			try {

				File userDir = new File(mainDir+"/ManageBookApp/Users/" + username);
				File log = new File(userDir.toString() + "/log.txt");
				File bookList = new File(userDir.toString() + "/BookList");
				File books = new File(bookList.toString() + "/MyBookList.txt");

				if (!userDir.exists()) {
					userDir.mkdirs();
					log.createNewFile();
					bookList.mkdir();
					books.createNewFile();
				}

				try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(log))) {

					oos.writeObject(user);

				} catch (IOException e) {
					e.printStackTrace();
				}

			} catch (IOException e) {
				// exception handling left as an exercise for the reader
				System.out.println(e + " Add User");
			}
			
			File bookFile = new File(
					mainDir+"/ManageBookApp/Users/" + username + "/BookList/" + "MyBookList.txt");
			
			try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(bookFile))) {

				Book book1 = new Book("Sergey Grinev", "Mastering JavaFX 10", "9781788293822", "CSE");
				Book book2 = new Book("Jamie Chan", "Learn Java In One Day and Learn It Well Java for Beginners with Hands-On Project", "9781539397830", "CSE");
				oos.writeObject(book1);
				oos.writeObject(book2);

			} catch (IOException e) {
				e.printStackTrace();
			}
	
			created = true;
		}

		return created;

	}

	public boolean addNewCollectionIO(String newCollection) {
		boolean created = false;
		File collectionDir = null;
		BookList book = new BookList();
		book.setPath(newCollection);

		if (!findCollection(newCollection)) {
			this.user.addBookList(book);

			try {

				collectionDir = new File(mainDir+"/ManageBookApp/Users/" + user.getUsername() + "/BookList/"
						+ newCollection + ".txt");

				collectionDir.createNewFile();
				created = true;

			} catch (IOException e) {
				// exception handling left as an exercise for the reader
				System.out.println(collectionDir.toString());
			}
			return created;

		} else {
			//BookList already Exists
			return created;
		}
	}

	//Delete Method
	public boolean deleteBookIO(String collection, String isbn ) {
		boolean deleted = false;
		//Book Collection Location
		File collectionDir = new File(mainDir+"/ManageBookApp/Users/" + user.getUsername() + "/BookList/" + collection + ".txt");
		int bookIndex = 0;
		int colIndex = 0;
		BookList bookList = new BookList();
		
		for (int i = 0; i < this.user.getBookList().size(); i++) {
			if (this.user.getBookList().get(i).getPath().equals(collection)) {
				colIndex = i;
				break;
			}
		}
		bookList.setPath(collection);
		//Read the bookFile and add all books except the one that matches the isbn
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(collectionDir))) {
			
			int i = 0;
			while (true) {
				Book book = (Book) ois.readObject();
				if(book.getIsbn().equals(isbn)) {
					bookIndex = i;
				}
				bookList.add(book);
				i++;
			}
		} catch (IOException | ClassNotFoundException e) {
			//No more Books
		}
		
		if (this.user.getBookList().get(colIndex).remove(bookIndex) && bookList.remove(bookIndex)) {
			
			try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(collectionDir))) {

				for (int j = 0; j < bookList.size(); j++) {
					oos.writeObject(bookList.getBook(j));
				}
				

			} catch (IOException e) {
				e.printStackTrace();
			}
			deleted = true;
		}
		return deleted;
	}
	
	public boolean deleteCollectionIO(String collection) {
		boolean deleted = false;
		File collectionDir = new File(
				mainDir+"/ManageBookApp/Users/" + user.getUsername() + "/BookList/" + collection + ".txt");
		
		if(collectionDir.delete()) {
			return user.removeBookList(collection);
		}
		return deleted;
	
	}
	
	// Find Methods//
	public boolean findUser(String username, String password) {
		boolean found = false;
		for (User user : userList) {

			if (user.equals(new User(username, password))) {
				found = true;
				break;
			}
		}
		return found;
	}

	public boolean findCollection(String name) {

		for (int i = 0; i < this.user.getBookList().size(); i++) {
			if (this.user.getBookList().get(i).getPath().equals(name)) {
				return true;
			}
		}
		return false;
	}
	
	public ArrayList<String> getbook(String collection, String isbn) {
		BookList temp = new BookList();
		String filePath = "";
		ArrayList<String> bookLines = new ArrayList<>();

		for (int i = 0; i < this.user.getBookList().size(); i++) {
			if (this.user.getBookList().get(i).getPath().equals(collection)) {
				temp = this.user.getBookList().get(i);
				break;
			}
		}

		for (int i = 0; i < temp.size(); i++) {
			if (temp.getBook(i).getIsbn().equals(isbn)) {
				if (!temp.getBook(i).getFilePath().equals(""))
					filePath = temp.getBook(i).getFilePath();
			}
		}
		
		
		File d = new File(System.getProperty("user.dir")); // user.dir returns the current working directory.
		File file = new File(d.toString()+"/bin/DownloadedBooks/"+ filePath);	//This is the book txt file
		//break this down ton anyArrayList for each page
		
		Scanner scan = null;
		Scanner read = null;
		try {
			scan = new Scanner(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		while (scan.hasNextLine()) {
			String nextLine = scan.nextLine();

			if (nextLine.length() > 55) {
				read = new Scanner(nextLine);

				while (read.hasNext()) {
					int i = 0;
					String words = "";

					while (read.hasNext() && i < 15) {
						String w = read.next();
						if(w.contains(":") || w.contains(";")) {
							words += w;
							break;
						}
						words += w + " ";
						i++;
					}
					bookLines.add("\t\t" + words);
				}
			} else {

				bookLines.add("\t\t" + nextLine);
			}
		}
		// read txt --> Split into 14 words a Line --> 500 line == 35 lines
	
		return bookLines;
		
	}
	
	public void setUserPfp(String n) {
		user.setPfp(Integer.valueOf(n));
		User userTemp = null;
		String pfp = mainDir+"/ManageBookApp/Users/" + user.getUsername() + "/log.txt";
		
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File(pfp)))) {

			userTemp = (User) ois.readObject();
			userTemp.setPfp(Integer.valueOf(n));
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		try (ObjectOutputStream ois = new ObjectOutputStream(new FileOutputStream(pfp))) {
				ois.writeObject(userTemp);
				
		} catch (IOException e) {
		}
		
	}
	
	// Will load data
	public User getUserInfo() {
		return user;
	}

	public ArrayList<BookList> getCollection() {
		return this.user.getBookList();
	}
	
	public ArrayList<String> getBookDetails() {
		return bookDetails;
	}
	// Load User
	
	public boolean loadUserDataIO(String username, String password) {
		boolean created = false;
		
		String pfp = mainDir+"/ManageBookApp/Users/" + username + "/log.txt";
		
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(pfp))) {
				user = (User) ois.readObject();
				
		} catch (IOException | ClassNotFoundException e) {
		}
		
		String bookLibrary = mainDir+"/ManageBookApp/Users/" + username + "/BookList";
		
		
		// Count how many book list user has
		ArrayList<String> books = Util.getFiles(bookLibrary);
		String listPath = "";
	
		for (int i = 0; i < books.size(); i++) {
			BookList bookList = new BookList();
			// Path of list
			String list = books.get(i).toString();
			Scanner scan = new Scanner(list);
			scan.useDelimiter("[/]");
			int k = 0;
			while (scan.hasNext()) {
				if (k == 6) {
					listPath = scan.next();
					break;
				}
				scan.next();
				k++;
			}
			scan.close();
			bookList.setPath(listPath.substring(0, listPath.length() - 4));
			
			if (books.get(i).length() == 0) {
				this.user.addBookList(bookList);
				
			} else {
				try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(books.get(i)))) {
					while (true) {
						Book book = (Book) ois.readObject();
						bookList.add(book);
					}
				} catch (IOException | ClassNotFoundException e) {
					this.user.addBookList(bookList);
					continue;
				}
			}
			created = true;
		}
		return created;
	}
	
	public boolean analyzeBook(String title) {
		
		boolean isDownLoaded = false;
		
		ReadBook bookReader = new ReadBook();
		
		ArrayList<Word> l = bookReader.analyzeBookCommonWords(title);
	
		if (l != null) {
			bookData = l;
			isDownLoaded = true;
		}
		return isDownLoaded;
	}
	
	public ArrayList<Word> getBookData(){
		return bookData;
	}
	
	public boolean webSrcapePrice(String isbn) throws InterruptedException {
		
		String newisbn = isbn.replaceAll("-", "");
		
		try {
			
		String userAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/15.3 Safari/605.1.15";
		String link = "";
		String author = "";
		String title = "";
		String allDetails = "";
		
			Document isbnSerach = Jsoup.connect("https://www.amazon.com/s?i=stripbooks&rh=p_66%3A" + newisbn
					+ "&s=relevanceexprank&Adv-Srch-Books-Submit.x=0&Adv-Srch-Books-Submit.y=0&unfiltered="
					+ "1&ref=sr_adv_b").userAgent(userAgent).get();

			Elements e = isbnSerach.getElementsByClass(
					"a-link-normal s-underline-text s-underline-link-text s-link-style a-text-normal");

			for (Element element : e.select("a.a-link-normal")) {
				link = element.attributes().get("href");
				title = element.text();
			}
			String fullLink ="https://www.amazon.com" + link;
			//bookDetails.add(fullLink);
			
			
			Document bookD = Jsoup.connect(fullLink).userAgent(userAgent).get();
			Elements elem = bookD
					.getElementsByClass("a-section a-spacing-none a-text-center rpi-attribute-value");
			Elements elemA = bookD
					.getElementsByClass("a-size-medium");
			
			for(Element element : elemA.select("span")) {
				author = element.text().toString();
				if(author.contains("(Author)")) {
					author = author.replaceAll("(Author)", "");
					author = author.substring(0, author.length()-2);
					bookDetails.add(author);
					break;
				}
				
				
			}
			
			bookDetails.add(title);
			for (Element element : elem.select("span")) {
				allDetails = element.text().toString();
				bookDetails.add(allDetails);
				
			}

		} catch (Exception elem) {
			Thread.sleep(10000);
		}

		return true;
	}

}

/*
 
 public Boolean loadUserData(String username, String password) {

		String bookLibrary = "/Users/sergioreyes/ManageBookApp/Users/" + username + "/BookList";
		// Count how many book list user has
		ArrayList<String> books = Util.getFiles(bookLibrary);
		// figure out a way for user to have muti

		for (int i = 0; i < books.size(); i++) {

			BookList bookList = new BookList();

			String listName = "";
			String authorTemp = "";
			String titleTemp = "";
			String isbnTemp = "";
			String genreTemp = "";
			Scanner scan;
			try {

				String list = books.get(i).toString();
				scan = new Scanner(list);
				scan.useDelimiter("[/]");

				int k = 0;
				while (scan.hasNext()) {
					if (k == 6) {
						listName = scan.next();
						break;
					}
					scan.next();
					k++;
				}

				scan = new Scanner(new File(books.get(i)));
				scan.useDelimiter("[,\n]");

				if (scan.hasNextLine()) {
					while (scan.hasNextLine()) {

						authorTemp = scan.next();
						titleTemp = scan.next();
						isbnTemp = scan.next();
						genreTemp = scan.next();

						Book book = new Book(authorTemp, titleTemp, isbnTemp, genreTemp);
						bookList.add(book);
					}
					bookList.setPath(listName.substring(0, listName.length() - 4));
					collection.add(bookList);
				} else {
					bookList.setPath(listName.substring(0, listName.length() - 4));
					collection.add(bookList);
				}
			} catch (Exception e) {
				System.out.println(e + " loadUser");
			}
		}
		user = new User(username, password, collection);
		return true;

	}
 
 public boolean addNewCollection(String newCollection) {
		boolean created = false;
		File collectionDir = null;
		BookList book = new BookList();
		book.setPath(newCollection);

		if (!findCollection(newCollection)) {
			collection.add(book);

			try {

				collectionDir = new File("/Users/sergioreyes/ManageBookApp/Users/" + user.getUsername() + "/BookList/"
						+ newCollection + ".txt");

				collectionDir.createNewFile();
				created = true;

			} catch (IOException e) {
				// exception handling left as an exercise for the reader
				System.out.println(collectionDir.toString());
			}
			return created;

		} else {
			return created;
		}

	}
 
 public boolean addBook(String user, String collection, String author, String title, String isbn, String genre) {
		boolean created = false;

		Book book = new Book(author, title, isbn, genre);

		for (int i = 0; i < this.collection.size(); i++) {
			if (this.collection.get(i).getPath().equals(collection)) {
				this.collection.get(i).add(book);
				break;
			}
		}

		try {
			String bookString = "";
			File bookDir = new File(
					"/Users/sergioreyes/ManageBookApp/Users/" + user + "/BookList/" + collection + ".txt");

			if (bookDir.length() == 0) {
				bookString = book.toString();
				Files.write(Paths.get(bookDir.toString()), bookString.getBytes(), StandardOpenOption.APPEND);
			} else {
				bookString = "\n" + book.toString();
				Files.write(Paths.get(bookDir.toString()), bookString.getBytes(), StandardOpenOption.APPEND);
			}
			created = true;
		} catch (IOException e) {
			// exception handling left as an exercise for the reader
			System.out.println(e);
		}
		return created;

	}
	
	public boolean addUser(String username, String password, String confirm) {
		boolean created = false;

		if (findUser(username, password)) {
			System.out.println("User alreay exists");
			return created;
		} else if (password.equals(confirm)) {

			User user = new User(username, password);
			userList.add(user);
			String userString = user.toString() + "\n";

			try {

				File userDir = new File("/Users/sergioreyes/ManageBookApp/Users/" + username);
				File log = new File(userDir.toString() + "/log.txt");
				File bookList = new File(userDir.toString() + "/BookList");
				File books = new File(bookList.toString() + "/MyBookList.txt");

				if (!userDir.exists()) {
					userDir.mkdirs();
					log.createNewFile();
					bookList.mkdir();
					books.createNewFile();
				}
				Files.write(Paths.get(log.toString()), userString.getBytes(), StandardOpenOption.APPEND);

			} catch (IOException e) {
				// exception handling left as an exercise for the reader
				System.out.println(e + " Add User");
			}
			created = true;
		}

		return created;

	}
	*/
 
