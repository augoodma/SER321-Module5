import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.*;

/**
 * Client 
 * This is the Client thread class, there is a client thread for each peer we are listening to.
 * We are constantly listening and if we get a message we print it. 
 */

public class ClientThread extends Thread {
	private BufferedReader bufferedReader;
	private static Peer peer;
	private boolean display = false;

	public static void setPeer(Peer p){
		peer = p;
	}
	
	public ClientThread(Socket socket) throws IOException {
		bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	}
	public void run() {
		String[] data = new String[3];
		String[] address = new String[2];
		while (true) {
			try {
			    JSONObject json = new JSONObject(bufferedReader.readLine());
			    data[0] = json.getString("data0");
				data[1] = json.getString("data1");
				data[2] = json.getString("data2");
			    if(data[0].equalsIgnoreCase("follower")) {
					System.out.println("> Node: " + data[1] + ":" + data[2] + " received.");
					address[0] = data[1];
					address[1] = data[2];
					peer.nodes.add(address);
			    }
			    else if(data[0].equalsIgnoreCase("leader")) {
					System.out.println("> Node: " + data[1] + ":" + data[2] + " received.");
					System.out.println("> All nodes received!");
					address[0] = data[1];
					address[1] = data[2];
					peer.nodes.add(address);
					Peer.leaderAddress = address;
					Peer.ready = true;
				}
			    else if(data[0].equalsIgnoreCase("ping")){
					address[0] = data[1];
					address[1] = data[2];
					if(Arrays.equals(address, Peer.leaderAddress)) {
						Peer.leaderAlive = true;
						Peer.timeOut = 0;
					}
					else Peer.leaderAlive = false;
					if(address[0].equalsIgnoreCase(peer.host) && address[1].equalsIgnoreCase(peer.port)){
						display = true;
						System.out.println("Enter an operation to perform ('calc num1 +-*/ num2'):");
						String input = peer.bufferedReader.readLine();
						String[] operation = input.split(" ");
						int num1 = Integer.parseInt(operation[1]);
						String operator = operation[2];
						int num2 = Integer.parseInt(operation[3]);
						int result;
						if(operator.equalsIgnoreCase("+"))
							result = num1 + num2;
						else if(operator.equalsIgnoreCase("-"))
							result = num1 - num2;
						else if (operator.equalsIgnoreCase("*"))
							result = num1 * num2;
						else
							result = num1 / num2;
						Peer.result = "{'data0': '" + operation[0] + "','data1':'" + result + "','data2':'" + result + "'}";
					}
			    }
				else if(data[0].equalsIgnoreCase("calc")) {
					if(display){
						System.out.println("> Calculation returned!");
						System.out.println("> Result = " + data[1]);
						display = false;
						Peer.working = false;
					}
				}
				else if(data[0].equalsIgnoreCase("electionStart")) {
					if(!Peer.leaderAlive) {
						peer.serverThread.sendMessage("{'data0': 'leaderDead','data1':'" + peer.host + "','data2':'" + peer.port  + "'}");
						Peer.vote++;
						if(Peer.vote >= Peer.majority){
							if(peer.nodes.contains(Peer.leaderAddress)) {
								peer.nodes.remove(Peer.leaderAddress);
								String[] newLeader = peer.nodes.get(peer.nodes.size() - 1);
								peer.serverThread.sendMessage("{'data0': 'newLeader','data1':'" + newLeader[0] + "','data2':'" + newLeader[1]  + "'}");
								System.out.println("> Leader dead, new leader is " + newLeader[0] + ":" + newLeader[1]  + "!");
								if(newLeader[0] == peer.host && newLeader[1] == peer.port){
									peer.leader = true;
									System.out.println("> You are Leader.");
									Peer.getNodes(peer);
									Peer.sendNodeList(peer);
									peer.serverThread.pingNodes(peer);
								}
							}
						}
					}
					else peer.serverThread.sendMessage("{'data0': 'leaderAlive','data1':'" + peer.host + "','data2':'" + peer.port  + "'}");
				}
				else if(data[0].equalsIgnoreCase("leaderDead")) {
					peer.serverThread.sendMessage("{'data0': 'leaderDead','data1':'" + peer.host + "','data2':'" + peer.port  + "'}");
					Peer.vote++;
					if(Peer.vote >= Peer.majority){
						if(peer.nodes.contains(Peer.leaderAddress)) {
							peer.nodes.remove(Peer.leaderAddress);
							int next = (int) ((Math.random() * (peer.nodes.size())));
							peer.serverThread.sendMessage("{'data0': 'newLeader','data1':'" + next + "','data2':'" + next  + "'}");
						}
					}
				}
				} catch (Exception e) {
				interrupt();
				break;
			}
		}
	}
}
