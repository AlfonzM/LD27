import java.util.ArrayList;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.tiled.TiledMap;

public class Level {
	static Point startPoint;
	static TiledMap map;
	static boolean[][] solid;
	static ArrayList<Door> portals;
	static ArrayList<Point> elevatorPoints;
	
	public Level(int level) throws SlickException{
		portals = new ArrayList<Door>();
		elevatorPoints = new ArrayList<Point>();
		
		try{
			map = new TiledMap("res/levels/"+level+".tmx");
		}
		catch(Exception e){
			Play.level = -1;
			map = new TiledMap("res/levels/rooftop.tmx");
		}
		solid = new boolean[map.getWidth()][map.getHeight()];
		
		for(int x = 0 ; x < map.getHeight(); x++){
			for(int y = 0 ; y < map.getWidth(); y++){
				int tileId = map.getTileId(y, x, 2);
				
				switch(tileId){
				case 1: // solid block
					solid[y][x] = true;
					break;
				
				case 4: // meds
					Play.meds.add(new Pickable(y, x, "med"));
					break;
					
				case 5: // elevator
					// elevator
					for(int i = 0 ; i < 3; i++){
						for(int j = 0 ; j < 3 ; j++){
							elevatorPoints.add(new Point((y+i), (x+j)));
						}
					}
					break;					
				
				case 25:
					startPoint = new Point(y, x);
					break;
					
				default:
					break;
				}
			}
		}
		
		// manually add portals and mobs
		switch(level){
		case 1:
			portals.add(new Door(26, 25, 28, 11));
			break;
			
		case 2:
			portals.add(new Door(44, 25, 44, 11));
			break;
			
		case 3:
			portals.add(new Door(43, 25, 39, 11));
			Play.mobs.add(new Enemy(32, 26, 7, 6, 3000, Enemy.Direction.left, Enemy.EType.doc));
			break;
			
		case 4:
			portals.add(new Door(38, 25, 43, 11));
			Play.mobs.add(new Enemy(23, 12, 12, 2, 0, Enemy.Direction.right, Enemy.EType.nurse));
			break;
			
		case 5:
			portals.add(new Door(46, 25, 31, 11));
			portals.add(new Door(24, 21, 15, 1));
			Play.mobs.add(new Enemy(28, 26, 11, 6, 3000, Enemy.Direction.left, Enemy.EType.doc));
			Play.mobs.add(new Enemy(39, 26, 10, 6, 3000, Enemy.Direction.right, Enemy.EType.doc));
			Play.mobs.add(new Enemy(14, 12, 35, 4, 0, Enemy.Direction.left, Enemy.EType.nurse));
			break;
			
		default:
			break;
		}
	}
}
