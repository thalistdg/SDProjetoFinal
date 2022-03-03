import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;

public class Balancer {

	private static Queue<Integer> requests;
	
	public static void main(String[] args) {
		int port = 6000;
		
		try {
			@SuppressWarnings("resource")
			ServerSocket serverSocket = new ServerSocket(port);
			System.out.println("Balanceador ouvindo na porta " + port);
			
			requests = new LinkedList<Integer>();
			
			new ThreadBalancer(requests).start();
			
			while(true) {
				Socket clientSocket = serverSocket.accept();
				
				ObjectInputStream entrada = new ObjectInputStream(clientSocket.getInputStream());
				
				int num = entrada.readInt();
				addRequest(requests, num);
				
				entrada.close();

			}
			
		} catch (Exception e) {
			System.out.println("Erro B: " + e.getMessage());

		}

	}
	
	public static synchronized void addRequest(Queue<Integer> requests, int num) {
		requests.add(num);
	}

}
