package file;

import java.io.DataInputStream;
import java.io.FileInputStream;

public class FileRead {

	public void readFile() {
		
		try(
				DataInputStream dis = new DataInputStream(new FileInputStream(".\\src\\file\\FileWriteTest.txt"));
				)
		{
			
			
			System.out.println(dis.readUTF());
			System.out.println(dis.readUTF());
			System.out.println(dis.readUTF());
			System.out.println(dis.readUTF());
			System.out.println(dis.readUTF());
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
		
		System.out.println("FileRead Start...");
		
		FileRead fileRead = new FileRead();
		fileRead.readFile();
		
		System.out.println("FileRead End...");
	}
}
