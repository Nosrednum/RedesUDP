package UtilityPackage;

public class Timer {
	private long time;
	public Timer() {
		time=0;
	}
	public void start() {
		this.time=System.currentTimeMillis();
	}
	public long stop() {
		long fin = System.currentTimeMillis();
		return (fin-time);
	}
}
