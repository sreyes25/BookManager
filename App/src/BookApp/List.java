package BookApp;

public interface List {

		boolean add(Book o);
		boolean remove(Book o);
		int search(Book o);
		Book getBook(int index);
		void setBook(Book o, int index);
		int size();
		boolean isEmpty();
		void display();
		

}
