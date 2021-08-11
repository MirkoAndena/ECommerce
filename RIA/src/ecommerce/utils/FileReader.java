package ecommerce.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;

public class FileReader {
	
	public static String read(HttpServlet servlet, String filename) {
		String path = servlet.getServletContext().getRealPath("/WEB-INF/templates/" + filename);      
		return read(path);
	}
	
	public static String read(String filename) {
		StringBuilder stringBuilder = new StringBuilder();
		try {
		      File myObj = new File(filename);
		      Scanner myReader = new Scanner(myObj);
		      while (myReader.hasNextLine()) {
		        String data = myReader.nextLine();
		        stringBuilder.append(data).append(System.lineSeparator());
		      }	
		      myReader.close();
	    } catch (FileNotFoundException e) {
	      System.out.println("An error occurred.");
	      e.printStackTrace();
	    }
		return stringBuilder.toString();
	}
}
