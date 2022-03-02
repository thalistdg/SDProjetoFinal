import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;

public class Coordinator {
	
	private static Queue<Integer> s1Pending = new LinkedList<Integer>();
	private static Queue<Integer> s2Pending = new LinkedList<Integer>();
	private static Queue<Integer> s3Pending = new LinkedList<Integer>();

	public static void main(String[] args) {
		
		int listenPort = 40000;
		
		try {
			@SuppressWarnings("resource")
			ServerSocket serverSocket = new ServerSocket(listenPort);
			
			while(true) {
				System.out.printf("PendingSizes->s1:%d|s2:%d|s3:%d\n", s1Pending.size(), s2Pending.size(), s3Pending.size());
				Socket clientSocket = serverSocket.accept();
				
				ObjectInputStream entrada = new ObjectInputStream(clientSocket.getInputStream());			
				ObjectOutputStream saida = new ObjectOutputStream(clientSocket.getOutputStream());
				saida.flush();
				
				int serverNumber = entrada.readInt();
				
				int request = entrada.readInt();
				
				int isPrime = 0;
				
				if(request != 0)
					isPrime = entrada.readInt();
				
				int pendingOperations =	addInLists(request, isPrime, serverNumber);
					
				if(serverNumber == 1)
					sendOperations(entrada, saida, s1Pending, pendingOperations);
				else if(serverNumber == 2) 
					sendOperations(entrada, saida, s2Pending, pendingOperations);
				else 
					sendOperations(entrada, saida, s3Pending, pendingOperations);
					
				entrada.close();
				saida.close();
				clientSocket.close();

			}

		}catch (Exception e) {
			System.out.println("Erro C: " + e.getMessage());
		}
	}
	
	public static synchronized int addInLists(int request, int isPrime, int serverNumber) {
		
		if(request != 0) {
			s1Pending.add(request);
			s1Pending.add(isPrime);
			s2Pending.add(request);
			s2Pending.add(isPrime);
			s3Pending.add(request);
			s3Pending.add(isPrime);
		}
		
		int pendingOperations;
		
		if(serverNumber == 1)
			pendingOperations = s1Pending.size()/2;
		else if(serverNumber == 2) 
			pendingOperations = s2Pending.size()/2;
		else 
			pendingOperations = s3Pending.size()/2;
		
		
		return pendingOperations;
		
	}
	
	public static synchronized void sendOperations(ObjectInputStream entrada, 
			ObjectOutputStream saida,
			Queue<Integer> pendingList,
			int pendingOperations
			) {

		try {

			saida.writeInt(pendingOperations);
			saida.flush();
			
			int operation;
			
			for(int i = 0;i < pendingOperations;i++) {
			
				operation = pendingList.remove(); // number
				saida.writeInt(operation);
				saida.flush();
				
				operation = pendingList.remove(); // if is prime
				saida.writeInt(operation);
				saida.flush();
			
			}
		}catch (Exception e) {
			System.out.println("Erro TC sendOperation: " + e.getMessage());
		}
	}
	

}
