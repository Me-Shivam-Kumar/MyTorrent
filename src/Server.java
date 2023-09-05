import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
	public static void main(String[] args) {
		int port=8080;
		int maxClients=25;
		ExecutorService executorService=Executors.newFixedThreadPool(maxClients);
		try(ServerSocket serverSocket=new ServerSocket(port)) {
			System.out.println("Server is listening on port number "+port);
			while(true) {
				Socket clientSocket=serverSocket.accept();
				Runnable clientHandler=new ClientHandler(clientSocket);
				executorService.submit(clientHandler);
			}
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
}

