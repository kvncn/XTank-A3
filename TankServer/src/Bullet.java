/**
 * @author Kate Nixon
 * FILE: Bullet.java
 * ASSIGNMENT: A3 - XTank
 * COURSE: CSC 335; FALL 2022
 * PURPOSE: This class represents the bullet used in
 * XTank. 
 * 
 * The bullet can belong to 3 different types of tanks:
 * standard, turtle, and bomb. Both the standard and turtle
 * do 50 damage to enemy tanks while the bomb does 100 damage
 * to enemy tanks.
 * 
 * The bullet should keep track of its x, y position along
 * with the angle it was shot at along with the tank it
 * belongs to.
 * 
 */
public class Bullet {
	private int xPos;
	private int yPos;
	private int angle;
	private Tank shooter;
	private int damage;
	private String type;
	private String color;
	
	/**
	 * Initializes the bullet, marking it belonging
	 * to a tank. The x and y coor along with the angle
	 * are obtained from the tank object
	 * @param Tank shooter, the tank it belongs to
	 */
	public Bullet(Tank shooter) {
		xPos = shooter.getXpos();
		yPos = shooter.getYpos();
		angle = shooter.getAngle();
		this.shooter = shooter;
		if(shooter.getType() == 2) {
			color = "red";
			damage = 100;
		}else {
			color = "black";
			damage = 50;
		}
		type = "standard";
	}
	
	/**
	 * Initializes the bullet according to tank
	 * type
	 * @param type
	 */
	public Bullet(String type) {
		xPos = -100;
		yPos = -100;
		angle = -1;
		shooter = null;
		setDamage(type);
		this.type = type;
	}
	
	/**
	 * Initializes blank/standard bullet
	 */
	public Bullet() {
		xPos = -100;
		yPos = -100;
		angle = -1;
		shooter = null;
		damage = 50;
	}
	
	/**
	 * color of bullet
	 * @return
	 */
	public String getColor() {
		return color;
	}
	
	/**
	 * sets damage according to type. Bomb is 100 damage
	 * others are 50
	 * @param type
	 */
	public void setDamage(String type) {
		//System.out.println("BULLET TYPE IS " + type);
		if(type.equalsIgnoreCase("standard") || type.equalsIgnoreCase("turtle")) {
			damage = 50;
		}else if(type.equalsIgnoreCase("bomb")) {
			damage = 100;
		}
		this.type = type;
	}
	
	/**
	 * returns damage as an int
	 * @return int damage
	 */
	public int getDamage() {
		return damage;
	}
	
	/**
	 * if the move is valid
	 * @return
	 */
	public Boolean move() {
		if (angle == 0) {
			if (xPos < 900) xPos += 1;
			else return true;
		} else if (angle == 1){
			if (xPos > 0) xPos -= 1;
			else return true;
		} else if (angle == 2){
			if (yPos > 0) yPos -= 1;
			else return true;
		} else if (angle == 3){
			if (yPos < 900) yPos += 1;
			else return true;
		}
		return false;
	}
	
	/**
	 * returns x position
	 * @return
	 */
	public int getXpos() {
		return xPos;
	}
	
	/**
	 * returns type as an int
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
	 * return y position as int
	 * @return
	 */
	public int getYpos() {
		return yPos;
	}
	
	/**
	 * returns angle as an int
	 * @return
	 */
	public int getAngle() {
		return angle;
	}
	
	/**
	 * return Tank it belongs to
	 * @return
	 */
	public Tank getShooter() {
		return shooter;
	}
	
	/**
	 * to string of bullet
	 */
	public String toString() {
		return "SHOT: x=" + xPos + ", y=" + yPos;
 	}
}
