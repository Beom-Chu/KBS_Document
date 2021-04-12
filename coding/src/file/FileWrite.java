package file;

import java.io.DataOutputStream;
import java.io.FileOutputStream;

public class FileWrite {

	public void writeFile() {
		
		
		
		try(
				FileOutputStream fos = new FileOutputStream(".\\src\\file\\FileWriteTest.txt");
				DataOutputStream dos = new DataOutputStream(fos);
				)
		{
			
			dos.writeUTF("가나다라마바사");
			dos.writeUTF("\n");	
			dos.writeUTF("ABCDEFGH");
			dos.writeUTF("\n");	
			dos.writeUTF("12345678");
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
		
		System.out.println("FileWrite Start...");
		
		FileWrite fileWrite = new FileWrite();
		
		fileWrite.writeFile();
		
		System.out.println("FileWrite End...");
	}

}
