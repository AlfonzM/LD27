import java.util.ArrayList;

import org.newdawn.slick.geom.Point;

public class Door {
	ArrayList<Point> points1, points2;
	Point target;
	
	static enum DoorType { door, vent, elevator };
	
	public Door(int x, int y, int x2, int y2){
		points1 = new ArrayList<Point>();
		points2 = new ArrayList<Point>();
		
		points1.add(new Point(x, y));
		points1.add(new Point(x+1, y));
		points1.add(new Point(x, y+1));
		points1.add(new Point(x+1, y+1));
		points1.add(new Point(x, y+2));
		points1.add(new Point(x+1, y+2));
		
		for(int i = 0 ; i < points1.size() ; i ++){
//			System.out.println(points1.get(i).getX() + ", " + points1.get(i).getY());
		}

		points2.add(new Point(x2, y2));
		points2.add(new Point(x2+1, y2));
		points2.add(new Point(x2, y2+1));
		points2.add(new Point(x2+1, y2+1));
		points2.add(new Point(x2, y2+2));
		points2.add(new Point(x2+1, y2+2));
		
		for(int i = 0 ; i < points2.size() ; i ++){
//			System.out.println(points2.get(i).getX() + ", " + points2.get(i).getY());
		}
	}
	
	public Point[] getLocation(){
		Point[] toReturn = new Point[2];
		toReturn[0] = points1.get(0);
		toReturn[1] = points2.get(0);
		return toReturn;
	}
}