import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Random;

public class Client {

	public static void main(String[] args) {
		int port = 6000;
		
		try {
			int count = 1;
			while(true) {

				Socket clientSocket = new Socket("127.0.0.1", port);

				ObjectOutputStream saida = new ObjectOutputStream(clientSocket.getOutputStream());
				saida.flush();				
				
				Random gerador = new Random();
				int num = gerador.nextInt(2);
				
				
				if(num == 1) { // Operacao de escrita
					num = gerador.nextInt(1000000-1) + 2;
				}
				saida.writeInt(num);
				saida.flush();
				
				System.out.printf("%d - Cliente enviou: %d\n", count, num);
				
				clientSocket.close();
				Thread.sleep(300);
				++count;
			}
			
		}catch(Exception e) {
			System.out.println("Erro: " + e.getMessage());
		}

	}

}
