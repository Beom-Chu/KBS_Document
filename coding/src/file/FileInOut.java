package file;

import java.io.FileInputStream;
import java.io.FileOutputStream;

public class FileInOut {
	
	String filePathIn = "D:\\KBS_Document\\coding\\src\\file\\FileInOut.java";
	String filePathOut = ".\\src\\file\\FileTest.txt";
	
	String filePathInImg = "D:\\KBS_Document\\coding\\src\\file\\on.png";
	String filePathOutImg = ".\\src\\file\\on2.png";
	
	public void FileWrite() {
		
		try(
				FileInputStream fis = new FileInputStream(filePathIn);
				FileOutputStream fos = new FileOutputStream(filePathOut);
				)
		{
						
			int readCnt = -1;
			while((readCnt = fis.read()) != -1) {
				fos.write(readCnt);
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void FileWrite2() {
		
		try(
				FileInputStream fis = new FileInputStream(filePathInImg);
				FileOutputStream fos = new FileOutputStream(filePathOutImg);
				)
		{
						
			int readCnt = -1;
			byte[] b = new byte[512];
			while((readCnt = fis.read(b)) != -1) {
				fos.write(b, 0, readCnt);
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	
	public static void main(String[] args) {
		
		FileInOut fileInOut = new FileInOut();
		
		long st = System.currentTimeMillis();
		fileInOut.FileWrite();
		System.out.println("[[int단위 - 소요시간 : "+ (System.currentTimeMillis()-st));
		
		long st2 = System.currentTimeMillis();
		fileInOut.FileWrite2();
		System.out.println("[[512byte - 소요시간 : "+ (System.currentTimeMillis()-st2));
		

	}

}
