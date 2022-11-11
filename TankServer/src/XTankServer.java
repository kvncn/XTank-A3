//
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
	//static ArrayList<DataOutputStream> sq;
	static XTankModel md;
	static Integer i = 0;
	
    public static void main(String[] args) throws Exception 
    {
		System.out.println(InetAddress.getLocalHost());
		//sq = new ArrayList<>();
		md = new XTankModel();
		
        try (var listener = new ServerSocket(59896)) 
        {
            System.out.println("The XTank server is running...");
            var pool = Executors.newFixedThreadPool(20);
            while (true) 
            {
                pool.execute(new XTankManager(listener.accept()));
            }
        }
    }

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
            	Tank tank = new Tank(300, 500, 2, ++i);
            	md.addTank(tank);
                //sq.add(out);
                int mode, x, y, angle;
                while (true)
                {
                	mode = in.readInt();
                	if (mode == 0) {
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
                	}
                	md.moveBullets();
            		md.regHits();
            		Thread.sleep(16);
                	//md.moveBullets();
            		for (Tank tk : md.getTanks()) {
            			//System.out.println("PASSING TANK (" + tk.toString() + ") and coords: (" + tk.getXpos() +", " + tk.getYpos() + ")");
            			out.writeInt(tk.getXpos());
            			out.writeInt(tk.getYpos());
            			out.writeInt(tk.getAngle());
            			out.writeInt(tk.getShot().getXpos());
            			out.writeInt(tk.getShot().getYpos());
            			out.writeInt(tk.getShot().getAngle());
            		}
            		
            		out.flush();
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