import java.util.concurrent.CountDownLatch;

public class ProjectManager extends Employee {
	
	private CountDownLatch morningMeeting;
	
	public ProjectManager(CountDownLatch startcdl, CountDownLatch lastMeeting, CountDownLatch firstMeeting) {
		super(-1, -1, startcdl, lastMeeting);
		morningMeeting = firstMeeting;
	}
	
	@Override
	public void run() {
		// TODO: finish
		startTime = 0;
		lunchTime = 240;
		startDayManager();
		
		// Morning Meeting
		morningMeeting.countDown();
		try {
			morningMeeting.await();
			say("started the Team Lead standup meeting");
			sleep(150);
			say("ended the Team Lead standup meeting");
		} catch (InterruptedException e) {
		}
		
		doWork(120, false); // Work until first exec meeting
		busyWait(new CountDownLatch(1), 60, "went to the first executive meeting", "finished the first executive meeting");
		
		doWork(lunchTime, false); // Work until lunch
		busyWait(new CountDownLatch(1), 60, "started eating lunch", "finished eating lunch");
		
		doWork(360, false); // Work until second exec meeting
		busyWait(new CountDownLatch(1), 60, "went to the second executive meeting", "finished the second executive meeting");
		
		doWork(480, false); // work until late meeting
		finalMeeting();
		doWork(540, false);
		say("ended work");
	}
	
	public synchronized void answerQuestion() {
		try {
			sleep(100);
			System.out.println("\t\tProject Manager finished answering question for team " + ((Employee) Thread.currentThread()).getTeamID());
		} catch (InterruptedException e) {
		}
	}
	
	/**
	 * Start the day
	 */
	private synchronized void startDayManager() {
		startcdl.countDown();
		try {
			startcdl.await();
		} catch (InterruptedException e) {}
		say("started work");
		Thread.yield();
	}
	
	/**
	 * Waits and sleeps for the final meeting after getting the conference room
	 */
	@Override
	protected void finalMeeting() {
		synchronized(Firm.getConferenceRoom()) {
			afternoonMeeting.countDown();
			try {
				afternoonMeeting.await();
				say("gathers all employees for the final meeting in the conference room");
				sleep(150);
				say("lets all employees leave the final meeting");
			} catch (InterruptedException e) {
			}
		}
	}
	
}
