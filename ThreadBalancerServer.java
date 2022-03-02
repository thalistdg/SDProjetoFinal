import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Queue;

public class ThreadBalancerServer extends Thread{
	
	private int serverPort;
	private int request;
	private int serverNumber;
	private Queue<Integer> idleServers;
		
	public ThreadBalancerServer(int serverPort, int request, int serverNumber, Queue<Integer> idleServers) {

		this.serverPort = serverPort;
		this.request = request;
		this.serverNumber = serverNumber;
		this.idleServers = idleServers;
	
	}

	public void run() {
		try {
			Socket clientSocket = new Socket("127.0.0.1", serverPort);

			ObjectOutputStream saida = new ObjectOutputStream(clientSocket.getOutputStream());
			saida.flush();				
			ObjectInputStream entrada = new ObjectInputStream(clientSocket.getInputStream());				
				
			saida.writeInt(request);
			saida.flush();

			int reply = entrada.readInt();

			if(reply == request) {
				addIdleServer();
			}else {
				System.out.println("Erro - Servidor retornou errado !!!");
			}		
			
			clientSocket.close();
			
		}catch(Exception e) {
			System.out.println("Erro TBS: " + e.getMessage());
		}
		
	}
	
	public synchronized void addIdleServer() {
		idleServers.add(serverNumber);
	}

}
