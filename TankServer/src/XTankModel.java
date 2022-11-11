import java.util.ArrayList;
import java.util.List;

public class XTankModel {
	private int[] maze = {100, 100, 100, 400, 700, 100, 950, 100,
			450, 650, 450, 750, 650, 650, 650, 750, 450, 650, 650, 650, 450, 750, 650, 750};
	private List<Tank> tanks;
	private List<Bullet> shots; 
	
	public XTankModel() {
		tanks = new ArrayList<Tank>();
		shots = new ArrayList<Bullet>();
	}
	
	public void addTank(Tank newTank) {
		tanks.add(newTank);
	}
	
	public void addShot(Tank tank, int angle) {
		Bullet shot = new Bullet(tank);
		tank.addShot(shot);
		shots.add(shot);
	}
	
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
	
	public void regHits() {
		//System.out.println("LETS CHECK THEM HITS");
		for (Bullet shot : shots) {
			//System.out.println("SHOT: " + shot);
			for (Tank tank : tanks) {
				if (shot.getShooter().equals(tank)) continue;
				if (shot.getXpos() >= tank.getXpos() - 20 && shot.getXpos() <= tank.getXpos() + 20) {
					//System.out.println("CHECKING Y");
					if (shot.getYpos() >= tank.getYpos() - 20 && shot.getYpos() <= tank.getYpos() +20) {
						tanks.remove(tank);
						shots.remove(shot);
						return;
					}
				}
			}
		}
	}
	
	public List<Tank> getTanks() {
		return tanks;
	}
}