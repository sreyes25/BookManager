package Book;
public interface List {

	boolean add(String o);
	boolean remove(Word o);
	int search(String o);
	Word getWord(int index);
	void setWord(Word o, int index);
	int size();
	boolean isEmpty();
	void display();
	
	
}
