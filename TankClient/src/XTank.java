import java.net.Socket;
import java.io.DataOutputStream;
import java.io.DataInputStream;
/**
 * @author Kevin Nisterenko
 * FILE: XTank.java
 * ASSIGNMENT: A3 - XTank
 * COURSE: CSC 335; FALL 2022
 * PURPOSE: This class creates the client for XTank.
 * 
 * This class sets up the in and out data streams along with the ui
 * for the client. 
 *
 */
public class XTank 
{
	public static void main(String[] args) throws Exception 
    {
		//can be modified to be played over network and not just local
		//compile time flexibility according to David
        try (var socket = new Socket("127.0.0.1", 59896)) 
        {
        	DataInputStream in = new DataInputStream(socket.getInputStream());
        	DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            var ui = new XTankUI(in, out);
            ui.start();
        }
    }
}
