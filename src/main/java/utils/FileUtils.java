package utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FileUtils {

	public static String readFile(String file) {
		StringBuilder builder = new StringBuilder();
        
        try {
            BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/" + file));
            String line;
            
            while((line = reader.readLine()) != null){
            	builder.append(line).append("//\n");
            }
            
            reader.close();
        } catch(IOException e){
            e.printStackTrace();
            System.exit(-1);
        }
        
        return builder.toString();
	}
}
