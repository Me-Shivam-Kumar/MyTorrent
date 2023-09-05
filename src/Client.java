import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client {
	public static void main(String[] args) {
		//Will Get this from a tracker
		String serverIp="127.0.0.1";
		int serverPort=8080;
		
		try(Socket socket=new Socket(serverIp,serverPort);DataInputStream inputStream=new DataInputStream(socket.getInputStream());DataOutputStream outputStream=new DataOutputStream(socket.getOutputStream())){
			int noOfPieces=100;
			int pieceSize=1024*1024;
			byte[][] pieces=new byte[noOfPieces][pieceSize];
			TorrentClient tc=new TorrentClient(noOfPieces,pieceSize,pieces,outputStream,inputStream);
			tc.downloadPieceFromPeer(2);
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
}
