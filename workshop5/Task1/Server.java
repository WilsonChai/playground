/*
 * Workshop 4 - Task 1 Server
 * Course:JAC444 - Semester 4
 * Last Name: Chai
 * First Name: Wilson
 * ID: 030-918-114
 * Section: DD
 * This assignment represents my own work in accordance with Seneca Academic Policy.
 * Signed by Wilson Chai
 * Date: April 17, 2018
 */
import java.io.IOException;
import java.net.*;
import java.util.Vector;
import java.io.*;
import java.util.*;

public class Server {
    static Vector<Socket> ClientSockets;
    static Vector<String> LoginNames;

    Server() throws IOException {
        ServerSocket server = new ServerSocket(5217);
        ClientSockets = new Vector<Socket>();
        LoginNames = new Vector<String>();

        while (true) {
            Socket client = server.accept();
            AcceptClient acceptClient = new AcceptClient(client);
        }
    }

    public static void main(String[] args) throws IOException {
        Server server = new Server();
    }

    class AcceptClient extends Thread {
        Socket ClientSocket;
        DataInputStream din;
        DataOutputStream dout;

        AcceptClient(Socket client) throws IOException {
            ClientSocket = client;
            din = new DataInputStream(ClientSocket.getInputStream());
            dout = new DataOutputStream(ClientSocket.getOutputStream());

            String LoginName = din.readUTF();

            LoginNames.add(LoginName);
            ClientSockets.add(ClientSocket);

            start();
        }

        public void run() {
            while (true) {
                try {
                    String msgFromClient = din.readUTF();
                    StringTokenizer st = new StringTokenizer(msgFromClient);
                    String LoginName = st.nextToken();
                    String MsgType = st.nextToken();
                    int lo = -1;

                    String msg = "";

                    while(st.hasMoreTokens()) {
                    	msg = msg + " " + st.nextToken();
                    }
                    
                    if (MsgType.equals("LOGIN")) {
	                    for (int i = 0; i < LoginNames.size(); i++) {
	                        Socket pSocket = (Socket) ClientSockets.elementAt(i);
	                        DataOutputStream pOut = new DataOutputStream(pSocket.getOutputStream());
	                        pOut.writeUTF(LoginName + " has logged in.");
	                    }
                    } else if (MsgType.equals("LOGOUT")) {
	                    for (int i = 0; i < LoginNames.size(); i++) {
	                    	if (LoginName.equals(LoginNames.elementAt(i))) {
	                    		lo = i;
	                    	}
	                        Socket pSocket = (Socket) ClientSockets.elementAt(i);
	                        DataOutputStream pOut = new DataOutputStream(pSocket.getOutputStream());
	                        pOut.writeUTF(LoginName + " has logged out");
	                    }
	                    if (lo >= 0) {
	                    	LoginNames.removeElementAt(lo);
	                    	ClientSockets.removeElementAt(lo);
	                    }
                    } else {
                    	for (int i = 0; i < LoginNames.size(); i++) {
	                        Socket pSocket = (Socket) ClientSockets.elementAt(i);
	                        DataOutputStream pOut = new DataOutputStream(pSocket.getOutputStream());
	                        pOut.writeUTF(LoginName + ": " + msg);
	                    }
                    }

                    if (MsgType.equals("LOGOUT")) {
                    	break;
                    }
                } catch (IOException e) {e.printStackTrace();}
            }
        }
    }
}
