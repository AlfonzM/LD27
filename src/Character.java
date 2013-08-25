import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

public class Character {
	static boolean facingRight;
	static boolean onGround, canJump, isJumping;
	static int jumpFrames;
	static Point pos;
	static float speed;
	
	// constant values
	static float maxSpeed = 3, inertia = 0.2f, acceleration = 0.06f;
	static int jumpMaxFrames = 60;
	static float gravity = 0.001f, jumpingGravity = 0.0005f, jumpSpeed = -0.27f;
	
	static Vector2f move;
//	static Rectangle sprite;
	static Rectangle collisionBox;
	static Image sprite;
	
	static int delta;
	
	public Character(int x, int y) throws SlickException{
		pos = new Point(x * Play.TS, y * Play.TS);
		move = new Vector2f(0, 0);
		collisionBox = new Rectangle(pos.getX(), pos.getY(), 20, 40);
		facingRight = true;
		sprite = new Image("res/sprite.png");
		
		canJump = true;
		isJumping = false;
		jumpFrames = 0;
	}
	
	public static void render(Graphics g){
		g.drawString((int) pos.getX() + ", " + (int) pos.getY(), 10, 25);
//		g.drawImage(sprite, pos.getX(), pos.getY());
		g.setColor(Color.black);
		g.fillRect(pos.getX(), pos.getY(), collisionBox.getWidth(), collisionBox.getHeight());
	}
	
	public static void update(Input input, int delta2) throws SlickException{
		
		delta = delta2;
		
		// left and right movement
		if(input.isKeyDown(Input.KEY_RIGHT)){
			if(!facingRight) // stop on turn around
				speed /= 4;
			
			move.x = speed;
			
			int newX = (int) (pos.getX() + Play.TS + move.x)/Play.TS;
			int newY = (int) ((pos.getY()) /Play.TS) + 1;
			
			if(!Level.solid[newX][newY] && !Level.solid[newX][newY + 1]){
				if(speed <= maxSpeed)
					speed += acceleration;
			}
			else{
				pos.setX((newX-1)*Play.TS - 0.01f); // stick to right most empty block
				// stop movement
				speed = 0;
				move.x = 0;
			}
			
			facingRight = true;
		}
		else if(input.isKeyDown(Input.KEY_LEFT)){
			if(facingRight) // stop on turn around
				speed /= 4;

			move.x = -speed;
			
			int newX = (int) (pos.getX() + move.x)/Play.TS;
			int newY = (int) (pos.getY()/Play.TS) + 1;
			
			if(!Level.solid[newX][newY] && !Level.solid[newX][newY + 1]){
				if(speed <= maxSpeed)
					speed += acceleration;
			}
			else{
				pos.setX((newX+1)*Play.TS); // stick to the left most solid block
				// stop movement
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
		// end left and right movement
		
		// jump		
		if(input.isKeyPressed(Input.KEY_SPACE)){
			if(!isJumping && onGround){
				move.y = jumpSpeed;
				isJumping = true;
				gravity = jumpingGravity;
				jumpFrames = 0;
			}
		}
		
		System.out.println(gravity);
		
		if(isJumping){
			if(input.isKeyDown(Input.KEY_SPACE)){
				if(jumpFrames < jumpMaxFrames){
					jumpFrames++;
				}
				else{
					isJumping = false;
					gravity = 0.001f;
				}
			}
			else{
				isJumping = false;
				gravity = 0.001f;
			}
		}
		
		
//		if(input.isKeyDown(Input.KEY_SPACE)){
//			if(canJump && move.y > -0.2f)
//				move.y -= 0.07f;
//			
//			if(move.y >= 0.3f)
//				canJump = false;
//		}
		
//		System.out.println(move.y);
		// end jump
		
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
				if(temp1.getX() == centerX && temp1.getY() == centerY){ // inside door1 tiles
					isInDoor = 1;
				}
				else if(temp2.getX() == centerX && temp2.getY() == centerY){ // inside door2 tiles
					isInDoor = 2;
				}
				else{
					Point exit = Level.exit.points.get(i);
					if(exit.getX() == centerX && exit.getY() == centerY){ // inside exit tiles
						isInDoor = 3;
					}
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
			else if(isInDoor == 3){
				//exit
				Play.initLevel();
			}
			
		}

		checkCollisions();
		move();
	}
	
	public static void move(){
		pos.setX(pos.getX() + move.x);
//		pos.setY(pos.getY() + move.y);
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
		int newY = (int) ((pos.getY()+ getBounds().getHeight() + move.y*delta)/Play.TS);
		
		if(!Level.solid[(int) pos.getX()/Play.TS][newY] && !Level.solid[(int) pos.getX()/Play.TS + 1][newY]){
			pos.setY(pos.getY() + move.y * delta);
			move.y += gravity * delta;
			onGround = false;
		}
		else{ // on ground
			pos.setY((newY-2)*Play.TS - 0.001f);
			move.y = 0;
			onGround = true;
		}
		
//		System.out.println(move.y);
		
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
