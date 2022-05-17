package BookApp;

import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class User implements Serializable {
	
	private String username;
	private String password;
	private int pfp;
	private ArrayList<BookList> bookLists;
	
	public User(String username, String password) {
		this.username = username;
		this.password = password;
		bookLists = new ArrayList<>();
	}
	public User(String username, String password, ArrayList<BookList> bookLists) {
		this.username = username;
		this.password = password;
		this.bookLists = bookLists;
	}
	
	public void addBookList(BookList bookList) {
		bookLists.add(bookList);
	}
	
	public ArrayList<BookList> getBookList() {
		return bookLists;
	}
	public boolean removeBookList(String collectionName) {
		for(int i = 0; i < bookLists.size(); i++) {
			if(bookLists.get(i).getPath().equals(collectionName)) {
				bookLists.remove(i);
				return true;
			}
		}
		return false;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public int getPfp() {
		return pfp;
	}
	public void setPfp(int pfp) {
		this.pfp = pfp;
	}
	@Override
	public String toString() {
		return username+","+password;
	}

	public boolean equals(Object obj) {
		
		if(obj == this) {
			return true;
		}
		
		if (!(obj instanceof User)){
			return false;
		}
		
		User user = (User)obj;
		return username.equals(user.getUsername()) &&
				password.equals(user.getPassword());
	}
	
}
