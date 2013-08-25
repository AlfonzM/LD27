import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

public class Level {
	
	static TiledMap map;
	static boolean[][] solid;
	static Door door1, door2;
	
	public Level(int level) throws SlickException{
		map = new TiledMap("res/levels/1.tmx");
		solid = new boolean[map.getWidth()][map.getHeight()];
		
		for(int x = 0 ; x < map.getHeight(); x++){
			for(int y = 0 ; y < map.getWidth(); y++){
				int tileId = map.getTileId(y, x, 0);
				
				switch(tileId){
				case 1: // solid block
					solid[y][x] = true;
					break;
				
				case 2: // door 1
					door1 = new Door(y-1, x-2);
					break;
					
				case 3: // door 2
					door2 = new Door(y-1, x-2);
					break;
				
				case 4: // meds
					Play.meds.add(new Pickable(y, x, "med"));
					break;
				
				default:
					break;
				}

//				System.out.print(tileId + " ");
			}
//			System.out.println();
		}
		
		door1.target = door2.getLocation();
		door2.target = door1.getLocation();
	}
}
