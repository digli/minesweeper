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
			long diff = t - System.currentTimeMillis();
			try {
				Thread.sleep(diff);
			} catch (InterruptedException e) {
				return;
			}
		}
	}
}