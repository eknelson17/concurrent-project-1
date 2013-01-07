import java.util.concurrent.CountDownLatch;


public class TeamLead extends Employee {
	
	public TeamLead(int id, int teamID, CountDownLatch startcdl) {
		super(id, teamID, startcdl);
	}
	
	@Override
	public void run() {
		// TODO: finish
		startTime = r.nextInt(30);
		startDay(startTime);
		doWork(startTime + 4800); // End the day
		System.out.println("Team Lead " + ID + " on team " + teamID + " ended work."); 
	}
	

	/**
	 * Acquire the conference room, wait on the cdl, then wait that time. 
	 * Used for meetings.
	 * @param cdl
	 * @param time
	 */
	public synchronized void meeting(CountDownLatch cdl, int time) {
		// TODO: acquire ConferenceRoom
		cdl.countDown();
		try {
			cdl.await();
			waitFor(time);
		} catch (InterruptedException e) {}
	}
	
	public synchronized void answerQuestion() {
		if (r.nextDouble() < 0.5) {	// Can answer
			return;
		} else {
			//TODO: Firm.getProjectManager().answerQuestion();
		}
	}
}
