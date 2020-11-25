import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * This is the main class for the peer2peer program.
 * It starts a client with a username and port. Next the peer can decide who to listen to. 
 * So this peer2peer application is basically a subscriber model, we can "blurt" out to anyone who wants to listen and 
 * we can decide who to listen to. We cannot limit in here who can listen to us. So we talk publicly but listen to only the other peers
 * we are interested in. 
 * 
 */

public class Peer {
	public BufferedReader bufferedReader;
	public ServerThread serverThread;
	public static String result = null;
	public boolean leader;
	public static boolean working = false;
	public static volatile boolean leaderAlive = true;
	public static volatile long timeOut = 0;
	public static volatile String operation;
	public String host;
	public List<String[]> nodes = new ArrayList<>();
	public static String[] leaderAddress = new String[2];
	public String port;
	public static volatile boolean ready;
	public static int vote = 0;
	public static int majority;

	public Peer(BufferedReader bufReader, ServerThread serverThread, boolean leader, String host, String port){
		this.host = host;
		this.bufferedReader = bufReader;
		this.serverThread = serverThread;
		this.leader = leader;
		this.port = port;
	}

	public static void main (String[] args) throws Exception {
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		String username = args[0];
		System.out.println("> Host: " + args[1] + " Port: " + args[2]);

		// starting the Server Thread, which waits for other peers to want to connect
		ServerThread serverThread = new ServerThread(args[2]);
		serverThread.start();
		Peer peer = new Peer(bufferedReader, serverThread, Boolean.parseBoolean(args[0]), args[1], args[2]);
		serverThread.setPeer(peer);
		if(peer.leader){
			//get node addresses
			System.out.println("> You are Leader.");
			getNodes(peer);
			sendNodeList(peer);
			serverThread.pingNodes(peer);
		}
		else{
			//connect to leader
			System.out.println("> You are Follower.");
			leaderAddress[0] = args[3];
			leaderAddress[1] = args[4];
			if(leaderConnect(peer)){
				System.out.println("> Awaiting nodes...");
				while (!ready) {
					Thread.onSpinWait();
				}
				if(result != null){
					peer.serverThread.sendMessage(result);
					result = null;
				}
				serverThread.awaitPing(peer);
				//call leader election
				election(peer);
			}
		}
	}

	private static void election(Peer peer) {
		peer.serverThread.sendMessage("{'data0': 'electionStart','data1':'" + peer.host + "','data2':'" + peer.port  + "'}");
	}

	private static void killLeader() {
		System.out.println("YOU DIED");
		System.exit(0);
	}

	static void getNodes(Peer peer) throws IOException {
		System.out.println("> Enter node addresses (host:port):");
		String input = peer.bufferedReader.readLine();
		String[] setupValue = input.split(" ");
		for (int i = 0; i < setupValue.length; i++) {
			String[] address = setupValue[i].split(":");
			Socket socket = null;
			try {
				peer.nodes.add(address);
				socket = new Socket(address[0], Integer.valueOf(address[1]));
				new ClientThread(socket).start();
				ClientThread.setPeer(peer);
			} catch (Exception c) {
				if (socket != null) {
					socket.close();
				} else {
					System.out.println("> Error. Exiting.");
					System.exit(0);
				}
			}
		}
	}

	static void sendNodeList(Peer peer) {
		for(String[] node : peer.nodes)
			peer.serverThread.sendMessage("{'data0': 'follower','data1':'" + node[0] + "','data2':'" + node[1]  + "'}");
		peer.serverThread.sendMessage("{'data0': 'leader','data1':'" + peer.host + "','data2':'" + peer.port  + "'}");
		System.out.println("> Node list sent!");
		majority = peer.nodes.size() / 2;
	}

	private static boolean leaderConnect(Peer peer) throws IOException {
		Socket socket = null;
		try {
			socket = new Socket(leaderAddress[0], Integer.parseInt(leaderAddress[1]));
			new ClientThread(socket).start();
			ClientThread.setPeer(peer);
			return true;
		} catch (Exception c) {
			return false;
		}
	}

	private static void pingNodes(Peer peer) throws IOException {
		long timer = System.currentTimeMillis();
		long interval;
		boolean b = false;
		String ping = "{'data0': 'ping','data1':'" + peer.host + "','data2':'" + peer.port + "'}";
		while(true) {
			operation = ping;
			interval = System.currentTimeMillis();
			if(interval - timer > 250) {
				peer.serverThread.sendMessage(operation);
				timer = System.currentTimeMillis();
			}
			if(!b)
				System.out.println("Enter an operation to perform:");
			b = true;
			String input = peer.bufferedReader.readLine();
			String[] setupValue = input.split(" ");
		}
	}

	private static void awaitPing(Peer peer) throws InterruptedException {
		leaderAlive = false;
		Thread.sleep(1000);
	}
}
