import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ReadSplitPiecesToByteArray {
	private String inputDirectory;
	public ReadSplitPiecesToByteArray(String inputDirectory){
		this.inputDirectory=inputDirectory;
	}
	public byte[][] readPiecies() {
        File[] files = new File(inputDirectory).listFiles();

        if (files == null || files.length == 0) {
            System.err.println("No input files found in the directory.");
            return null;
        }
        
        List<byte[]> packetsList = new ArrayList<>();

        for (File file : files) {
            if (file.isFile()) {
                try (FileInputStream fis = new FileInputStream(file);
                     ByteArrayOutputStream baos = new ByteArrayOutputStream()
                ) {
                    byte[] buffer = new byte[1024]; // You can adjust the buffer size as needed
                    int bytesRead;

                    while ((bytesRead = fis.read(buffer)) != -1) {
                        baos.write(buffer, 0, bytesRead);
                    }

                    packetsList.add(baos.toByteArray());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        // Convert the List<byte[]> to byte[][]
        byte[][] packetsArray = new byte[packetsList.size()][];
        packetsList.toArray(packetsArray);
        return packetsArray;
	}
   
}
