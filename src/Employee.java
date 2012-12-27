import java.util.Random;
import java.util.concurrent.CountDownLatch;


public class Employee extends Thread {
	
	public int ID;
	public int teamID;
	protected static Random r = new Random();
	
	protected CountDownLatch startcdl;
	
	protected int startTime;
	
	public Employee(int id, int teamID, CountDownLatch startcdl) {
		this.ID = id;
		this.teamID = teamID;
		this.startcdl = startcdl;
	}
	
	@Override
	public void run() {
		startDay();
		doWork(startTime + 4800); // End the day
		System.out.println("Employee " + ID + " on team " + teamID + " ended work."); 
	}
	
	/**
	 * Start the day at a random time
	 * Blocks until that time
	 */
	public synchronized void startDay() {
		startcdl.countDown();
		try {
			startcdl.await();
		} catch (InterruptedException e) {}
		startTime = r.nextInt(30);
		while (true) {// SimulationTime.getTime < startTime
			yield();
		}
		// TODO: System.out.println("Employee " + ID + " on team " + teamID + " started work."); 
	}
	
	/**
	 * Do some work, could possibly ask a question
	 * @param nextScheduledEvent time of the next thing to do
	 */
	public void doWork(int nextScheduledEvent) {
		while (true) { // SimulationTime.getTime < nextScheduledEvent
			// TODO: ask question
		}
	}
	
	/**
	 * Wait on the cdl, then wait that time. 
	 * Used for lunch/meetings.
	 * @param cdl
	 * @param time
	 */
	public synchronized void busyWait(CountDownLatch cdl, long time) {
		cdl.countDown();
		try {
			cdl.await();
			wait(time);
		} catch (InterruptedException e) {}
	}
}
