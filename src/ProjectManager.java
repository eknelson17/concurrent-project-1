import java.util.concurrent.CountDownLatch;


public class ProjectManager extends Employee {
	
	public ProjectManager(CountDownLatch startcdl) {
		super(-1, -1, startcdl);
	}
	
	@Override
	public void run() {
		// TODO: finish
		startTime = 0;
		startDay(startTime);
		doWork(startTime + 4800); // End the day
		System.out.println("Employee " + ID + " on team " + teamID + " ended work."); 
	}
	
	public synchronized void answerQuestion() {
		try {
			sleep(100);
		} catch (InterruptedException e) {
		}
	}
	
}
