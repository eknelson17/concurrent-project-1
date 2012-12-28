import java.util.Random;
import java.util.concurrent.CountDownLatch;


public class Employee extends Thread {
	
	/**
	 * ID in the team.
	 * 0 is TeamLead
	 */
	protected final int ID;
	
	/**
	 * ID of team
	 */
	protected final int teamID;
	
	/**
	 * Time employee starts work. 
	 * Project Manager starts at 0
	 */
	protected int startTime;
	
	
	protected static Random r = new Random();
	
	/**
	 * CDL for starting to run.
	 */
	protected CountDownLatch startcdl;
	
	public Employee(int id, int teamID, CountDownLatch startcdl) {
		this.ID = id;
		this.teamID = teamID;
		this.startcdl = startcdl;
	}
	
	public int getID() {
		return ID;
	}
	
	public int getTeamID() {
		return teamID;
	}
	
	@Override
	public void run() {
		startTime = r.nextInt(30);
		startDay(startTime);
		doWork(startTime + 4800); // End the day
		System.out.println("Employee " + ID + " on team " + teamID + " ended work."); 
	}
	
	/**
	 * Start the day at a random time
	 * Blocks until that time
	 */
	public synchronized void startDay(int startOffset) {
		startcdl.countDown();
		try {
			startcdl.await();
		} catch (InterruptedException e) {}
		waitFor(startOffset);
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
	 * @param cdl latch to wait before meeting
	 * @param time offset from current time to wait
	 */
	public synchronized void busyWait(CountDownLatch cdl, int time) {
		cdl.countDown();
		try {
			cdl.await();
			waitFor(time);
		} catch (InterruptedException e) {}
	}
	
	/**
	 * Waits for a certain amount of time to pass. 
	 * @param time offset from current time to wait
	 */
	protected void waitFor(int time) {
		//int initTime = SimulationTime.getTime();
		while (true) { // SimulationTime.getTime < initTime + time
			yield();
		}
	}
	
}
