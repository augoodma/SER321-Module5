import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import org.json.*;

/**
 * Client 
 * This is the Client thread class, there is a client thread for each peer we are listening to.
 * We are constantly listening and if we get a message we print it. 
 */

public class ClientThread extends Thread {
	private BufferedReader bufferedReader;
	private Peer peer;
	public ClientThread(Socket socket, Peer peer) throws IOException {
		bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		this.peer = peer;
	}
	public void run() {
		while (true) {
			try {
				String[] node = new String[2];
				if(Peer.leader) {
					JSONObject json = new JSONObject(bufferedReader.readLine());
					node[0] = json.getString("username");
					node[1] = json.getString("message");
					Peer.nodes.add(node);
					System.out.println(node[0] + ":" + node[1] + " received!");
					Peer.nodesReceived++;
					if(Peer.numNodes == Peer.nodesReceived){
						System.out.println("All node data received!");
						System.out.println("Press enter to connect to followers.");
						Peer.connected = true;
						Peer.leader = true;
						Peer.askForInput();
					}
				}
			} catch (Exception e) {
				interrupt();
				break;
			}
		}
	}

}
