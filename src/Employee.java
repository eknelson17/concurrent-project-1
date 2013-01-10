import java.util.Random;
import java.util.concurrent.CountDownLatch;


public class Employee extends Thread {
	
	protected final double chance = 0.0025;
	
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
	
	protected int lunchTime;
	
	
	protected static Random r = new Random();
	
	/**
	 * CDL for starting to run.
	 */
	protected CountDownLatch startcdl;
	protected CountDownLatch afternoonMeeting;
	
	public Employee(int id, int teamID, CountDownLatch startcdl, CountDownLatch lastMeeting) {
		this.ID = id;
		this.teamID = teamID;
		this.startcdl = startcdl;
		this.afternoonMeeting = lastMeeting;
	}
	
	public int getID() {
		return ID;
	}
	
	public int getTeamID() {
		return teamID;
	}
	
	@Override
	public void run() {	
		//TODO: Finish run
		startTime = r.nextInt(30);
		lunchTime = 180 + r.nextInt(90); //11:00 - 12:30
		startDay(startTime);
		
		busyWait(Firm.getLead(teamID).getTeamLatch(), 15);
		
		doWork(lunchTime, true); // Work until lunch
		busyWait(new CountDownLatch(1), 30, "started eating lunch", "finished eating lunch");
		
		doWork(480, true); // Work until 4:00
		
		finalMeeting();
		doWork(startTime + 510, false); // Work until end of day
		say("ended work");
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
		say("started work");
		Thread.yield();
	}
	
	/**
	 * Do some work, could possibly ask a question
	 * @param nextScheduledEvent time of the next thing to do
	 * @param can questions be asked in this time?
	 */
	public void doWork(int nextScheduledEvent, boolean asksQuestion) {
		while (Firm.getFirmTime().getTimeElapsed() < nextScheduledEvent) {
			if (hasQuestion(chance)&& asksQuestion) {
				say("would like to ask a question");
				askQuestion();
			} else {
				try {
					sleep(10);
				} catch (InterruptedException e) {
				}
			}
		}
	}
	
	/**
	 * Wait on the cdl, then wait that time. 
	 * Used for lunch/meetings.
	 * @param cdl latch to wait before meeting
	 * @param time offset from current time to wait
	 * @param message to say before waiting (for example "starts eating lunch")
	 * @param message to say after waiting
	 */
	public synchronized void busyWait(CountDownLatch cdl, int time, String message1, String message2) {
		cdl.countDown();
		try {
			cdl.await();
			say(message1);
			waitFor(time);
			say(message2);
		} catch (InterruptedException e) {}
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
		long initTime = Firm.getFirmTime().getTimeElapsed();
		while (Firm.getFirmTime().getTimeElapsed() < initTime + time) {
			yield();
		}
	}
	
	/**
	 * Asks a question of the team lead
	 */
	protected void askQuestion() {
		Firm.getLead(teamID).answerQuestion();
	}
	
	/**
	 * Finds out if the employee has a question to ask
	 * @param chance (0.0-1.0) percent chance of question
	 * @return
	 */
	protected boolean hasQuestion(double c) {
		return (r.nextDouble() < c);		
	}
	
	/**
	 * Prints out a statement with information about the Thread stating it
	 * @param s action to state without spaces on ends
	 */
	protected void say(String s) {
		if (ID == 0) {
			System.out.println("Team Lead for team " + teamID + " " + s + " at " + Firm.getFirmTime().formatTime());
		} else if(ID == -1) {
			System.out.println("Project Manager" + " " + s + " at " + Firm.getFirmTime().formatTime());
		} else {
			System.out.println("Employee " + ID + " on team " + teamID + " " + s + " at " + Firm.getFirmTime().formatTime());
		}
	}
	
	/**
	 * Waits and sleeps for the final meeting. 
	 */
	protected void finalMeeting() {
		afternoonMeeting.countDown();
		try {
			afternoonMeeting.await();
			sleep(150);
		} catch (InterruptedException e) {
		}
	}
	
}
