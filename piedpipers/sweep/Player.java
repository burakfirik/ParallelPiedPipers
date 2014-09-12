package piedpipers.sweep;

import java.util.*;

import piedpipers.sim.Point;

public class Player extends piedpipers.sim.Player {
	static int npipers;
	
	static double pspeed = 0.49;
	static double mpspeed = 0.09;
	
	static Point target = new Point();
	static int[] thetas;
	static boolean finishround = false; // false if the pipers are not near the gate
	static boolean initi = false;
	static boolean sweep = false; // false if the pipers have not began sweeping, true if pipers begin sweeping
	static ArrayList<ArrayList<Integer>> assignedRats;
	
	public void init() {
		target = new Point(dimension/2 - 3, dimension/2); // the location past the gate on the left side where the pipers will rest once they have swept all of the rats
		
		// this calculates the angle that the pipers move once they pass through the gate
		thetas = new int[npipers];
		for (int i=0; i< npipers; i++) {
			int theta = (180 * i)/ (npipers) + (180)/ (2 * npipers);
			thetas[i]=theta;
			System.out.println("THETAS EQUAL : " + thetas[i]);
		}
		
		assignedRats = new ArrayList<ArrayList<Integer>>();
		for(int i = 0; i < npipers; i++){
			assignedRats.add(new ArrayList<Integer>());
		}
		
	}

	static double distance(Point a, Point b) {
		return Math.sqrt((a.x - b.x) * (a.x - b.x) + (a.y - b.y) * (a.y - b.y));
	}

	// Return: the next position
	// my position: dogs[id-1]z

	public Point move(Point[] pipers, // positions of dogs
			Point[] rats) { // positions of the rats
			
		npipers = pipers.length;
		System.out.println(initi);
		Point gate = new Point(dimension/2, dimension/2);
		
		if (!initi) {
			this.init();initi = true;
		}
		Point current = pipers[id]; // Where the pipers are right now
		double ox = 0, oy = 0; // direction of the piper
		//nrats = rats.length;
		
		
		if (getSide(current) == 0 && !sweep) { // if on the left side - starting point
			this.music = false;
			
			// this is the change in movement towards the gate
			double dist = distance(current, gate);
			assert dist > 0;
			ox = (gate.x - current.x) / dist * pspeed;
			oy = (gate.y - current.y) / dist * pspeed;
			System.out.println("move toward the right side");
		}
		else if (!closetoWall(current) && !sweep) { // on the right side and hasn't bounced back yet
			// this is the change in movement based on the angle
			this.music = false;
			ox = pspeed * Math.sin(thetas[id] * Math.PI / 180);
			oy = pspeed * Math.cos(thetas[id] * Math.PI / 180);
		}		
		else { // after bounced and turns on music;
			this.music = true; // music turns on once pipers have reached close to wall
			if(!this.sweep){
				this.sweep = true; // sweeping begins once the pipers have reached close to wall
				assignRats(rats);
			}
			
			ArrayList<Integer> missingRatsIDs = ratsToCollect(pipers, rats); // rats that have to get collected
			System.out.println("number of missing rats = " + missingRatsIDs.size());
			if(missingRatsIDs.size() > 0){
				/*
				String print = "rats to collect are: ";
				for(int i = 0; i < missingRats.size(); i++)
					print += missingRats.get(i) + ", ";
				System.out.println(print);
				*/
				
				System.out.println("hasResponsibility for id=" + id + ":" + hasResponsibility(assignedRats.get(id), missingRatsIDs));
				if(hasResponsibility(assignedRats.get(id), missingRatsIDs)){
					current = toRat(current, missingRatsIDs, rats);	
				}
				else
					current = toGate(current);
				
			}
			else
				current = toGate(current);
				
		}
		
		current.x += ox;
		current.y += oy;
		return current;
	}
	
	// returns the point that the piper moves towards the rat
	public Point toRat(Point current, ArrayList<Integer> missingRats, Point[] rats){
		
		double ox = 0, oy = 0;
		Point gate = new Point(dimension/2, dimension/2);
		int furthestRatID = furthestRat(gate, missingRats, rats); // getting the ID of the furthest rat
		Point furthestRat = rats[furthestRatID]; // getting the point of the furthest rat
		
		System.out.println("IN TO RAT, GETTING RAT AT POINT: " + furthestRat);
		// if both the x and y of the piper are farther than 1 from the rat, change both
		if(Math.abs(furthestRat.x - current.x) > 1 && Math.abs(furthestRat.y - current.y) > 1 ){
			double dist = distance(current, furthestRat);
			assert dist > 0;
			System.out.println("IN BOTH DIFFERENCE");
			ox = (furthestRat.x - current.x) / dist * mpspeed;
			oy = (furthestRat.y - current.y) / dist * mpspeed;
		}
		// if the x of the piper is farther than 1 from the rat, change x
		else if(Math.abs(furthestRat.x - current.x) > 1){
		
			System.out.println("IN X DIFFERENCE");
			double dist = distance(current, furthestRat);
			assert dist > 0;
			
			ox = (furthestRat.x - current.x) / dist * mpspeed;
		}
		// if the y of the piper is farther than 1 from the rat, change y
		else{ // if  (Math.abs(furthestRat.y - current.y) > 1)
		
			System.out.println("IN Y DIFFERENCE, furthestRat = " + furthestRat);
			double dist = distance(current, furthestRat);
			assert dist > 0;
			
			oy = (furthestRat.y - current.y) / dist * mpspeed;
		}
		
		Point next = new Point(current.x + ox, current.y + oy);
		return next;	
	}
	
	// returns the rat that is furthest in the region
	public int furthestRat(Point gate, ArrayList<Integer> missingRats, Point[] rats){
		System.out.println("missingRat location = " + missingRats.get(0) + " = " + rats[missingRats.get(0)]);
		int ratID = 0;
		ArrayList<Integer> currentRats = assignedRats.get(id);
		for(int i = 0; i < missingRats.size(); i++){
			System.out.println("isResponsibility = " + isResponsibility(currentRats, rats[missingRats.get(i)], rats) + ", and distance > distance[ratID] = " + (distance(gate, rats[missingRats.get(i)]) > distance(gate, rats[missingRats.get(ratID)])));
			if(isResponsibility(currentRats, rats[missingRats.get(i)], rats) && distance(gate, rats[missingRats.get(i)]) > distance(gate, rats[missingRats.get(ratID)]))
				ratID = i;
		}
		System.out.println("furthest rat location " + ratID + " = " + rats[ratID]);
		return missingRats.get(ratID);
	}
	
	// if any of the missing rats are the current piper's responsibility
	public boolean hasResponsibility(ArrayList<Integer> currentRats, ArrayList<Integer> missingRats){
		System.out.println("in hasResponsibility()");
		for(int i = 0; i < currentRats.size(); i++){
			for(int j = 0; j < missingRats.size(); j++){
				if(currentRats.get(i) == missingRats.get(j))
					return true;
			}
		}
		return false;
	}
	
	// returns true if the specific rat is the current piper's responsibility
	public boolean isResponsibility(ArrayList<Integer> currentRats, Point specificRat, Point[] rats){
		for(int i = 0; i < currentRats.size(); i++){
			if(rats[currentRats.get(i)].equals(specificRat))
				return true;
		}
		return false;
	}
	
	// returns the point if moving piper towards the gate
	public Point toGate(Point current){
		double ox = 0, oy = 0;
		Point gate = new Point(dimension/2, dimension/2);
		// if both the x and y of the piper are farther than 1 from the gate, change both
		// !finishround signifies that the piper is not ready to move towards the target yet
		if(Math.abs(gate.x - current.x) > 1 && Math.abs(gate.y - current.y) > 1 && !finishround){
			double dist = distance(current, gate);
			assert dist > 0;
			
			ox = (gate.x - current.x) / dist * mpspeed;
			oy = (gate.y - current.y) / dist * mpspeed;
		}
		// if the x of the piper is farther than 1 from the gate, change x
		else if(Math.abs(gate.x - current.x) > 1  && !finishround){
			double dist = distance(current, gate);
			assert dist > 0;
			
			ox = (gate.x - current.x) / dist * mpspeed;
		}
		// if the y of the piper is farther than 1 from the gate, change y
		else if(Math.abs(gate.y - current.y) > 1  && !finishround){
			double dist = distance(current, gate);
			assert dist > 0;
			
			oy = (gate.y - current.y) / dist * mpspeed;
		}
		// now the piper is close to the gate, so it can move towards the target
		else if(Math.abs(target.x - current.x) > 1){
			finishround = true; // change finishround to false to show that the piper has finished sweeping its region and can move to the left side
			double dist = distance(current, target);
			assert dist > 0;
			
			ox = (target.x - current.x) / dist * mpspeed;
		}
		
		Point next = new Point(current.x + ox, current.y + oy);
		return next;
			
	}
	
	// assigns the rats to all of the pipers based on region
	public void assignRats(Point[] rats) {

		// adds the ratID to the piper's list of rats that it's responsible for
		for(int j = 0; j < rats.length; j++){
			int responsiblePiper = getPiperResponsibilityID(rats[j]);
			assignedRats.get(responsiblePiper).add(j);
		}
		
		/*
		String s = "ASSIGNMENT\npiper ";
		for(int i = 0; i < assignedRats.size(); i++){
			ArrayList<Point> piperAssignedRats = assignedRats.get(i);
			s += i + ": ";
			for(int k = 0; k < piperAssignedRats.size(); k++){
				s += piperAssignedRats.get(k) + ",";
			}
			s += "\n";
		}
		System.out.println(s);
		*/

	}
	
	// returns the ID of the piper who has the responsibility for the rat
	public int getPiperResponsibilityID(Point ratLoc){
		double ratDegree = Math.toDegrees(Math.atan( (ratLoc.x - dimension/2)/(dimension/2 - ratLoc.y))) + 90;
		int piperID = 0;
		for (int i=0; i< npipers; i++) {
			if(ratDegree >= thetas[i] - (180 / (2 * npipers)) && ratDegree < thetas[i] + (180 / (2 * npipers))){
				piperID = i;
				return piperID;
			}
		}
		return piperID;
	}
	
	// returns an ArrayList of ratIDs that need to be collected
	public ArrayList<Integer> ratsToCollect(Point[] pipers, Point[] rats){
		ArrayList<Integer> ratsToCollect = new ArrayList<Integer>();
		for(int i = 0; i < rats.length; i++){
			Point closestPiper = getClosestMusicPiper(pipers, rats, i);
			double dist = distance(rats[i], closestPiper);
			if(dist > 10) ratsToCollect.add(i);
		}
	
		return ratsToCollect;
	}
	
	// returns true if all rats have been collected
	boolean allRatsCollected(Point[] pipers, Point[] rats){
		if(ratsToCollect(pipers, rats).size() == 0)	
			return true;
		return false;
	}
	
	// returns the point of the closest music piper, taken from Piedpipers class
	public Point getClosestMusicPiper(Point[] pipers, Point[] rats, int ratId) {
		int minpiper = -1;
		double mindist = Double.MAX_VALUE;
		for (int i = 0; i < npipers; ++i) {
			if (this.music) {
				double d = distance(rats[ratId], pipers[i]);
				if (d < mindist && d > 0) { // ignore overlapping pipers?
					mindist = d;
					minpiper = i;
				}
			}
		}
		return pipers[minpiper];
	}
	
	boolean closetoWall (Point current) { // if distance is less than the speed
		boolean wall = false;
		if (Math.abs(current.x-dimension+10)<pspeed) {
			//System.out.println("close to the right wall");
			wall = true;
		}
		if (Math.abs(current.y-dimension+10)<pspeed) {
			//System.out.println("close to the bottom wall");
			wall = true;
		}
		if (Math.abs(current.y-10)<pspeed) {
			//System.out.println("close to top wall");
			wall = true;
		}
		return wall;
	}
	
	int getSide(double x, double y) {
		if (x < dimension * 0.5)
			return 0;
		else if (x > dimension * 0.5)
			return 1;
		else
			return 2;
	}

	int getSide(Point p) {
		return getSide(p.x, p.y);
	}

}