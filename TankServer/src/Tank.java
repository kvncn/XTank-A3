/**
 * @author Kate Nixon
 * FILE: Tank.java
 * ASSIGNMENT: A3 - XTank
 * COURSE: CSC 335; FALL 2022
 * PURPOSE: This class represents the tank in
 * XTank. 
 * 
 * There are 3 different types of tanks:
 * standard, turtle, and bomb. 
 * Standard and bomb have the same health (100) while a turtle
 * tank has double. The bomb tanks also have special bullets,
 * which are detailed in the bullet class.
 * 
 * The tank should keep track of its x, y position along
 * with its angle, type, health, and color.
 * 
 */
public class Tank {
	
	private int xPos;
	private int yPos; 
	private int angle; 
	private Integer id;
	private Bullet shot; 
	private String type;
	private int health;
	private String color;
	
	/**
	 * Initializes the tank with its x,y coor along with
	 * angle(always facing up to begin with) along with its id
	 * (which is just the number representing the order in which
	 * the client appeared) and type of tank
	 * @param initialX
	 * @param initialY
	 * @param initialAngle
	 * @param id
	 * @param type
	 */
	public Tank(int initialX, int initialY, int initialAngle, Integer id, String type) {
		xPos = initialX;
		yPos = initialY;
		angle = initialAngle;
		this.id = id;
		shot = new Bullet(type);
		this.type = type;
		if(type.equalsIgnoreCase("turtle")) {
			color = "green";
			health = 200;
		}else if(type.equalsIgnoreCase("turtle")) {
			color = "red";
			health = 100;
		}else {
			color = "blue";
			health = 100;
		}
	}
	
	/**
	 * @return String representing the color
	 */
	public String getColor() {
		return color;
	}
	
	/**
	 * tank was hit. Damage from bullet
	 * is subtracted from health
	 * @param damage
	 */
	public void hit(int damage) {
		health -= damage;
		System.out.println("HEALTH IS NOW " + health);
	}
	
	/**
	 * Method that tells if death has occured
	 * @return boolean, 
	 */
	public boolean death() {
		return health == 0;
	}
	
	/**
	 * return int representation of type of tank
	 * @return
	 */
	public int getType() {
		if(type.equalsIgnoreCase("turtle")) {
			return 1;
		}else if(type.equalsIgnoreCase("bomb")) {
			return 2;
		}
		//0 represents standard type
		return 0;
	}
	
	/**
	 * moves x position
	 * @param xMove
	 */
	public void xMove(int xMove) {
		xPos = xMove;
	}
	
	/**
	 * moves y position
	 * @param yMove
	 */
	public void yMove(int yMove) {
		yPos = yMove;
		
	}
	
	/**
	 * changes the angle of the tank
	 * @param angle
	 */
	public void changeAngle(int angle) {
		this.angle = angle;
	}
	
	/**
	 * get x position
	 * @return
	 */
	public int getXpos() {
		return xPos;
	}
	
	/**
	 * gets y position
	 * @return
	 */
	public int getYpos() {
		return yPos;
	}

	/**
	 * gets angle of tank
	 * @return
	 */
	public int getAngle() {
		return angle;
	}
	
	/**
	 * gets id of tank
	 * @return
	 */
	public int getID() {
		return id;
	}
	
	/**
	 * adds bullet to be associated with tank
	 * @param shot
	 */
	public void addShot(Bullet shot) {
		this.shot = shot;
		
	}
	
	/**
	 * returns bullet associated with tank
	 * @return
	 */
	public Bullet getShot() {
		return shot;
	}
	
	/**
	 * prints out string representation of tank
	 * which shows which direction it is pointed at
	 * 
	 */
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
