import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;


public class Main extends StateBasedGame{
	
	public static int GWIDTH = 1000, GHEIGHT = 600;

	public Main() {
		super("LD27");
		// TODO Auto-generated constructor stub
	}

	@Override
	public void initStatesList(GameContainer gc) throws SlickException {
		this.addState(new Play());
		this.enterState(0);
		
	}
	
	public static void main(String args[]) throws SlickException{
		AppGameContainer appgc = new AppGameContainer(new Main());
		appgc.setDisplayMode(GWIDTH, GHEIGHT, false);
		appgc.setTargetFrameRate(60);
		appgc.setShowFPS(true);
		appgc.start();
	}

}
