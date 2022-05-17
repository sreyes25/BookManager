package Book;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import BookApp.Util;

public class ReadBook {

	public ArrayList<Word> analyzeBookCommonWords(String title) {

		ArrayList<String> book = getbook(title);

		if (book != null) {

			WordAnalyzer list = new WordAnalyzer();
			Scanner scan = null;

			for (int i = 0; i < book.size(); i++) {
				scan = new Scanner(book.get(i));

				while (scan.hasNext()) {
					String w = scan.next();
					Word word = new Word(w);

					list.add(word.getWord());

				}

			}
			ArrayList<Word> common = list.mostCommonWords();
			return common;
		}
		return null;
	}

	public static ArrayList<String> getbook(String title) {
		ArrayList<String> bookLines = new ArrayList<>();
		File d = new File(System.getProperty("user.dir")); 
		File bookFile = new File(d.toString()+"/bin/DownloadedBooks/");
		
		File file = new File(bookFile.toString()+"/"+ title + ".txt"); // This is the book
																									// txt file
		// break this down ton anyArrayList for each page

		if (file.exists()) {
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
							if (w.contains(":") || w.contains(";")) {
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
		} else
			return null;
	}
}
