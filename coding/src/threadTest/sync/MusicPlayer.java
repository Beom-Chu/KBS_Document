package threadTest.sync;

public class MusicPlayer implements Runnable {
	
	public int type;
	public MusicBox mb;
	
	public MusicPlayer(int type, MusicBox mb) {
		this.type = type;
		this.mb = mb;
	}
	
	@Override
	public void run() {
		
		switch (type) {
		case 1:
			mb.playMusic1();
			break;
		case 2:
			mb.playMusic2();
			break;
		case 3:
			mb.playMusic3();
			break;
		}
		
	}

}
