import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;


public class Sounds {
	public static Sound pickPill, die, door, elevator, music;
	
	public Sounds() throws SlickException{
		pickPill = new Sound("res/sounds/pickpill.wav");
		die = new Sound("res/sounds/die.wav");
		door = new Sound("res/sounds/door.wav");
		elevator = new Sound("res/sounds/elevator.wav");
		music = new Sound("res/sounds/music.wav");
	}

}
