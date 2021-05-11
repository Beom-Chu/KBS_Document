package threadTest.sync;

public class MusicBox {
	
	public synchronized void playMusic1() {
		
		for(int i=0; i<10; i++) {
			System.out.println("Classic");
			try {
				Thread.sleep((int)(Math.random()*1000));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public synchronized void playMusic2() {
		for(int i=0; i<10; i++) {
			System.out.println("Pop");
			try {
				Thread.sleep((int)(Math.random()*1000));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	/*
	public synchronized void playMusic3() {
		for(int i=0; i<10; i++) {
			System.out.println("Hiphop");
			try {
				Thread.sleep((int)(Math.random()*1000));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	*/
	public void playMusic3() {
		for(int i=0; i<10; i++) {
			
			synchronized (this) {
				System.out.println("Hiphop");
			}
			
			try {
				Thread.sleep((int)(Math.random()*1000));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
