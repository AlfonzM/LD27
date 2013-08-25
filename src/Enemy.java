import org.newdawn.slick.Animation;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Rectangle;


public class Enemy {
	Point pos;
	
	int x1, x2;
	
	int distance, delay, timer;
	float speed = 3;
	Rectangle vision;
	
	Animation animation;
	
	static enum Direction {left, right};
	static enum EType {doc, nurse};
	Direction dir;
	
	public Enemy(int x, int y, int distance, int visionRange, int delay, Direction direction, EType type){
		pos = new Point(x, y);
		this.distance = distance;
		this.vision = new Rectangle(0, 0, visionRange, Play.TS*2);
		this.delay = delay;
		this.dir = direction;
		
		if(dir == Direction.left){
			x1 = x;
			x2 = x + distance;
			vision.setX(pos.getX() - vision.getWidth());
		}
		else{
			x1 = x - distance;
			x2 = x;
			vision.setX(pos.getX() + Play.TS);
		}
		
		timer = 0;
	}
	
	public void update(int delta){
		timer += delta;
		
		if(timer >= delay){
			dir = (dir == Direction.left) ? Direction.right : Direction.left;
			if(dir == Direction.left){
				// walk right
				pos.setX(pos.getX() + speed);
				vision.setX(pos.getX() + Play.TS);
				if(pos.getX() >= x2){
					timer = 0; // idle for delay secs			
				}
			}
			else{
				// walk left
				pos.setX(pos.getX() - speed);
				vision.setX(pos.getX() - vision.getWidth());
				if(pos.getX() <= x1){
					timer = 0; // idle for delay secs
				}
			}
		}
	}
	
	public void render(Graphics g){
	}
	
	public Rectangle getVisionBounds(){
		return vision;
	}
}