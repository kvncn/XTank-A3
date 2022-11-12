//
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class XTankUI
{
	private int[] maze = {100, 100, 100, 400, 700, 100, 950, 100,
			450, 650, 450, 750, 650, 650, 650, 750, 450, 650, 650, 650, 450, 750, 650, 750};
	// The location and direction of the "tank"
	private int x = 300;
	private int y = 500;
	private int angle = 2;
	private char curr;
	private int mode;
	private ArrayList<Integer> tanks;
	private ArrayList<Integer> shots;

	private Canvas canvas;
	private Display display;
	private Shell shell;
	
	DataInputStream in; 
	DataOutputStream out;
	
	public XTankUI(DataInputStream in, DataOutputStream out)
	{
		this.in = in;
		this.out = out;
		tanks = new ArrayList<>();
		shots = new ArrayList<>();
		mode = 0;
	}
	
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
			event.gc.setForeground(shell.getDisplay().getSystemColor(SWT.COLOR_WHITE));
			event.gc.drawRectangle(0, 0, 900, 900);
			event.gc.setForeground(shell.getDisplay().getSystemColor(SWT.COLOR_BLACK));
			for (int i = 0; i < tanks.size() - 2; i+=3) {
				int tankX = tanks.get(i);
				int tankY = tanks.get(i+1);
				tankAngle = tanks.get(i+2);
				if (tankAngle == 2 || tankAngle == 3) {
					event.gc.drawRectangle(tankX, tankY, 25, 50);
					event.gc.drawOval(tankX, tankY+12, 25, 25);
					event.gc.setLineWidth(2);
					
					if (tankAngle == 2) {
						event.gc.drawLine(tankX+13, tankY+12, tankX+13, tankY-7);
					} else {
						event.gc.drawLine(tankX+13, tankY+35, tankX+13, tankY+60);
					}
				} else {
					event.gc.drawRectangle(tankX, tankY, 50, 25);
					event.gc.drawOval(tankX+12, tankY, 25, 25);
					event.gc.setLineWidth(2);
					
					if (tankAngle == 0) {
						event.gc.drawLine(tankX+35, tankY+13, tankX+60, tankY+13);
					} else {
						event.gc.drawLine(tankX+12, tankY+13, tankX-7, tankY+13);
					}
				}
			}
			
			for (int i = 0; i < shots.size() - 2; i+=3) {
				int shotX = shots.get(i);
				int shotY = shots.get(i+1);
				int shotAngle = shots.get(i+2);
				if (shotAngle == 0) {
					event.gc.drawOval(shotX+60, shotY+10, 5, 5);
				} else if (shotAngle == 1) {
					event.gc.drawOval(shotX-14, shotY+10, 5, 5);
				} else if (shotAngle == 2) {
					event.gc.drawOval(shotX+10, shotY-12, 5, 5);
				} else {
					event.gc.drawOval(shotX+10, shotY+55, 5, 5);
				}
			}
			
			for (int i = 0; i < maze.length - 3; i += 4) {
				event.gc.drawLine(maze[i], maze[i+1], maze[i+2], maze[i+3]);
			}
			//event.gc.setBackground(shell.getDisplay().getSystemColor(SWT.COLOR_WHITE));
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
                	
                	if (!intersects(newX, newY)) {
                		x = newX;
                		y = newY;
                	}
            	}
				
				try {
					out.writeInt(mode);
					out.writeInt(angle);
					out.writeInt(x);
					out.writeInt(y);
					out.flush();
				} catch (IOException e1) {
					System.out.println("SOMETHING WRONG WHEN CLIENT WROTE TO SERVER");
					e1.printStackTrace();
				}
				
				canvas.redraw();
			}
			public void keyReleased(KeyEvent e) {}
		});
		
		//---- event loop
		Runnable runnable = new Runner();
		display.asyncExec(runnable);
		shell.open();
		while(!shell.isDisposed()) {
			if(!display.readAndDispatch()) {}
		}
		display.dispose();
	}
	
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
	
	class Runner implements Runnable
	{
		public void run() 
		{
			try {
				
				if (in.available() > 0) {
				tanks = new ArrayList<>();
				shots = new ArrayList<>();
				
				int tankX = in.readInt();
				int tankY = in.readInt();
				int tankAngle = in.readInt();
				
				int shotX = in.readInt();
				int shotY = in.readInt();
				int shotAngle = in.readInt();
				//System.out.println("Im getting this position: (" + tankX + ", " + tankY + ")");
				//System.out.println("Im getting this shot: (" + shotX + ", " + shotY + ")");
				
				tanks.add(tankX);
				tanks.add(tankY);
				tanks.add(tankAngle);
				
				shots.add(shotX);
				shots.add(shotY);
				shots.add(shotAngle);
				}
			
				canvas.redraw();	
	            
			} catch (IOException e) {
				System.out.println("SOMETHING WRONG WHEN CLIENT READ FROM SERVER");
				e.printStackTrace();
			}
			display.timerExec(1, this);
		}
	};	
}


