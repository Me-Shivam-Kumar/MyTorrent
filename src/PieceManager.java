import java.util.BitSet;
public class PieceManager {
	private final int numPieces;
	private final BitSet completedPieces;
	
	public PieceManager(int numPieces) {
		this.numPieces=numPieces;
		this.completedPieces=new BitSet(numPieces);
	}
	public synchronized boolean hasPiece(int pieceIndex) {
		return completedPieces.get(pieceIndex);
	}
	public synchronized void markPieceAsCompleted(int pieceIndex) {
		completedPieces.set(pieceIndex);
	}
	public synchronized void markPieceAsInComplete(int pieceIndex) {
		completedPieces.clear(pieceIndex);
	}
	public int getNumPieces() {
		return numPieces;
	}
}
