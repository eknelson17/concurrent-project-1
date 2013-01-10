import java.util.concurrent.CountDownLatch;


public class TeamLead extends Employee {
	
	private CountDownLatch morningMeeting;
	
	private CountDownLatch teamMeeting;
	
	public TeamLead(int id, int teamID, CountDownLatch startcdl, CountDownLatch lastMeeting, CountDownLatch firstMeeting) {
		super(id, teamID, startcdl, lastMeeting);
		morningMeeting = firstMeeting;
		teamMeeting = new CountDownLatch(Firm.MEMBERS_PER_TEAM);
	}
	
	public CountDownLatch getTeamLatch() {
		return teamMeeting;
	}
	
	@Override
	public void run() {
		// TODO: finish
		startTime = r.nextInt(30);
		lunchTime = 180 + r.nextInt(90); //11:00 - 12:30
		startDay(startTime);
		
		// Morning Meeting
		morningMeeting.countDown();
		try {
			morningMeeting.await();
			sleep(150);
		} catch (InterruptedException e) {
		}
		
		meeting(teamMeeting, 15, "started the team meeting for team " + teamID, "finished the team meeting for team " + teamID);
		
		doWork(lunchTime, true); // Work until lunch
		busyWait(new CountDownLatch(1), 30, "started eating lunch", "finished eating lunch");
		
		doWork(480, true); // Work until 4:00
		
		finalMeeting();
		doWork(startTime + 510, false); // Work until end of day
		say("ended work");
	}
	

	/**
	 * Acquire the conference room, wait on the cdl, then wait that time. 
	 * Used for meetings.
	 * @param cdl
	 * @param time
	 */
	public synchronized void meeting(CountDownLatch cdl, int time, String msg1, String msg2) {
		synchronized(Firm.getConferenceRoom()) {
			cdl.countDown();
			try {
				cdl.await();
				say(msg1);
				waitFor(time);
				say(msg2);
			} catch (InterruptedException e) {}
		}
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
