import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Rectangle;


public class Pickable {
	Point pos;
	String value;
	Animation animation;
	
	public Pickable(int x, int y, String val) throws SlickException{
		pos = new Point(x * Play.TS, y * Play.TS);
		value = val;
		
		switch(val){
		case "med":
			// add animation
			Image[] sprites = new Image[2];
			sprites[0] = new Image("res/pill.png");
			sprites[1] = new Image("res/pill.png");
			animation = new Animation(sprites, new int[] { 100, 100 }, true);
			break;
		}
	}
	
	public Rectangle getBounds(){
		return new Rectangle(pos.getX(), pos.getY(), Play.TS, Play.TS);
	}
}
