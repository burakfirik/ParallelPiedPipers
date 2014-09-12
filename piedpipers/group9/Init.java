package piedpipers.group9;

import java.util.ArrayList;

import piedpipers.sim.Point;

/*
 * Place all Init methods here
 * 
 * */

public  class Init{
	
	
	public static int[] initThetas() {
		// TODO Auto-generated method stub
		int [] thetas=new int[Global.npipers];
		int theta=0;
		
		thetas = new int[Global.npipers];
		for (int i=0; i< Global.npipers; i++) {
			theta = (180 * i)/ (Global.npipers) + (180)/ (2 * Global.npipers);
			thetas[i]=theta;
			System.out.println("THETAS EQUAL : " + thetas[i]);
		}
		Global.initThetas=true;
		return thetas;
	}

	//initialize Gloabl variables to give access to other classes
	public static void initGlobal(Point[] pipers, Point[] rats,int dimension) {
		Global.pipers=pipers;
		Global.rats=rats;
		Global.npipers=pipers.length;
		Global.nrats=rats.length;
		Global.initGlobal=true;
		Global.dimension=dimension;
		Global.initGlobal=true;
	}

	public static void initAssignRats() {
		// TODO Auto-generated method stub
		Global.assignedRats = new ArrayList<ArrayList<Point>>();
		for(int i = 0; i < Global.npipers; i++){
			Global.assignedRats.add(new ArrayList<Point>());
		}
		
	}
	

	
}
