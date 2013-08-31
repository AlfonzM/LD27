import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Rectangle;


public class Enemy {
	Point pos;
	
	int x1, x2;
	
	int distance, delay, timer;
	float speed;
	Rectangle vision, sprite;
	
	Animation animation, wLeft, wRight, iLeft, iRight;
	
	static enum Direction {left, right};
	static enum EType {doc, nurse};
	enum MoveType { pause, walkLeft, walkRight };
	MoveType moveType;
	Direction dir;
	
	public Enemy(int x, int y, int distance, int visionRange, int delay, Direction direction, EType type) throws SlickException{
		x *= Play.TS;
		y *= Play.TS;
		distance *= Play.TS;
		
		pos = new Point(x, y);
		
		// sprite
		if(type == EType.doc){ // doctor
			Image[] leftImages = new Image[5];
			Image[] rightImages = new Image[5];
			Image[] idleLeft = { new Image("res/enemies/doctor/left/d1.png")};
			Image[] idleRight = { new Image("res/enemies/doctor/right/d1.png")};
			
			for(int i = 0 ; i < leftImages.length; i++ ){
				leftImages[i] = new Image("res/enemies/doctor/left/d" + (i+1) + ".png");
				rightImages[i] = new Image("res/enemies/doctor/right/d" + (i+1) + ".png");
			}
			
			int a = 200;
			int[] walkDuration = { a, a, a, a, a };
			int[] idleDuration = { 100 };
			wLeft = new Animation(leftImages, walkDuration, true);
			wRight = new Animation(rightImages, walkDuration, true);
			iLeft = new Animation(idleLeft, idleDuration, true);
			iRight = new Animation(idleRight, idleDuration, true);
			
			sprite = new Rectangle(pos.getX(), pos.getY(), 1*Play.TS, 2*Play.TS);
			speed = 1.5f;
			
			animation = (direction == Direction.left)? iLeft: iRight;
		}
		else{ // nurse
			Image[] leftImages = new Image[5];
			Image[] rightImages = new Image[5];
			Image[] idleLeft = { new Image("res/enemies/nurse/left/1.png")};
			Image[] idleRight = { new Image("res/enemies/nurse/right/1.png")};
			
			for(int i = 0 ; i < leftImages.length; i++ ){
				leftImages[i] = new Image("res/enemies/nurse/left/" + (i+1) + ".png");
				rightImages[i] = new Image("res/enemies/nurse/right/" + (i+1) + ".png");
			}
			
			int a = 200;
			int[] walkDuration = { a, a, a, a, a };
			int[] idleDuration = { 100 };
			wLeft = new Animation(leftImages, walkDuration, true);
			wRight = new Animation(rightImages, walkDuration, true);
			iLeft = new Animation(idleLeft, idleDuration, true);
			iRight = new Animation(idleRight, idleDuration, true);
			
			sprite = new Rectangle(pos.getX(), pos.getY(), 1*Play.TS, 2*Play.TS);
			speed = 2;

			animation = (direction == Direction.left)? iLeft: iRight;
		}		
		
		// vision
		this.vision = new Rectangle(pos.getX(), pos.getY(), Play.TS * visionRange + sprite.getWidth(), 2);
		
		this.delay = delay;
		this.dir = direction;
		
		moveType = MoveType.pause;
		
		if(dir == Direction.left){
			x1 = x;
			x2 = x + distance;
			vision.setX(pos.getX() - vision.getWidth() + sprite.getWidth());
		}
		else{
			x1 = x - distance;
			x2 = x;
			vision.setX(pos.getX());
		}
		
		timer = 0;
	}
	
	public void update(int delta){
		if(timer >= delay && moveType == MoveType.pause){
			// change direction
			if(dir == Direction.left){
				dir = Direction.right;
				moveType = MoveType.walkRight;
			}
			else{
				dir = Direction.left;
				moveType = MoveType.walkLeft;
			}
		}
		
		if(moveType == MoveType.walkLeft){
			animation = wLeft;
			
			pos.setX(pos.getX() - speed);
			vision.setX(pos.getX() + sprite.getWidth() - vision.getWidth());
			if(pos.getX() < x1){
				moveType = MoveType.pause;
				timer = 0; // idle for delay secs
			}			
		}
		else if(moveType == MoveType.walkRight){
			animation = wRight;
			
			pos.setX(pos.getX() + speed);
			vision.setX(pos.getX() + Play.TS - sprite.getWidth());
			if(pos.getX() > x2){
				moveType = MoveType.pause;
				timer = 0; // idle for delay secs			
			}			
		}
		else if(moveType == MoveType.pause){
			timer += delta;
			animation = (dir == Direction.left)? iLeft: iRight;
		}
	}
	
	public void render(Graphics g) throws SlickException{
		
		// vision detection
		if(Character.getBounds().intersects(vision)){
			Sounds.die.play();
			Play.level--;
			Play.initLevel();
		}

		// render vision line
		g.setColor(Color.red);
		g.fillRect(vision.getX(), pos.getY() + 5, vision.getWidth(), vision.getHeight());
		
		// render enemy
		g.setColor(Color.gray);
//		g.fillRect(pos.getX(), pos.getY(), sprite.getWidth(), sprite.getHeight());
		g.drawAnimation(animation, pos.getX() - 7, pos.getY() - 5);
		
	}
}