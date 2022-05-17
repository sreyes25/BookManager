package BookApp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JFileChooser;

public class Util {
	private static Scanner scan;

	public static ArrayList<User> loadUsers() {
		ArrayList<User> userList = new ArrayList<>();
		String tempUser = "";
		String tempPass = "";
		try {
			String path = "/Users/sergioreyes/ManageBookApp/Users";
			ArrayList<String> userFolder = new ArrayList<>();
			userFolder = getFolders(path);

			for (int i = 0; i < userFolder.size(); i++) {
				
				path = userFolder.get(i).toString()+ "/log.txt";
				scan = new Scanner(new File(path));
				scan.useDelimiter("[,\n]");
				

				while (scan.hasNext()) {
					tempUser = scan.next();
					tempPass = scan.next();
					
					User user = new User(tempUser, tempPass);
				
					userList.add(user);
				}
	
				//If user already has an exist booklist Add it to the User
				// Wise blank a Default Book List will load
			}

		} catch (Exception e) {
			System.out.println(e);
		}
		return userList;
	}
	
	public static ArrayList<User> loadUsersIO() {
		ArrayList<User> userList = new ArrayList<>();

		try {
			String path = "/Users/sergioreyes/ManageBookApp/Users";
			ArrayList<String> userFolder = new ArrayList<>();
			userFolder = getFolders(path);
	

			for (int i = 0; i < userFolder.size(); i++) {
				
				path = userFolder.get(i).toString()+ "/log.txt";
				
				try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File(path)))) {

					User user = (User) ois.readObject();
					userList.add(user);
				} catch (IOException e) {
					e.printStackTrace();
				}

			}

		} catch (Exception e) {
			System.out.println(e);
		}
		return userList;
	}

	public static ArrayList<String> getFolders(String path) {
		File file = new File(path);
		File[] list = file.listFiles();
		ArrayList<String> folders = new ArrayList<>();

		if (file.isDirectory()) {
			try {
				for (int i = 0; i < list.length; i++) {
					if (list[i].isDirectory()) {
						folders.add(list[i].getPath());
					}
				}
			} catch (Exception e) {
				System.err.println(e.getMessage());
			}
		}
		return folders;
	}
	
	public static ArrayList<String> getFiles(String path) {
		ArrayList<String> files = new ArrayList<>();
		File mainFolder = new File(path);
		File[] list = mainFolder.listFiles();
		

		for (int i = 0; i < list.length; i++) {
			if (list[i].isFile() && (!list[i].getName().equals(".DS_Store"))) {
				files.add(list[i].getPath());
			}
		}
		return files;
	}
	
	public static String findMainDir() {
		String c = new JFileChooser().getFileSystemView().getDefaultDirectory().toString();
		return c;
	}
	
	public static boolean valadateAddBook(String tf0, String tf1, String tf2, String tf3) {
		
		if(tf0.length() > 1 &&
				tf1.length() > 1 &&
				tf2.length() > 1 &&
				tf3.length() > 1) 
		{	
			return true;
		}
		else return false;
	}
}
