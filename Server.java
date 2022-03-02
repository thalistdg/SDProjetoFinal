import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	public static void main(String[] args) {
		
		int listenPort = Integer.parseInt(args[0]);
		int serverNumber = Integer.parseInt(args[1]);
		String filePath = new String(args[2]);
		
		int coordPort = 40000;
		
		try {
			@SuppressWarnings("resource")
			ServerSocket serverSocket = new ServerSocket(listenPort);
			System.out.println("Servidor ouvindo na porta " + listenPort);
			
			while(true) {
				Socket clientSocket = serverSocket.accept();
				
				ObjectInputStream entrada = new ObjectInputStream(clientSocket.getInputStream());			
				ObjectOutputStream saida = new ObjectOutputStream(clientSocket.getOutputStream());
				saida.flush();
				
				int request = entrada.readInt();	
				
				Socket coordSocket = new Socket("127.0.0.1", coordPort);

				ObjectOutputStream coordSaida = new ObjectOutputStream(coordSocket.getOutputStream());
				coordSaida.flush();				
				ObjectInputStream coordEntrada = new ObjectInputStream(coordSocket.getInputStream());
				
				coordSaida.writeInt(serverNumber);
				coordSaida.flush();
				
				coordSaida.writeInt(request);
				coordSaida.flush();

				
				if(request == 0) {
					// Operacao de leitura
					
					writePendings(coordEntrada, filePath);
					
					File file = new File(filePath);
					if(file.isFile()) {
					
						BufferedReader buffRead = new BufferedReader(new FileReader(filePath));
						String linha = "";
						while (true) {
							if (linha != null) {
								System.out.println(linha);
	
							} else
								break;
							linha = buffRead.readLine();
						}
						buffRead.close();
					}
					
				}else{
					// Operacao de escrita
					
					boolean requestIsPrime = isPrime(request);
					
					coordSaida.writeInt(requestIsPrime ? 1 : -1);
					coordSaida.flush();
					
					writePendings(coordEntrada, filePath);		
					
				}
				coordSocket.close();
				
				saida.writeInt(request);
				saida.flush();
				
				entrada.close();
				saida.close();
				clientSocket.close();
				
			}
			
			
		}catch(Exception e) {
			System.out.println("Erro Server: " + e.getMessage());
		}

	}
	
	public static void writePendings(ObjectInputStream coordEntrada, String filePath) {
		try {
			FileWriter file = new FileWriter(filePath, true);
			PrintWriter writeFile = new PrintWriter(file);
			
			int pendingOperations = coordEntrada.readInt();
			int number;
			int numberIsPrime;
			
			for(int i = 0;i < pendingOperations;i++) {
				number = coordEntrada.readInt();	
				numberIsPrime = coordEntrada.readInt();
				
				if(numberIsPrime == 1)
					writeFile.printf("O valor %d eh primo\n", number);
				else if(numberIsPrime == -1)
					writeFile.printf("O valor %d nao eh primo\n", number);
				else
					System.out.println("Erro Server: valor diferente de 1 e -1");
				
			}
			
			file.close();
		
		} catch (Exception e) {
			System.out.println("Erro Server WP: " + e.getMessage());
		}
		
	}
	
	
	public static boolean isPrime(int num) {
		if(num == 1)
			return false;
		
	    for(int i = 2; i < num; i++) {
	        if(num % i == 0)
	            return false;   
	    }
	    return true;
	}

}
