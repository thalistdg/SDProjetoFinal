import java.util.LinkedList;
import java.util.Queue;

public class ThreadBalancer extends Thread {
	
	private Queue<Integer> requests;
	public Queue<Integer> idleServers;
	
	public ThreadBalancer(Queue<Integer> requests) {
		this.requests = requests;
		this.idleServers = new LinkedList<Integer>();
				
		try {

			idleServers.add(1);
			idleServers.add(2);
			idleServers.add(3);	

		}catch(Exception e) {	
			System.out.println("Erro TB: " + e.getMessage());
		}
	}
	
	public synchronized void run() {
		
		try {
			while(true) {	
				System.out.printf("TB request/server size %d %d\n", requests.size(), idleServers.size());		
				if(!requests.isEmpty() & !idleServers.isEmpty()) {
			
					int request = removeRequest();
					int server = removeIdleServer();			
					
					if(server == 1)
						new ThreadBalancerServer(10000, request, server, idleServers).start();
					else if(server == 2)
						new ThreadBalancerServer(20000, request, server, idleServers).start();
					else 
						new ThreadBalancerServer(30000, request, server, idleServers).start();
					
				}else {
					Thread.sleep(1000);
				}	
				
			}
		}catch (Exception e) {
			System.out.println("Erro TB: " + e.getMessage());
		}
		
	}
	
	public synchronized int removeRequest() {
		return requests.remove();
	}
	
	public synchronized int removeIdleServer() {
		return idleServers.remove();
	}

}
