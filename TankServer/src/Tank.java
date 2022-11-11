//
public class Tank {
	
	private int xPos;
	private int yPos; 
	private int angle; 
	private Integer id;
	private Bullet shot; 
	
	public Tank(int initialX, int initialY, int initialAngle, Integer id) {
		xPos = initialX;
		yPos = initialY;
		angle = initialAngle;
		this.id = id;
		shot = new Bullet();
	}
	
	public void xMove(int xMove) {
		xPos = xMove;
	}
	
	public void yMove(int yMove) {
		yPos = yMove;
		
	}
	
	public void changeAngle(int angle) {
		this.angle = angle;
	}
	
	public int getXpos() {
		return xPos;
	}
	
	public int getYpos() {
		return yPos;
	}
	
	public int getAngle() {
		return angle;
	}
	
	public int getID() {
		return id;
	}
	
	public void addShot(Bullet shot) {
		this.shot = shot;
	}
	
	public Bullet getShot() {
		return shot;
	}
	
	public String toString() {
		String ret = "";
		if (angle == 0) {
			ret += ">>";
		} else if (angle == 1) {
			ret += "<<";
		} else if (angle == 2) {
			ret += " /\\";
		} else if (angle == 3) {
			ret += " \\/";
		}
		ret += id.toString();
		return ret;
	}

}
