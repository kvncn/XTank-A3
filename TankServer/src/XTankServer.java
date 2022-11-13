/**
 * @author Kevin Nisterenko
 * FILE: XTankServer.java
 * ASSIGNMENT: A3 - XTank
 * COURSE: CSC 335; FALL 2022
 * PURPOSE: This class sets the XTank server which passes 
 * along information regarding the tanks in the server
 * (such as their x,y position along with the angle they
 * are facing and the type of tank it is) along with the
 * shots in the server. 
 * 
 * Up to 20 different client can connect to the server and
 * participate in XTank.
 * 
 */

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.net.InetAddress;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;

/**
 * When a client connects, a new thread is started to handle it.
 */
public class XTankServer 
{
	static ArrayList<DataOutputStream> sq;
	static XTankModel md;
	static Integer i = 0; //used to id tanks
	
    public static void main(String[] args) throws Exception 
    {
    	//use this to get the local host address to play on
    	//multiple computers
		System.out.println(InetAddress.getLocalHost());
		sq = new ArrayList<>();
		md = new XTankModel();
		
        try (var listener = new ServerSocket(59896)) 
        {
            System.out.println("The XTank server is running...");
            //limit of 20 clients
            var pool = Executors.newFixedThreadPool(20);
            while (true) 
            {
                pool.execute(new XTankManager(listener.accept()));
            }
        }
    }

    /**
     * 
     * @author Kevin Nisterenko
     * 
     * This class manages the XTank and adds tank to the Model
     * that is used to represent the entire game to provide
     * communication between the different clients.
     *
     */
    private static class XTankManager implements Runnable 
    {
        private Socket socket;

        XTankManager(Socket socket) { this.socket = socket; }

        @Override
        public void run() 
        {
            System.out.println("Connected: " + socket);
            try 
            {
            	DataInputStream in = new DataInputStream(socket.getInputStream());
            	DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            	// types are: standard, bomb, turtle
            	// depending on what the user wants to be
            	int typeInt = in.readInt();
            	String tankType =  "";
            	switch (typeInt) {
        		case 1: tankType = "standard"; break;
        		case 2: tankType = "bomb"; break;
        		case 3: tankType = "turtle"; break;
        		default: tankType = "standard"; break;
        		}
            	Tank tank = new Tank(300, 500, 2, ++i, tankType);
            	//adding tank to model
            	md.addTank(tank);
                int mode, x, y, angle;
                while (true)
                {
                	mode = in.readInt();
                	if (mode == 0) {
                		//reading tank information from client
                		angle = in.readInt();
                		x = in.readInt();
                    	y = in.readInt();
                    	
	                	tank.xMove(x);
	                	tank.yMove(y);
	                	tank.changeAngle(angle);
                	} else if (mode == 1) {
                		md.addShot(tank, tank.getAngle());
                		// just to free buffer
                		in.readInt();
                		in.readInt();
                		in.readInt();
                		md.moveBullets();
                	}
                	md.moveBullets();
                	md.regHits();
            		for (Tank tk : md.getTanks()) {
            			//writing tank information to client
            			out.writeInt(tk.getXpos());
            			out.writeInt(tk.getYpos());
            			out.writeInt(tk.getAngle());
            			out.writeInt(tk.getType());
            			out.flush();
            			//writing shot information to client
            			out.writeInt(tk.getShot().getXpos());
            			out.writeInt(tk.getShot().getYpos());
            			out.writeInt(tk.getShot().getAngle());
            			out.flush();
            		}
                }
            } 
            catch (Exception e) 
            {
            	e.printStackTrace();
                System.out.println("Error:" + socket);
            } 
            finally 
            {
                try { socket.close(); } 
                catch (IOException e) {}
                System.out.println("Closed: " + socket);
            }
        }
    }
}
