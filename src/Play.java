import java.util.ArrayList;
import java.util.Iterator;

import org.newdawn.slick.AngelCodeFont;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;


public class Play extends BasicGameState{
	public static int level;
	public static int TS = 20;
	public static int timer;
	static ArrayList<Pickable> meds;
	
	// fonts
	AngelCodeFont font40, font24, font8, font16;

	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
//		font40 = new AngelCodeFont("res/fonts/04b03.fnt", new Image("res/fonts/04b03_0.png"));
		font24 = new AngelCodeFont("res/fonts/font24.fnt", new Image("res/fonts/font24_0.png"));
		font16 = new AngelCodeFont("res/fonts/font16.fnt", new Image("res/fonts/font16_0.png"));
//		font8 = new AngelCodeFont("res/fonts/04b03_8.fnt", new Image("res/fonts/04b03_8_0.png"));
		
		level = 0;
		initLevel();
	}
	
	public static void initLevel() throws SlickException{
		// game objects
		meds = new ArrayList<Pickable>();
		
		new Character(2, 15);
		new Level(++level);
		timer = 10000;
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		int color = 240;
		g.setBackground(new Color(color, color, color, 1));
		
		// render map
		Level.map.render(0, 0);
		
		Color timerColor;
		if(timer >= 3000){
			timerColor = Color.black;
		}
		else{
			timerColor = Color.red;
		}
		
		// render GUI (timer)
		font16.drawString(Character.pos.getX(), Character.pos.getY() - 30, ""+ (timer/1000) + "." + ((timer%1000)/100) + ((timer%100)/10), timerColor);
		
		// render meds
		for(int i = 0; i < meds.size(); i++ ){
			Pickable p = meds.get(i);
			g.drawAnimation(p.animation, p.pos.getX(), p.pos.getY());
		}
		
		// render player
		Character.render(g);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		Input input = gc.getInput();
		Character.update(input, delta);
		
		// check collisions
		checkCollisions();
		
		if(input.isKeyPressed(Input.KEY_F5)){
			init(gc, sbg);
		}
		
		timer -= delta;
		if(timer < -2)
			init(gc, sbg);
	}
	
	public void checkCollisions(){
		
		// loop all pickables
		for (Iterator<Pickable> iterator = meds.iterator(); iterator.hasNext(); ) {
			Pickable p = iterator.next();
			if(Character.getBounds().intersects(p.getBounds())){ // pickup med
				iterator.remove();
				timer = 10000;
			}
		}
	}

	@Override
	public int getID() {
		return 0;
	}

}
