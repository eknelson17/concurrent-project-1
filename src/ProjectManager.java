import java.util.concurrent.CountDownLatch;


public class ProjectManager extends Employee {
	
	public ProjectManager(CountDownLatch startcdl) {
		super(-1, -1, startcdl);
	}
	
	@Override
	public void run() {
		// TODO: finish
		startTime = 0;
		startDayManager();
		busyWait(new CountDownLatch(1), 540); // End the day
		System.out.println("Project Manager ended work at " + Firm.getFirmTime().formatTime()); 
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
		System.out.println("Project Manager started work at " + Firm.getFirmTime().formatTime());
		Thread.yield();
	}
}
