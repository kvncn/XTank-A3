import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
/**
 * @author Kevin Nisterenko
 * FILE: XTankUI.java
 * ASSIGNMENT: A3 - XTank
 * COURSE: CSC 335; FALL 2022
 * PURPOSE: This class creates the UI of XTank client side.
 *
 * This class receives information regarding the tanks and the
 * bullets in action from the server in the form of integers.
 * 
 * Each tank has 4 integers associated with it that represent
 * tankX, tankY, tankAngle, and tankType.
 * 
 * Each shot has 3 integers that represent x, y, and angle.
 * 
 * When the tank changes position client side, that is written back 
 * to the server. 
 * 
 * The ui also handles maze collisions and removing the player if dead.
 * 
 * 
 */
public class XTankUI
{
	private int[] maze = {100, 100, 100, 400, 700, 100, 950, 100,
			450, 650, 450, 750, 650, 650, 650, 750, 450, 650, 650, 650, 450, 750, 650, 750};
	// The location and direction of the "tank"
	private int x = 300;
	private int y = 500;
	private int angle = 2;
	private int type;
	private char curr;
	private int mode;
	private ArrayList<Integer> tanks;
	private ArrayList<Integer> shots;

	private Canvas canvas;
	private Display display;
	private Shell shell;
	
	DataInputStream in; 
	DataOutputStream out;
	
	/**
	 * sets up UI with both the input stream and the output stream
	 * Initializes ArrayList<Tank> and Array<Bullet> that represent
	 * the current tanks and bullets in game. 
	 * @param in
	 * @param out
	 */
	public XTankUI(DataInputStream in, DataOutputStream out)
	{
		this.in = in;
		this.out = out;
		tanks = new ArrayList<>();
		shots = new ArrayList<>();
		mode = 0;
	}
	
	/**
	 * This method represents the beginning of the game and
	 * creates an empty canvas. When the paint listener is 
	 * activated, current tanks and bullets are printed 
	 * on the UI
	 */
	public void start()
	{
		display = new Display();
		shell = new Shell(display);
		shell.setText("xtank");
		shell.setLayout(new FillLayout());
		shell.setSize(900, 900);

		canvas = new Canvas(shell, SWT.BACKGROUND);
		
		canvas.addPaintListener(event -> {
			if (tanks.size() != 0) {
			//System.out.println("Let's print da tanks: " + tanks);
			}
			int tankAngle = 2;
			//event.gc.setBackground(shell.getDisplay().getSystemColor(SWT.COLOR_WHITE));
			//each tank will always have x, y coor with angle and type
			//thus can reliably skip 4
			for (int i = 0; i < tanks.size() - 3; i+=4) {
				int tankX = tanks.get(i);
				int tankY = tanks.get(i+1);
				tankAngle = tanks.get(i+2);
				type = tanks.get(i+3);
				int color = getTankColor(type);
				printTank(event, tankX, tankY, tankAngle, color);
			}
			
			//bullets only contain 3 types of information:
			//x,y coor, and the angle
			for (int i = 0; i < shots.size() - 2; i+=3) {
				int shotX = shots.get(i);
				int shotY = shots.get(i+1);
				int shotAngle = shots.get(i+2);
				if (shotAngle == 0) {//face right
					event.gc.drawOval(shotX+60, shotY+10, 5, 5);
				} else if (shotAngle == 1) {//face left
					event.gc.drawOval(shotX-14, shotY+10, 5, 5);
				} else if (shotAngle == 2) {//face up
					event.gc.drawOval(shotX+10, shotY-12, 5, 5);
				} else {//face down
					event.gc.drawOval(shotX+10, shotY+55, 5, 5);
				}
			}
			
			//draw maze obstables
			for (int i = 0; i < maze.length - 3; i += 4) {
				event.gc.drawLine(maze[i], maze[i+1], maze[i+2], maze[i+3]);
			}
		});
		
		canvas.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent e) {
				System.out.println("key " + e.character);
				curr = e.character;
				
				if (curr == ' ') {
					mode = 1; 
            	} else {
            		int newX = x;
            		int newY = y;
            		mode = 0;
            		if (curr == 'w') {
                		newY-=10;
                		angle = 2;
                	} 
                	if (curr == 'a') {
                		newX-=10;
                		angle = 1;
                	}
                	if (curr == 's') {
                		newY+=10;
                		angle = 3;
                	}
                	if (curr == 'd') {
                		newX+=10;
                		angle = 0;
                	}
                	
                	//if not hitting obstacle, then new x and y
                	//for the tank
                	if (!intersects(newX, newY)) {
                		x = newX;
                		y = newY;
                	}
            	}
			}
			public void keyReleased(KeyEvent e) {}
		});
		
		//---- event loop
		Runnable runnable = new Runner();
		display.asyncExec(runnable);
		shell.open();
		while(!shell.isDisposed()) {
			if(!display.readAndDispatch()) {
				try {
					Thread.sleep(16);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		display.dispose();
	}
	
	/**
	 * Prints out tank based on direction and type.
	 * If the tank is a turtle type, it is green; standard, cyan;
	 * and bomb, red.
	 * @param event
	 * @param tankX
	 * @param tankY
	 * @param tankAngle
	 * @param color
	 */
	private void printTank(PaintEvent event, int tankX, int tankY, int tankAngle, int color) {
		if(tankAngle == 2) { // face up
			event.gc.fillRectangle(canvas.getBounds());
			event.gc.setBackground(shell.getDisplay().getSystemColor(color));
			event.gc.fillRectangle(tankX, tankY, 25, 50);
			event.gc.setBackground(shell.getDisplay().getSystemColor(SWT.COLOR_BLACK));
			event.gc.fillOval(tankX, tankY+12, 25, 25);
			event.gc.setLineWidth(2);
			event.gc.drawLine(tankX+13, tankY+12, tankX+13, tankY-7);
		}else if(tankAngle == 3) {//face down
			event.gc.fillRectangle(canvas.getBounds());
			event.gc.setBackground(shell.getDisplay().getSystemColor(color));
			event.gc.fillRectangle(tankX, tankY, 25, 50);
			event.gc.setBackground(shell.getDisplay().getSystemColor(SWT.COLOR_BLACK));
			event.gc.fillOval(tankX, tankY+12, 25, 25);
			event.gc.setLineWidth(2);
			event.gc.drawLine(tankX+13, tankY+35, tankX+13, tankY+60);
		}else if(tankAngle == 0) {//right
			event.gc.fillRectangle(canvas.getBounds());
			event.gc.setBackground(shell.getDisplay().getSystemColor(color));
			event.gc.fillRectangle(tankX, tankY, 50, 25);
			event.gc.setBackground(shell.getDisplay().getSystemColor(SWT.COLOR_BLACK));
			event.gc.fillOval(tankX+12, tankY, 25, 25);
			event.gc.setLineWidth(2);
			event.gc.drawLine(tankX+35, tankY+13, tankX+60, tankY+13);
		}else {//left
			event.gc.fillRectangle(canvas.getBounds());
			event.gc.setBackground(shell.getDisplay().getSystemColor(color));
			event.gc.fillRectangle(tankX, tankY, 50, 25);
			event.gc.setBackground(shell.getDisplay().getSystemColor(SWT.COLOR_BLACK));
			event.gc.fillOval(tankX+12, tankY, 25, 25);
			event.gc.setLineWidth(2);
			event.gc.drawLine(tankX+12, tankY+13, tankX-7, tankY+13);
		}
	}
	
	/**
	 * If the tank is a turtle type, it is green; standard, cyan;
	 * and bomb, red.
	 * @param type, 0 for stand, 1 for turtle, 2 for bomb
	 * @return
	 */
	private int getTankColor(int type) {
		if(type == 1) { //turtle tank
			return SWT.COLOR_DARK_GREEN;
		}else if(type == 2) { //bomb tank
			return SWT.COLOR_DARK_RED;
		}
		//standard tank
		return SWT.COLOR_CYAN; 	
	}
	
	/**
	 * This method checks if the potential x and y coor
	 * intersects with either the canvas or with the maze
	 * @param newX
	 * @param newY
	 * @return
	 */
	private boolean intersects(int newX, int newY) {
		// canvas boundaries 
		if ((newX >= 830 || newX <= 0) || (newY >= 800 || newY <= 0)) return true; 
		
		for (int i = 0; i < maze.length - 3; i += 4) {
			int startX = maze[i];
			int startY = maze[i+1];
			int endX = maze[i+2];
			int endY = maze[i+3];
			
			// maze line boundaries
			if (newX + 50 >= startX && newX <= endX) {
				if (newY + 50 >= startY && newY <= endY) {
					return true; 
				}
			}
  		}
		
		return false;
	}
	
	/**
	 * Runs the dialogue between the server and the clients
	 * Writes the mode, angle, and x,y coor to the server
	 * 
	 * Reads in tank and bullet information from the server
	 *
	 */
	class Runner implements Runnable
	{
		public void run() 
		{
			try {
				out.writeInt(mode);
				out.writeInt(angle);
				out.writeInt(x);
				out.writeInt(y);
				//out.writeInt(type);
				out.flush();
			} catch (IOException e) {
				System.out.println("SOMETHING WRONG WHEN CLIENT WROTE TO SERVER");
				e.printStackTrace();
			}
			
			try {
				tanks = new ArrayList<>();
				shots = new ArrayList<>();
				
				int tankX = in.readInt();
				int tankY = in.readInt();
				int tankAngle = in.readInt();
				int tankType = in.readInt();
				
				int shotX = in.readInt();
				int shotY = in.readInt();
				int shotAngle = in.readInt();
				//System.out.println("Im getting this position: (" + tankX + ", " + tankY + ")");
				//System.out.println("Im getting this shot: (" + shotX + ", " + shotY + ")");
				
				tanks.add(tankX);
				tanks.add(tankY);
				tanks.add(tankAngle);
				tanks.add(tankType);
				
				shots.add(shotX);
				shots.add(shotY);
				shots.add(shotAngle);
			} catch (IOException e) {
				System.out.println("SOMETHING WRONG WHEN CLIENT READ FROM SERVER");
				e.printStackTrace();
			}
			canvas.update();
			canvas.redraw();	
            display.timerExec(1, this);
		}
	};	
}
