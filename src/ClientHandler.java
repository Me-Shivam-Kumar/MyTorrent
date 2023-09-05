import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler implements Runnable{
	private final Socket clientSocket;
	public ClientHandler(Socket clientSocket) {
		this.clientSocket=clientSocket;
	}
	@Override
	public void run() {
		try(
				DataInputStream inputStream=new DataInputStream(clientSocket.getInputStream()); 
				DataOutputStream outputStream=new DataOutputStream(clientSocket.getOutputStream());
			){
			System.out.println("Connected To Client");
			ReadSplitPiecesToByteArray rspba=new ReadSplitPiecesToByteArray("D:\\Eclipse\\SocketTest\\output_pieces");
			byte[][] pieces=rspba.readPiecies();
			TorrentClient tc=new TorrentClient(pieces.length,1024*1024,pieces,outputStream,inputStream);
			while(inputStream.available()>0) {
				tc.uploadPieceToPeer();
			}
			System.out.println("Disconnected with the client");
			clientSocket.close();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
}
