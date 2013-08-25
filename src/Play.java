import java.util.ArrayList;
import java.util.Iterator;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;


public class Play extends BasicGameState{
	
	public static int TS = 20;
	public static int timer;
	static ArrayList<Pickable> meds;

	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		// game objects
		meds = new ArrayList<Pickable>();
		
		new Character(10, 300);
		new Level(1);
		timer = 10000;
		
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		int color = 240;
		g.setBackground(new Color(color, color, color, 1));
		g.setColor(Color.black);
		g.drawString(""+ (timer/1000 + 1), 10, 40);
		Level.map.render(0, 0);
		
		// render meds
		for(int i = 0; i < meds.size(); i++ ){
			Pickable p = meds.get(i);
			g.drawAnimation(p.animation, p.pos.getX(), p.pos.getY());
		}
		
		Character.render(g);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		Input input = gc.getInput();
		Character.update(input);
		
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
