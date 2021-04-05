package Thread;

public class PrintRunable implements Runnable {
	String str;
	public PrintRunable(String str) {
		this.str = str;
	}
	
	public void printLine() {
		for(int i=0; i<10; i++) {
			System.out.println("PrintRunable:"+str);
			try {
				Thread.sleep((int)(Math.random()*100));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	@Override
	public void run() {
		printLine();
	}

}
