package piedpipers.group9;

import piedpipers.sim.Point;

/**/

public class PlayUtilities {
	
	public static void updateGlobal(Point[] pipers, Point[] rats){
		Global.rats=rats;
		Global.pipers=pipers;
	}
	
	//square point number
	// return square in the middle of the sweep field
	public Point moveInSquare(int pointNumber){
		
		switch(pointNumber){
			case 0:
				return new Point(2.0*(double)Global.dimension/3.0,(double)Global.dimension/3.0);
			case 1:
				return new Point(2.0*(double)Global.dimension/3.0,2.0*(double)Global.dimension/3.0);
			case 2:
				return new Point(5.0*(double)Global.dimension/6.0,2.0*(double)Global.dimension/3.0);
			case 3:
				return new Point(5.0*(double)Global.dimension/6.0,(double)Global.dimension/3.0);
			default:
				return null;
		}
	
	}
	
	public static int getSide(Point p) {
		return getSide(p.x, p.y);
	}
	public static int getSide(double x, double y) {
		if (x < Global.dimension * 0.5)
			return 0;
		else if (x > Global.dimension * 0.5)
			return 1;
		else
			return 2;
	}
	
	// detect whether the player has achieved the requirement
	public double density() {
			int nrats=0;
			for (int i = 0; i < Global.nrats; ++i) {
				if (getSide(Global.rats[i]) == 1)
					nrats++;
			}
			return (double)nrats/(Global.dimension*Global.dimension/2.0);
	}
	
	public Point movePiperTo(Point from, Point to) {
        double dx = to.x - from.x;
        double dy = to.y - from.y;
        double d = vectorLength(dx, dy);
        if (d <= Global.MUSICPIPER_SPEED * Global.TIMEUNIT)
            return to;
        else {
            double scale = (Global.MUSICPIPER_SPEED * Global.TIMEUNIT - Global.SMALLDISTANCE)/ d;
            double x = from.x + scale * dx;
            double y = from.y + scale * dy;
            if (x > Global.dimension)
                x = Global.dimension;
            if (y > Global.dimension)
                y = Global.dimension;
            return new Point(x, y);
        } 
    }
	
	 public double vectorLength(double dx, double dy) {
	        return Math.sqrt(dx * dx + dy * dy);
	 }

	 public static double distance(Point a, Point b) {
	        return Math.sqrt((a.x-b.x) * (a.x-b.x) +
	                         (a.y-b.y) * (a.y-b.y));
	 }


	public static boolean closetoWall (Point current) {
			boolean wall = false;
			if (Math.abs(current.x-Global.dimension)<Player.pspeed) {
				wall = true;
			}
			if (Math.abs(current.y-Global.dimension)<Player.pspeed) {
				wall = true;
			}
			if (Math.abs(current.y)<Player.pspeed) {
				wall = true;
			}
			return wall;
		}
		

    // use geometry to compute whether the sheep is on the line defined by dog movement or not
    public static boolean onTheLine(Point rat, Point piper, Point lastRoundPiper) {
        double a = distance(rat, piper);
        double b = distance(rat, lastRoundPiper);
        double c = distance(piper, lastRoundPiper);
        double s = (a + b + c) / 2;
        double area = Math.sqrt(s*(s-a)*(s-b)*(s-c));
        double distance = 2 * area / a;
        if (distance <= Global.SMALLDISTANCE)
            return true;
        return false;
    }
    
    // pick a closest piper for a particular rat
    static Point getClosestPiper(int ratId, Point[] pipers, Point[] rats) {
        int minPiper = -1;
        double mindist = Double.MAX_VALUE;
        for (int i = 0; i < pipers.length; ++i) {
            double d = distance(rats[ratId], pipers[i]);
            if (d < mindist && d != 0) { // ignore overlapping piper
                mindist = d;
                minPiper = i;
            }
        }
        return pipers[minPiper];
    }

}	
