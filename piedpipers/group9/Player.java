package piedpipers.group9;

import java.util.*;

import piedpipers.sim.Point;

public class Player extends piedpipers.sim.Player {
	static int npipers;
	
	static double pspeed = 0.49;
	static double mpspeed = 0.09;
	
	static Point target = new Point();
	static int[] thetas;
	static boolean finishround = true;
	static boolean initi = false;
	
	public void init() {
		thetas = new int[npipers];
		for (int i=0; i< npipers-1; i++) {
			//int theta = (int) (Math.sin( 180 / npipers)*i);
			int theta = (180 * i)/ (npipers-1) + (180)/ (2 * npipers-1);
			thetas[i]=theta;
			System.out.println(thetas[i]);
		}
		
		/*for (int i=0; i< npipers; i++) {
			Random random = new Random();
			int theta = random.nextInt(180);
			thetas[i]=theta;
			System.out.println(thetas[i]);
		}*/
	}

	static double distance(Point a, Point b) {
		return Math.sqrt((a.x - b.x) * (a.x - b.x) + (a.y - b.y) * (a.y - b.y));
	}

	// Return: the next position
	// my position: dogs[id-1]

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
		if (getSide(current) == 0) { // if on the left side
			finishround = true;
			this.music = false;
			double dist = distance(current, gate);
			assert dist > 0;
			ox = (gate.x - current.x) / dist * pspeed;
			oy = (gate.y - current.y) / dist * pspeed;
			//Random random = new Random();
			//int theta = random.nextInt(180);
			
			//thetas[id]=theta;
			System.out.println("move toward the right side");
		}
		else if (!closetoWall(current) && finishround) { // on the right side and while all of the rats haven't been collected
			this.music = false;
			ox = pspeed * Math.sin(thetas[id] * Math.PI / 180);
			oy = pspeed * Math.cos(thetas[id] * Math.PI / 180);
		}
		else { // after bounced and turns on music
			finishround = false;
			this.music = true;
			double dist = distance(current, gate);
			assert dist > 0;
			ox = (gate.x - current.x) / dist * mpspeed;
			oy = (gate.y - current.y) / dist * mpspeed;
			System.out.println("move toward the left side");
		}
		
        if (id == npipers-1 && current.x<dimension/2-2)
        {
        	gate.x = gate.x-2;
        	double dist = distance(current, gate);
			ox = (gate.x-2 - current.x) / dist * pspeed;
			oy = (gate.y - current.y) / dist * pspeed;
        }
        if(id == npipers-1 && current.x>=dimension/2-5)
        {
    			this.music = true;
        		ox = 0;
        		oy = 0;
        }
        
		
		current.x += ox;
		current.y += oy;
		return current;
	}
	boolean closetoWall (Point current) { // if distance is less than the speed
		boolean wall = false;
		if (Math.abs(current.x-dimension+10)<pspeed) {
			System.out.println("close to the right wall");
			wall = true;
		}
		if (Math.abs(current.y-dimension+10)<pspeed) {
			System.out.println("close to the bottom wall");
			wall = true;
		}
		if (Math.abs(current.y-10)<pspeed) {
			System.out.println("close to top wall");
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