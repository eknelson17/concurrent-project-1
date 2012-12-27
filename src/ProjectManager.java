import java.util.concurrent.CountDownLatch;


public class ProjectManager extends Employee {
	
	public ProjectManager(CountDownLatch startcdl) {
		super(-1, -1, startcdl);
	}
	
	@Override
	public void run() {
		// TODO: finish
		startDay();
		doWork(startTime + 4800); // End the day
		System.out.println("Employee " + ID + " on team " + teamID + " ended work."); 
	}
	
	/**
	 * Start the day at initial start. 
	 */
	@Override
	public synchronized void startDay() {
		// TODO: finish when SimulationTime is stubbed
		startcdl.countDown();
		try {
			startcdl.await();
		} catch (InterruptedException e) {}
		startTime = 0;
		while (true) {// SimulationTime.getTime < 0
			yield();
		}
		// TODO: System.out.println("Project Manager started work"); 
	}
}
