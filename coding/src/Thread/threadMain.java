package Thread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class threadMain {

	public static void main(String[] args) throws Exception{
		
		System.out.print("Thread:1\nRunable:2\n입력:");
		
		InputStream is = System.in;
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		
		String type = null;
		try {
			type = br.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(type.equals("1")) {
			
			PrintThread prt1 = new PrintThread("A");
			PrintThread prt2 = new PrintThread("B");
			
			prt1.start();
			prt2.start();
			
		}else if(type.equals("2")) {
			
			PrintRunable prt1 = new PrintRunable("A");
			PrintRunable prt2 = new PrintRunable("B");
			
			Thread trd1 = new Thread(prt1);
			Thread trd2 = new Thread(prt2);
			
			trd1.start();
			trd2.start();
		}
		
		
		
		
		
	}

}
