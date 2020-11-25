import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

/**
 * SERVER
 * This is the ServerThread class that has a socket where we accept clients contacting us.
 * We save the clients ports connecting to the server into a List in this class. 
 * When we wand to send a message we send it to all the listening ports
 */

public class ServerThread extends Thread{
	private ServerSocket serverSocket;
	private Set<Socket> listeningSockets = new HashSet<Socket>();
	private static Peer peer;
	public static boolean working = false;

	public void setPeer(Peer p){
		peer = p;
	}

	public ServerThread(String portNum) throws IOException {
		serverSocket = new ServerSocket(Integer.valueOf(portNum));
	}
	
	/**
	 * Starting the thread, we are waiting for clients wanting to talk to us, then save the socket in a list
	 */
	public void run() {
		try {
			while (true) {
				Socket sock = serverSocket.accept();
				listeningSockets.add(sock);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Sending the message to the OutputStream for each socket that we saved
	 */
	void sendMessage(String message) {
		try {
			for (Socket s : listeningSockets) {
				PrintWriter out = new PrintWriter(s.getOutputStream(), true);
				out.println(message);
		     }
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	void pingNodes(Peer peer) throws IOException {
		long timer = System.currentTimeMillis();
		long interval;
		int next = (int) ((Math.random() * (peer.nodes.size())));
		String[] address = peer.nodes.get(next);
		String host = address[0];
		String port = address[1];
		String nextNode = "{'data0': 'ping','data1':'" + host + "','data2':'" + port + "'}";
		String ping = "{'data0': 'ping','data1':'" + peer.host + "','data2':'" + peer.port + "'}";
		while(true) {
			peer.operation = ping;
			interval = System.currentTimeMillis();
			if(interval - timer > 250) {
				peer.serverThread.sendMessage(ping);
				if(!Peer.working) {
					peer.serverThread.sendMessage(nextNode);
					Peer.working = true;
				}
				timer = System.currentTimeMillis();
			}
		}
	}

	void awaitPing(Peer peer) throws InterruptedException, IOException {
		while(true) {
			if(!peer.leaderAlive) return;
			peer.leaderAlive = false;
			Thread.sleep(1000);
		}
	}
}
