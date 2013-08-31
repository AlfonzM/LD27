import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import org.newdawn.slick.AngelCodeFont;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.particles.ConfigurableEmitter;
import org.newdawn.slick.particles.ParticleIO;
import org.newdawn.slick.particles.ParticleSystem;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;


public class Play extends BasicGameState{
	public static int level;
	public static boolean gameOver, soundPlayed, loadingLevel;
	public static int TS = 20;
	public static int timer, theendTimer;
	public static Image redEffect, blackEffect;
	static float alpha, theendAlpha, creditsAlpha;
	static ArrayList<Pickable> meds;
	static ArrayList<Enemy> mobs;
	
	Sound die;
	
	Image theend, credits;
	
	Animation pill, elevatorIdle, elevatorOpen;
	
	// particles
	ConfigurableEmitter pillEmitter;
	ParticleSystem particles;
	
	// fonts
	AngelCodeFont font40, font24, font8, font16;

	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
//		font40 = new AngelCodeFont("res/fonts/04b03.fnt", new Image("res/fonts/04b03_0.png"));
		font24 = new AngelCodeFont("res/fonts/font24.fnt", new Image("res/fonts/font24_0.png"));
		font16 = new AngelCodeFont("res/fonts/font16.fnt", new Image("res/fonts/font16_0.png"));
//		font8 = new AngelCodeFont("res/fonts/04b03_8.fnt", new Image("res/fonts/04b03_8_0.png"));
		
		redEffect = new Image("res/redEffect.png");
		blackEffect = new Image("res/blackEffect.png");
		alpha = 0;
		redEffect.setAlpha(alpha);
		blackEffect.setAlpha(alpha);
		loadingLevel = true;
		
		gameOver = false;
		theendAlpha = 0;
		creditsAlpha = 0;
		theendTimer = 0;
		soundPlayed = false;
		die = new Sound("res/sounds/body.wav");
		
		new Sounds();
		
		// elevator sprites
		Image[] elevIdle = new Image[8];
		Image[] elevOpen = new Image[22];
		
		int a = 100, b = 100;
		int[] idleDur = { a, a, a, a, a, a, a, a };
		int[] openDur = { b, b, b, b, b, b, b, b, b, b, b, b, b, b, b, b, b, b, b, b, b, b };
		elevatorIdle = new Animation(elevIdle, idleDur, true);
		elevatorOpen = new Animation(elevOpen, openDur, true);
		
		level = 0;
		initLevel();
		
		// particles
		try{
			Image image = new Image("res/particle/square.png", false);
			particles = new ParticleSystem(image, 1500);
			particles.setRemoveCompletedEmitters(true);
			File xml = new File("res/particle/pillParticle.xml");
			pillEmitter = ParticleIO.loadEmitter(xml);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		particles.setBlendingMode(ParticleSystem.BLEND_ADDITIVE);
		ConfigurableEmitter e;
		e = pillEmitter.duplicate();
		e.setPosition(100, 100, false);
		
		e.setEnabled(false);
		particles.addEmitter(e);
		
		Image[] pills = new Image[11];
		int[] dur = new int[11];
		for(int i = 0 ; i < pills.length ; i++){
			pills[i] = new Image("res/pillAnimation/" + (i+1) + ".png");
			dur[i] = 80;
		}
		
		pill = new Animation(pills, dur, true);
		theend = new Image("res/theend.png");
		credits = new Image("res/credits.png");
	}
	
	public static void initLevel() throws SlickException{
		// game objects
		meds = new ArrayList<Pickable>();
		mobs = new ArrayList<Enemy>();
		alpha = 0;

		new Level(++level);
		new Character(Level.startPoint.getX(), Level.startPoint.getY()-1);
		timer = 10000;
		
		if(level == -1){
			Sounds.music.stop();
		}
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
			timerColor = Color.black;
		}
		
		// render meds
		for(int i = 0; i < meds.size(); i++ ){
			Pickable p = meds.get(i);
			g.drawAnimation(pill, p.pos.getX(), p.pos.getY());
		}
		
		particles.render();
		
		// render mobs
		for(int i = 0 ; i < mobs.size(); i ++){
			mobs.get(i).render(g);
		}
		
		// render player
		Character.render(g);
		
		redEffect.setAlpha(alpha);
		blackEffect.setAlpha(alpha);
		
		// render withdrawal syndrome effects
		if(timer < 6000){
			if(alpha < 0.7f)
				alpha += 0.002f;
			
			g.drawImage(redEffect, 0, 0);
			g.drawImage(blackEffect, 0, 0);
		}
		// render GUI (timer)
		if(level != -1)
			font16.drawString(Character.pos.getX(), Character.pos.getY() - 30, ""+ (timer/1000) + "." + ((timer%1000)/100) + ((timer%100)/10), timerColor);
		
		if(gameOver){
			if(theendTimer > 500 && !soundPlayed){
				// play sound
				die.play();
				soundPlayed = true;
			}
			
			if(theendTimer > 4000 && theendAlpha < 1)
				theendAlpha += 0.005f;
			
			if(theendTimer > 6000 && creditsAlpha < 1)
				creditsAlpha += 0.005f;
			
			theend.setAlpha(theendAlpha);
			credits.setAlpha(creditsAlpha);
			
			g.drawImage(theend, 700, 180);
			g.drawImage(credits, 700, 280);
		}
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		Input input = gc.getInput();
		Character.update(input, delta);

		for (Iterator<Enemy> iterator = mobs.iterator(); iterator.hasNext(); ) {
			Enemy e = iterator.next();
			e.update(delta);
		}
		
		// check collisions
		checkCollisions();
		
		if(input.isKeyPressed(Input.KEY_F5)){
			init(gc, sbg);
		}
		
		if(level != -1)
			timer -= delta;
		
		// timer runs out
		if(timer < -2){
			Sounds.die.play();
			level--;
			initLevel();
		}
		
		if(gameOver && theendTimer < 9000){
			theendTimer += delta;
		}
		
		particles.update(delta);
		
		if(input.isKeyPressed(Input.KEY_ENTER)){
			initLevel();
		}
	}
	
	public void checkCollisions(){
		
		// loop all pickables
		for (Iterator<Pickable> iterator = meds.iterator(); iterator.hasNext(); ) {
			Pickable p = iterator.next();
			if(Character.getBounds().intersects(p.getBounds())){ // pickup med
				Sounds.pickPill.play();
				iterator.remove();
				timer = 10000;
				alpha = 0;
			}
		}
	}

	@Override
	public int getID() {
		return 0;
	}

}
