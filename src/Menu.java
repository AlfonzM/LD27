import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class Menu extends BasicGameState{
	
	Image home;
	
	Rectangle bar;

	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		
		new Sounds();
		home = new Image("res/homescreen.png");
		Sounds.music.loop(1, 0.5f);
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		g.setBackground(new Color(240, 240, 240, 1));
		g.drawImage(home, 0, 0);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta)	throws SlickException {
		Input input = gc.getInput();
		
		if(input.isKeyPressed(Input.KEY_SPACE)){
			sbg.enterState(0);
		}
	}

	@Override
	public int getID() {
		return 1;
	}

}
