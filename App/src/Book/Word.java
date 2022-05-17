package Book;

public class Word {
	
	private String word;
	private int wordCount;
	
	public Word(String word) {
		super();
		this.word = word;
		this.wordCount = 1;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public int getWordCount() {
		return wordCount;
	}

	public void wordAdded() {
		this.wordCount++;
	}

	@Override
	public String toString() {
		return word + ": " + wordCount;
	}
	
	
	
}
