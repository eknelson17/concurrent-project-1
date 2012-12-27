import java.util.concurrent.CountDownLatch;


public class TeamLead extends Employee {
	
	public TeamLead(int id, int teamID, CountDownLatch startcdl) {
		super(id, teamID, startcdl);
	}
	
	@Override
	public void run() {
		// TODO: finish
		startDay();
		doWork(startTime + 4800); // End the day
		System.out.println("Team Lead " + ID + " on team " + teamID + " ended work."); 
	}
	

	/**
	 * Acquire the conference room, wait on the cdl, then wait that time. 
	 * Used for meetings.
	 * @param cdl
	 * @param time
	 */
	public synchronized void meeting(CountDownLatch cdl, long time) {
		// TODO: acquire ConferenceRoom
		cdl.countDown();
		try {
			cdl.await();
			wait(time);
		} catch (InterruptedException e) {}
	}
}
