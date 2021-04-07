package Thread.sync;

public class MusicPlayerMain {

	public static void main(String[] args) {
		
		MusicBox mb = new MusicBox();
		
		MusicPlayer mp1 = new MusicPlayer(1,mb);
		MusicPlayer mp2 = new MusicPlayer(2,mb);
		MusicPlayer mp3 = new MusicPlayer(3,mb);
		
		Thread trd1 = new Thread(mp1);
		Thread trd2 = new Thread(mp2);
		Thread trd3 = new Thread(mp3);
		
		trd1.start();
		trd2.start();
		trd3.start();

	}

}
