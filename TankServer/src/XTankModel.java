import java.util.ArrayList;
import java.util.List;
/**
 * @author Kevin Nisterenko
 * FILE: XTankModel.java
 * ASSIGNMENT: A3 - XTank
 * COURSE: CSC 335; FALL 2022
 * PURPOSE: This class sets up the Model in our MVC which is used to
 * store the state of our Xtank Game and use that to communicate 
 * between each client. 
 * 
 * The model contains information about the (random) maze being used in the 
 * game along with the List<Tank> active in the game and the List<Bullet> 
 * active in the game
 * 
 */
public class XTankModel {
	private int[] maze = {100, 100, 100, 400, 700, 100, 950, 100,
			450, 650, 450, 750, 650, 650, 650, 750, 450, 650, 650, 650, 450, 750, 650, 750};
	private List<Tank> tanks;
	private List<Bullet> shots; 
	
	/**
	 * initializes the model with empty lists of tank and bullet 
	 * objects that get updated as clients join the server
	 */
	public XTankModel() {
		tanks = new ArrayList<Tank>();
		shots = new ArrayList<Bullet>();
	}
	
	/**
	 * adds a new tank to the model
	 * is triggered when client joins server
	 * @param newTank
	 */
	public void addTank(Tank newTank) {
		tanks.add(newTank);
	}
	
	/**
	 * adds new bullet to the game
	 * is triggered when client presses space bar
	 * @param tank
	 * @param angle
	 */
	public void addShot(Tank tank, int angle) {
		Bullet shot = new Bullet(tank);
		tank.addShot(shot);
		shots.add(shot);
	}
	
	/**
	 * This method moves the bullets.
	 * If bullet hits the maze, it is removed from the model.
	 * When the bullets are done moving, 2 is returned
	 * to change the mode of the game.
	 * @return
	 */
	public int moveBullets() {
		for (Tank tank : tanks) {
			Bullet shot = tank.getShot();
			Boolean remove = shot.move();
			int shotX = shot.getXpos();
			int shotY = shot.getYpos();
			for (int i = 0; i < maze.length - 3; i += 4) {
				int startX = maze[i];
				int startY = maze[i+1];
				int endX = maze[i+2];
				int endY = maze[i+3];
			
				// maze line boundaries
				if (shotX + 60 >= startX && shotX - 10 <= endX) {
					if (shotY + 60 >= startY && shotY - 10 <= endY) {
						remove = true; 
						break;
					}
				}
	  		}
			
			if (remove) {
				shots.remove(tank.getShot());
				Bullet inv = new Bullet();
				tank.addShot(inv);
				return 2;
			}
		}
		return 2;
	}
	
	/**
	 * This method checks if the bullets hit another tank
	 * If the tank is hit by an enemy bullet and that
	 * changes the health to 0, then the tank is removed 
	 * from the game
	 */
	public void regHits() {
		//System.out.println("LETS CHECK THEM HITS");
		for (Bullet shot : shots) {
			//System.out.println("SHOT: " + shot);
			for (Tank tank : tanks) {
				if (shot.getShooter().equals(tank)) continue;
				if (shot.getXpos() >= tank.getXpos() - 20 && shot.getXpos() <= tank.getXpos() + 20) {
					//System.out.println("CHECKING Y");
					if (shot.getYpos() >= tank.getYpos() - 20 && shot.getYpos() <= tank.getYpos() +20) {
						//System.out.println("DAMAGE IS " + shot.getDamage());
						tank.hit(shot.getDamage());
						if(tank.death()) {
							tanks.remove(tank);
							shots.remove(shot);
							return;
						}
						shots.remove(shot);
						return;
					}
				}
			}
		}
	}
	
	/**
	 * returns the list of current tanks
	 * @return
	 */
	public List<Tank> getTanks() {
		return tanks;
	}
}
