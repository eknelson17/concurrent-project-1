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
		doWork(startTime + 480); // End the day
		say("ended work");
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
		if ((r.nextDouble() < 0.5) && Thread.currentThread() != this) { // can answer
			System.out.println("\tTeam Lead for team " + teamID + " answered a question for Employee " + ((Employee) Thread.currentThread()).getId() +" at " + Firm.getFirmTime().formatTime());
			return;
		} else {
			if (Thread.currentThread() != this) {
				System.out.println("\tTeam Lead for team " + teamID + " and Employee " + ((Employee) Thread.currentThread()).getId() +" head to the PM's office at " + Firm.getFirmTime().formatTime());
			} else {
				System.out.println("\tTeam Lead for team " + teamID + " heads to the PM's office at " + Firm.getFirmTime().formatTime());
			}
			Firm.getProjectManager().answerQuestion();
		}
	}
}
