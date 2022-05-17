package BookApp;

public class BookList implements List {

	private Book[] book;
	private String path;
	private int nElems;

	public BookList() {
		book = new Book[nElems + 1];
		nElems = 0;
	}

	@Override
	public boolean add(Book b) {
		Book[] a = new Book[nElems + 1];
		a = clone(book, true);
		a[nElems++] = (Book) b;
		book = a;
		return true;
	}

	public boolean add(Book b, int index) {
		book[index] = b;
		return true;
	}

	@Override
	public boolean remove(Book b) {
		// TODO Auto-generated method stub
		int i = 0;
		for (i = 0; i < nElems; i++) {
			if (book[i].equals(b))
				break;
		}

		if (i == nElems) {
			return false;
		} else {
			nElems--;
			for (int j = i; j < nElems; j++) { // overwrite the object found by the next object
				book[j] = book[j + 1];
			}

			Book[] a = new Book[nElems];
			a = clone(book, false);
			book = a;

			return true;
		}
	}

	public boolean remove(int index) {
		if (index > nElems) {
			return false;
		} else {
			nElems--;
			for (int j = index; j < nElems; j++) {
				book[j] = book[j + 1];
			}
			Book[] a = new Book[nElems];
			a = clone(book, false);
			book = a;
			return true;
		}

	}

	public boolean remove() {
		nElems = 0;
		book = new Book[nElems + 1];
		return true;
	}

	@Override
	public int search(Book b) {
		// TODO Auto-generated method stub
		int i = 0;
		for (i = 0; i < nElems; i++) {
			if (book[i].equals(b))
				break;
		}
		return i;
	}

	@Override
	public Book getBook(int index) {
		// TODO Auto-generated method stub
		return book[index];
	}

	@Override
	public void setBook(Book b, int index) {
		// TODO Auto-generated method stub
		book[index] = (Book) b;
	}

	@Override
	public int size() {
		return nElems;
	}

	@Override
	public boolean isEmpty() {
		if (nElems == 0) {
			return true;
		} else
			return false;
	}

	@Override
	public void display() {

		System.out.println("List[" + nElems + "]");
		for (int i = 0; i < book.length; i++) {
			System.out.println(book[i]);
		}
		System.out.println();

	}

	public Book[] clone(Book[] b, boolean add) {
		int x;
		int y;

		if (add == true) {
			x = 1;
			y = 0;
		} else {
			x = 0;
			y = -1;
		}
		Book[] book = new Book[nElems + x];

		for (int i = 0; i < b.length + y; i++) {
			book[i] = b[i];
		}
		return book;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	
	
/*
	public boolean equals(Book obj) {

		if (obj == this) {
			return true;
		}

		if (!(obj instanceof Book)) {
			return false;
		}
		
		//Set an equals method
		return true;

	}
*/
}
