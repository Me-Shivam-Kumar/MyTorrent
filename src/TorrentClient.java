import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TorrentClient {
	private int pieceSize;
	private PieceManager pieceManager;
	private byte[][] pieces;
	private ExecutorService executorService;
	private DataOutputStream outputStream;
	private DataInputStream inputStream;
	
	public TorrentClient(int numPieces,int pieceSize,byte[][] pieces,DataOutputStream outputStream, DataInputStream inputStream) {
		this.pieceSize=pieceSize;
		this.pieceManager=new PieceManager(numPieces);
		this.pieces=pieces;
		this.executorService=Executors.newFixedThreadPool(10);
		this.inputStream=inputStream;
		this.outputStream=outputStream;
	}
	
	//Download Piece
	public void downloadPieceFromPeer(int pieceIndex) throws IOException {
		//Using Lambda Expressions
		executorService.submit(()->{
			sendRequestMessage(outputStream,pieceIndex);
			byte[] pieceData=receivePiece(inputStream);
			if(verifyPiece(pieceData,pieceIndex)) {
				pieces[pieceIndex]=pieceData;
				pieceManager.markPieceAsCompleted(pieceIndex);
				System.out.println("Downloaded and Verified piece "+pieceIndex);
			}else {
				System.out.println("Piece Verification failed for piece "+pieceIndex);
			}
		});
	}
	
	//Upload Piece
	public void uploadPieceToPeer() {
		 executorService.submit(()->{
			 try {
				 int requestedPieceIndex=reveiveRequestMessage(inputStream);
				 if(pieceManager.hasPiece(requestedPieceIndex)) {
					 sendPiece(outputStream,requestedPieceIndex);
				 }else {
					 System.out.println("Piece "+requestedPieceIndex+" is not available");
				 }
			 }catch(IOException e) {
				 e.printStackTrace();
			 }
		 });
	}
	
	//Verify Piece
	private boolean verifyPiece(byte[] pieceData, int pieceIndex) {
		//Code to verify pieces is to be added
		return true;
	}
	
	//Send Piece
	private void sendPiece(DataOutputStream outputStream, int pieceIndex) throws IOException {
		int messageLength=5+pieceSize;
		outputStream.writeInt(messageLength);
        outputStream.writeByte(TorrentMessageTypes.PIECE); 
        outputStream.writeInt(pieceIndex);
        outputStream.write(pieces[pieceIndex]);
	}
	
	//Receive Piece
	private byte[] receivePiece(DataInputStream inputStream) {
		int messageLength;
		try {
			messageLength = inputStream.readInt();
			byte messageType=inputStream.readByte();
			int pieceIndex=inputStream.readInt();
			byte[] pieceData=new byte[messageLength-5];
			inputStream.readFully(pieceData);
			return pieceData;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	//Piece Request Message(Receive)
	private int reveiveRequestMessage(DataInputStream inputStream) throws IOException {
		int messageLength=inputStream.readInt();
		int messageType=inputStream.readByte();
		int requestedPieceIndex=inputStream.readInt();
		return requestedPieceIndex;
	}
	
	//Piece Request Message(Send)
	private void sendRequestMessage(DataOutputStream outputStream,int pieceIndex) {
		int messageLength=5;
		try {
			outputStream.write(messageLength);
			outputStream.write(TorrentMessageTypes.REQUEST);
			outputStream.write(pieceIndex);
			outputStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}