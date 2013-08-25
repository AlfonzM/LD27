import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

public class Character {
	static boolean facingRight;
	static Point pos;
	static float speed;
	static float maxSpeed = 3, inertia = 0.2f, acceleration = 0.06f;
	static Vector2f move;
//	static Rectangle sprite;
	static Rectangle collisionBox;
	static Image sprite;
	
	public Character(int x, int y) throws SlickException{
		pos = new Point(x, y);
		move = new Vector2f(0, 0);
		collisionBox = new Rectangle(pos.getX(), pos.getY(), 20, 40);
		facingRight = true;
		sprite = new Image("res/sprite.png");
	}
	
	public static void render(Graphics g){
		g.drawString((int) pos.getX() + ", " + (int) pos.getY(), 10, 25);
		g.drawImage(sprite, pos.getX(), pos.getY());
	}
	
	public static void update(Input input){
		if(input.isKeyDown(Input.KEY_RIGHT)){
			move.x = speed;
			
			if(!facingRight) // stop on turn around
				speed /= 4;
			
			if(speed <= maxSpeed)
				speed += acceleration;
			
			facingRight = true;
		}
		else if(input.isKeyDown(Input.KEY_LEFT)){
			if(facingRight) // stop on turn around
				speed /= 4;

			move.x = -speed;
			
			if(!Level.solid[(int) ((pos.getX() + move.x)/Play.TS)][(int) (pos.getY()/Play.TS)] ||
			   !Level.solid[(int) ((pos.getX() + move.x)/Play.TS)][(int) ((pos.getY() + sprite.getHeight())/Play.TS)]){
				if(speed <= maxSpeed)
					speed += acceleration;
			}
			else{
				speed = 0;
				move.x = 0;
			}
			
			facingRight = false;
		}
		else{
			if(speed != 0){
				speed -= inertia;
				if(speed <= 0)
					speed = 0;				
			}
			
			if(facingRight)
				move.x = speed;
			else if(!facingRight)
				move.x = -speed;
			else
				move.x = 0;
		}
		
		// go up/down stairs
		if(input.isKeyPressed(Input.KEY_UP)){
			// check if center of sprite is inside door tiles
			int centerX = (int) ((pos.getX() + getBounds().getWidth()/2) /Play.TS);
			int centerY = (int) ((pos.getY() + getBounds().getHeight()/2) / Play.TS);
			System.out.println(centerX + ", " + centerY);
			
			int isInDoor = -1;
			
			for(int i = 0 ; i < 6 && isInDoor == -1; i++){
				Point temp1 = Level.door1.points.get(i);
				Point temp2 = Level.door2.points.get(i);
				if(temp1.getX() == centerX && temp1.getY() == centerY){ // collide with door1 tiles
					isInDoor = 1;
				}
				else if(temp2.getX() == centerX && temp2.getY() == centerY){
					isInDoor = 2;
				}
				
			}
			
			if(isInDoor == 1){ // pressed up while in door1, teleport to door2
				pos.setX(Level.door1.target.getX() * Play.TS + Play.TS/2);
				pos.setY((Level.door1.target.getY()+1) * Play.TS);
				speed = 0;
			}
			else if(isInDoor == 2){ // pressed up while in door2, teleport to door1
				pos.setX(Level.door2.target.getX() * Play.TS + Play.TS/2);
				pos.setY((Level.door2.target.getY()+1) * Play.TS);
				speed = 0;
			}
			
		}

		checkCollisions();
		move();
	}
	
	public static void move(){
		pos.setX(pos.getX() + move.x);
		pos.setY(pos.getY() + move.y);
//		sprite.setX(pos.getX());
//		sprite.setY(pos.getY());
	}
	
	public static void isBlocked(int newX, int newY){
		
	}
	
	public static boolean isBlockedLeft(){
		if(Level.solid[(int) pos.getX()/Play.TS][(int) pos.getY()/Play.TS]){
			return true;
		}
		else
			return false;
	}
	
	
	public static void checkCollisions(){
		// gravity
		if(!Level.solid[(int) pos.getX()/Play.TS][(int) pos.getY()/Play.TS + 2]){
			if(move.y < 10)
			move.y += 0.1f;
		}
		else{
			move.y = 0;
		}
		
//		// left
//		if(move.x < 0){
//			if(Level.solid[(int) pos.getX()/Play.TILESIZE][(int) pos.getY()/Play.TILESIZE] || 
//			   Level.solid[(int) pos.getX()/Play.TILESIZE][(int) pos.getY()/Play.TILESIZE + 1] ||
//			   pos.getX() < 0){
//				speed = 0;
//				move.x = 0;
//			}
//		}
//		// right
//		else if(move.x > 0){
//			if(Level.solid[(int) pos.getX()/Play.TILESIZE + 1][(int) pos.getY()/Play.TILESIZE] ||
//			   Level.solid[(int) pos.getX()/Play.TILESIZE + 1][(int) pos.getY()/Play.TILESIZE +1] ||
//			   pos.getX() + sprite.getWidth() > Main.GWIDTH){
//				speed = 0;
//				move.x = 0;
//			}
//		}
	}
	
	public static Rectangle getBounds(){
		return new Rectangle(pos.getX(), pos.getY(), sprite.getWidth(), sprite.getHeight());
	}
}
