import org.newdawn.slick.Animation;
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
	static int jumpMaxFrames = 50;
	static float gravity = 0.001f, jumpingGravity = 0.0005f, jumpSpeed = -0.20f;
	
	static Vector2f move;
	static Rectangle collisionBox;
	static Image sprite;
	
	static int delta;
	
	// sprites
	static Animation animation; // current animation
	static Animation walkLeft, walkRight, jumpLeft, jumpRight;
	static Animation idleRight, idleLeft;
	
	public Character(float f, float g) throws SlickException{
		pos = new Point(f * Play.TS, g * Play.TS);
		move = new Vector2f(0, 0);
		collisionBox = new Rectangle(pos.getX(), pos.getY(), 20, 40);
		facingRight = true;
		
		canJump = true;
		isJumping = false;
		jumpFrames = 0;
		
		// init animations
		
		// idle
		int[] idleDuration = { 100 };
		Image[] idleImages = { new Image("res/player/idle/1.png") };
		Image[] idleImages2 = { new Image("res/player/idle/2.png") };
		idleRight = new Animation(idleImages, idleDuration, true);
		idleLeft = new Animation(idleImages2, idleDuration, true);
		
		// walk
		int a = 20;
		int frameNum = 18;
		int[] walkDuration = new int[frameNum];
		Image[] leftImages = new Image[frameNum];
		Image[] rightImages = new Image[frameNum];
		for(int i = 0 ; i < frameNum; i++){
			rightImages[i] = new Image("res/player/runRight/run_" + (i+1) + ".png");
			leftImages[i] = new Image("res/player/runLeft/run_" + (i+1) + ".png");
			walkDuration[i] = a;
		}
		walkLeft = new Animation(leftImages, walkDuration, true);		
		walkRight = new Animation(rightImages, walkDuration, true);
		
		// jump
		int[] jumpDuration = { };
		Image[] leftJumpImages = { };
		Image[] rightJumpImages = { };
		jumpLeft = new Animation(leftJumpImages, jumpDuration, true);
		jumpRight = new Animation(rightJumpImages, jumpDuration, true);
		
		animation = idleRight;
	}
	
	public static void render(Graphics g){
		g.setColor(Color.black);
		
		// display collision box
//		g.fillRect(pos.getX(), pos.getY(), collisionBox.getWidth(), collisionBox.getHeight());
		
		if(!Play.gameOver)
			g.drawAnimation(animation, pos.getX()-7, pos.getY()-4);	
	}
	
	public static void update(Input input, int delta2) throws SlickException{
		
		if(!Play.gameOver){
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
				animation = walkRight;
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

				animation = walkLeft;
				facingRight = false;
			}
			else{ // idle
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
			
			// animations
			if(move.x == 0){
				if(facingRight)
					animation = idleRight;
				else
					animation = idleLeft;
			}
			
			// jump		
			if(input.isKeyDown(Input.KEY_SPACE)){
				if(!isJumping && onGround){
					int newY = (int) ((pos.getY() - move.y*delta)/Play.TS);
					//make sure head is not touching ceiling
					if(!Level.solid[(int) pos.getX()/Play.TS][newY] && !Level.solid[(int) pos.getX()/Play.TS + 1][newY]){
						move.y = jumpSpeed;
						isJumping = true;
						gravity = jumpingGravity;
						jumpFrames = 0;
					}
				}
			}
			
			if(isJumping){
				int newY = (int) ((pos.getY() - move.y*delta)/Play.TS);
				if(Level.solid[(int) pos.getX()/Play.TS][newY] || Level.solid[(int) pos.getX()/Play.TS + 1][newY]){
					// head touches roof
					pos.setY((newY+1) * Play.TS);
					isJumping = false;
					gravity = 0.001f;
					move.y = 0;
				}
				else{
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
			}
			// end jump
			
			// check if in doors
			int centerX = (int) ((pos.getX() + getBounds().getWidth()/2) / Play.TS);
			int centerY = (int) ((pos.getY()) / Play.TS);
			
			for(int i = 0; i < Level.portals.size(); i++){
				Door d = Level.portals.get(i);
				// check points1 and points2 of current door
				for(int j = 0 ; j < d.points1.size(); j++){
					if(d.points1.get(j).getX() == centerX && d.points1.get(j).getY() == centerY){
						if(input.isKeyPressed(Input.KEY_UP)){
							pos.setX(d.points2.get(0).getX() * Play.TS + Play.TS/2);
							pos.setY((d.points2.get(0).getY()+1) * Play.TS);
							speed = 0;
							Sounds.door.play();
						}
					}
					else if(d.points2.get(j).getX() == centerX && d.points2.get(j).getY() == centerY){
						if(input.isKeyPressed(Input.KEY_UP)){
							pos.setX(d.points1.get(0).getX() * Play.TS + Play.TS/2);
							pos.setY((d.points1.get(0).getY()+1) * Play.TS);
							speed = 0;
							Sounds.door.play();
						}
					}
				}
			}
			
			// check elevator
			for(int i = 0 ; i < Level.elevatorPoints.size() ; i++ ){
				Point p = Level.elevatorPoints.get(i);
				if(p.getX() == centerX && p.getY() == centerY){
					// display arrow to elevator
					if(input.isKeyPressed(Input.KEY_UP)){
						Play.initLevel();
						Sounds.elevator.play();
					}
				}
			}

			checkCollisions();
			move();
		}
		
	}
	
	public static void move(){
		if(pos.getX()+getBounds().getWidth()+move.x < Main.GWIDTH-5 && pos.getX()+move.x > 0)
			pos.setX(pos.getX() + move.x);
//		pos.setY(pos.getY() + move.y);
//		sprite.setX(pos.getX());
//		sprite.setY(pos.getY());
	}
	
	public static void checkCollisions(){
		// gravity
		int newY = (int) ((pos.getY()+ getBounds().getHeight() + move.y*delta)/Play.TS);

		if(newY < 30){
			if(!Level.solid[(int) pos.getX()/Play.TS][newY] && !Level.solid[(int) pos.getX()/Play.TS + 1][newY]){
				pos.setY(pos.getY() + move.y * delta);
				
				move.y += gravity * delta;
				onGround = false;
			}
			else{ // on ground
				pos.setY((newY-2)*Play.TS - 0.001f); // clip to lowest block
				move.y = 0;
				onGround = true;
				isJumping = false;
			}
		}
		
		if(newY >= 30){
			// the end
			Play.gameOver = true;
		}
	}
	
	public static Rectangle getBounds(){
		return new Rectangle(pos.getX(), pos.getY(), collisionBox.getWidth(), collisionBox.getHeight());
	}
}
