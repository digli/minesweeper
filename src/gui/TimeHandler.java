package gui;

public class TimeHandler extends Thread {

	private TimeCounter tc;

	public TimeHandler(TimeCounter tc) {
		this.tc = tc;
	}

	public void run() {
		long t = System.currentTimeMillis();
		while (!interrupted()) {
			tc.increment();
			t += 1000;
			try {
				Thread.sleep(t - System.currentTimeMillis());
			} catch (InterruptedException e) {
				return;
			}
		}
	}
}