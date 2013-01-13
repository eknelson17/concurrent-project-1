import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class Firm {
	
	/**
	 * System constants
	 */
	public static final int NUMBER_OF_EMPLOYEES = 13; //3 teams + ProjectManager
	public static final int NUMBER_OF_TEAMS = 3 ;
	public static final int MEMBERS_PER_TEAM = 4 ;
	
	/**
	 * This latch will be passed to each Employee (And the ProjectManager) and the Timer in order to 
	 * start all Employee threads and the Timer thread at the same time
	 */
	private static final CountDownLatch cd = new CountDownLatch(NUMBER_OF_EMPLOYEES+1) ;
	
	private static final CountDownLatch firstMeeting = new CountDownLatch(NUMBER_OF_TEAMS + 1) ;
	private static final CountDownLatch lastMeeting = new CountDownLatch(NUMBER_OF_EMPLOYEES) ;
	
	/**
	 * Conference room that Team leads must obtain a lock on before hosting meeting
	 */
	private final static Object confRoom = new Object();
	
	/**
	 * The ProjectManager
	 */
	private final static ProjectManager pm = new ProjectManager(cd, lastMeeting, firstMeeting) ;
	
	/**
	 * A random Random class constant. 
	 */
	private final static Random r = new Random() ;
	
	/**
	 * The list of teams is represented by a 2d Array. Each array of employees represents
	 * a single team, and the array of arrays represents all the teams in this firm. 
	 */
	private final static Employee[][] teams = Firm.populateTeams(NUMBER_OF_TEAMS, MEMBERS_PER_TEAM);
	
	/**
	 * The timer that each thread will use to keep track of time
	 */
	private final static FirmTime timer = new FirmTime(cd);
	
	/**
	 * initializes a 2D array of Employees. Also starts each thread as it's created
	 */
	private static Employee[][] populateTeams(int numberOfTeams, int membersPerTeam ){
		Employee[][] teams = new Employee[numberOfTeams][membersPerTeam] ;
		
		//Creates each team
		for(int i = 0 ; i < numberOfTeams ; i++){
			
			//Creates team members (For each team)
			for(int j = 0 ; j < membersPerTeam ; j++){
				//The first member of a team will always be the team lead
				if( j==0 ){
					TeamLead tL = new TeamLead(j, i, cd, lastMeeting, firstMeeting) ;
					teams[i][j] = tL;
					tL.start() ;
				}
				//Everyone else is a regular employee
				else{
					Employee e = new Employee(j, i, cd, lastMeeting) ;
					teams[i][j] = e;
					e.start() ;
				}
			}
		}
		return teams ;
	}
	
	public static Employee[][] getAllEmployees(int id){
		return teams ; 
	}
	
	/**
	 * Getter for confRoom
	 * @return
	 */
	public static Object getConferenceRoom(){
		return confRoom ;
	}
	
	/**
	 * Returns the leader of a passed Employee's team
	 * @param the Team Lead whose leader you want to find
	 */
	public static TeamLead getLead(int teamID){	
		TeamLead lead = (TeamLead) teams[teamID][0] ;
		
		return lead ;
	}
	
	/**
	 * Getter for the firm's FirmTime
	 * @return
	 */
	public static FirmTime getFirmTime(){
		return timer ;
	}
	
	/**
	 * Getter for the firm's ProjectManager
	 * @return
	 */
	public static ProjectManager getProjectManager(){
		return pm ;
	}
	
	/**
	 * Getter for Random r
	 * @return
	 */
	public static Random getRandom(){
		return r ;
	}
	
	public static void main(String[] args){
		pm.start();
		timer.start();
		
		for(int i = 0 ; i <  NUMBER_OF_TEAMS ; i++){
			for(int j = 0 ; j < MEMBERS_PER_TEAM ; j++){
				try {
					teams[i][j].join();
				} catch (InterruptedException e) {
				}
			}
		}
		try {
			pm.join();
		} catch (InterruptedException e) {
		}
		
		Firm.getFirmTime().cancel();
		return;
	}
}
