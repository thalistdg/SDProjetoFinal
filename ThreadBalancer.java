import java.util.LinkedList;
import java.util.Queue;

public class ThreadBalancer extends Thread{
	
	
	private static Queue<Integer> requests;
	private static Queue<Integer> idleServers;
	
	public ThreadBalancer(Queue<Integer> requests) {
		ThreadBalancer.requests = requests;
		ThreadBalancer.idleServers = new LinkedList<Integer>();
				
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
				System.out.printf("TB requests/idleServers size %d/%d\n", requests.size(), idleServers.size());
				
				if(!requests.isEmpty() & !idleServers.isEmpty()) {
					
					int request = removeRequest();
					int server = removeIdleServer(request);
					
					if(server == 1)
						new ThreadBalancerServer(10000, request, server, idleServers).start();
					else if(server == 2)
						new ThreadBalancerServer(20000, request, server, idleServers).start();
					else if(server == 3)
						new ThreadBalancerServer(30000, request, server, idleServers).start();
					else
						System.out.println("Erro TB -2");
					
				}else {
					Thread.sleep(300);
				}	
				
			}
		}catch (Exception e) {
			System.out.println("Erro TB: " + e.getMessage());
		}
		
	}
	
	public synchronized int removeRequest() {
		if(!requests.isEmpty())
			return requests.remove();
		return -2;
	}
	
	public synchronized int removeIdleServer(int r) {
		if(r != -2 && !idleServers.isEmpty())
			return idleServers.remove();
		return -2;
	}

}
