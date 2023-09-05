import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;

public class MessageManager {
	private Socket soc;
	private DataInputStream inputStream;
	private DataOutputStream outputStream;
	
	public MessageManager(Peer peer) {
		super();
		try {
			soc=new Socket(peer.getIpAddress(),peer.getPort());
			inputStream=new DataInputStream(soc.getInputStream());
			outputStream=new DataOutputStream(soc.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendIntrestedMessage() {
		sendMessage(new byte[] {0,0,0,1,TorrentMessageTypes.INTERESTED});
	}
	public void sendNotInterestedMessage() {
		sendMessage(new byte[] {0,0,0,1,TorrentMessageTypes.NOT_INTERESTED});
	}
	public void sendHaveMessage(int pieceIndex) {
		byte[] payload=ByteBuffer.allocate(4).putInt(pieceIndex).array();
		sendMessage(new byte[] {0,0,0,(byte)(1+payload.length),TorrentMessageTypes.HAVE},payload);
	}
	public byte[] receiveMessage() {
		try {
			int length=inputStream.readInt();
			if(length==0) {
				return new byte[] {TorrentMessageTypes.KEEP_ALIVE};
			}
			byte messageType=inputStream.readByte();
			byte[] payload=new byte[length-1];
			inputStream.readFully(payload);
			return new byte[] {messageType};
		}catch(IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	public void sendMessage(byte[] lengthPrefix,byte[] payload) {
		try {
			outputStream.write(lengthPrefix);
			outputStream.write(payload);
			outputStream.flush();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	public void sendMessage(byte[] message) {
		try {
			outputStream.write(message);
			outputStream.flush();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	public void close() {
		try {
			soc.close();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	
}
