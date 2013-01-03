import java.util.Timer;
import java.util.TimerTask;

public class FirmTime {

	/**
	 * The hour of the beginning of day on a 24 hour clock
	 */
	private static final int START_HOUR = 8;

	/**
	 * The minutes of the beginning of day
	 */
	private static final int START_MINUTES = 0;

	/**
	 * The number of minutes that would equate to end of day
	 */
	private static final int END_OF_DAY = 540;

	/**
	 * The timer
	 */
	private final Timer timer = new Timer();

	/**
	 * Time in minutes elapsed since the program started.
	 */
	private int timeElapsed;

	/**
	 * Starts the Timer. Schedules a task to increment time elapsed every 10 ms
	 */
	public void start() {
		TimerTask task = new TimerTask() {
			public void run() {
				timeElapsed += 1;
			}
		};
		timer.schedule(task, 0, 10);
	}

	/**
	 * Terminates the timer.
	 */
	public void cancel() {
		timer.cancel();
	}

	/**
	 * Checks whether the number of minutes elapsed is equivalent to end of day
	 * 
	 * @return true if it is end of day false if it isn't
	 */
	public boolean isEndOfDay() {
		if (timeElapsed == END_OF_DAY)
			return true;
		return false;
	}

	/**
	 * Getter for time elapsed
	 * 
	 * @return time elapsed since beginning of day in minutes
	 */
	public int getTimeElapsed() {
		return timeElapsed;
	}

}
