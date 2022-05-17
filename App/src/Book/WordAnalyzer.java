package Book;

import java.util.ArrayList;

public class WordAnalyzer implements List, Cloneable {

	private Word[] arr;
	private int nElems;
	
	public WordAnalyzer() {
		arr = new Word[nElems + 1];
		nElems = 0;
	}

	@Override
	public boolean add(String s) {
		
		String lower = s.toLowerCase();
		
		if (lower.length() < 3) {
			
			for(int i = 0; i < lower.length(); i++) {
				if (lower.charAt(i) < 96 && lower.charAt(i) < 123) {
					return false;
				}
			}
			
		}
		
		if (!searchList(lower)) {
			Word[] a = new Word[nElems + 1];
			a = clone(arr, true);
			Word temp = new Word(lower);
			a[nElems++] = temp;
			arr = a;
			return true;
		}
		else {
			int i = search(lower);
			getWord(i).wordAdded();
		}
		return false;
	}

	@Override
	public boolean remove(Word s) {
		// TODO Auto-generated method stub
		int i = 0;
		for (i = 0; i < nElems; i++) {
			if (arr[i].equals(s))
				break;
		}

		if (i == nElems) {
			return false;
		} else {
			nElems--;
			for (int j = i; j < nElems; j++) { // overwrite the object found by the next object
				arr[j] = arr[j + 1];
			}

			Word[] a = new Word[nElems];
			a = clone(arr, false);
			arr = a;

			return true;
		}
	}

	public boolean remove(int index) {
		if (index > nElems) {
			return false;
		} 
		else {
			nElems--;
			for (int j = index; j < nElems; j++) { 
				arr[j] = arr[j + 1];
			}
			Word[] a = new Word[nElems];
			a = clone(arr, false);
			arr = a;
			return true;
		}

	}

	public boolean remove() {
		nElems = 0;
		arr = new Word[nElems + 1];
		return true;
	}

	@Override
	public int search(String s) {
		// TODO Auto-generated method stub
		int i = 0;
		for (i = 0; i < nElems; i++) {
			if (arr[i].getWord().equals(s))
				break;
		}
		return i;
	}
	
	public boolean searchList(String s) {
		for (int i = 0; i < nElems; i++) {
			if (arr[i].getWord().equals(s))
				return true;
		}
		return false;
	}

	@Override
	public Word getWord(int index) {
		// TODO Auto-generated method stub
		return arr[index];
	}

	@Override
	public void setWord(Word s, int index) {
		// TODO Auto-generated method stub
		arr[index] = (Word)s; 
	}
	
	public void addToWordCount(int index) {
		arr[index].wordAdded();
	}
	
	public ArrayList<Word> mostCommonWords() {
		WordAnalyzer list = new WordAnalyzer();
		list.setList(arr);
		list.setSize(nElems);
		
		
		ArrayList<Word> commWords = new ArrayList<Word>();

		while (commWords.size() != 10) {

			Word max = list.getWord(0);
			Word min = list.getWord(0);
			for (int i = 0; i < list.size(); i++) {

				if (list.getWord(i).getWordCount() > max.getWordCount()) {
					max = list.getWord(i);
				}
				if (list.getWord(i).getWordCount() < min.getWordCount()) {
					min = list.getWord(i);
				}
			}
			commWords.add(max);
			list.remove(max);
		}
		
		return commWords;
	}
	
	public void setList(Word[] list) {
		arr = list;
	}

	@Override
	public int size() {
		return nElems;
	}
	
	public void setSize(int size) {
		this.nElems = size;
	}

	@Override
	public boolean isEmpty() {
		if(nElems == 0) {
			return true;
		}
		else
			return false;
	}
	
	@Override
	public void display() {
		
		System.out.println("List["+nElems+"]");
		for(int i = 0; i < arr.length; i++) {
			System.out.println(arr[i].getWord()+": "+arr[i].getWordCount());
		}
		System.out.println();
		
	}

	public Word[] clone(Word[] s, boolean add) {
		int x;
		int y;

		if (add == true) {
			x = 1;
			y = 0;
		} else {
			x = 0;
			y = -1;
		}
		Word[] array = new Word[nElems + x];

		for (int i = 0; i < s.length + y; i++) {
			array[i] = s[i];
		}
		return array;
	}


}
