import java.util.ArrayList;

import org.newdawn.slick.geom.Point;

public class Door {
	ArrayList<Point> points;
	Point target;
	
	public Door(int x, int y){
		points = new ArrayList<Point>();
		
		points.add(new Point(x, y));
		points.add(new Point(x+1, y));
		points.add(new Point(x, y+1));
		points.add(new Point(x+1, y+1));
		points.add(new Point(x, y+2));
		points.add(new Point(x+1, y+2));
	}
	
	public Point getLocation(){
		return points.get(0);
	}
}
