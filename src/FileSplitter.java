import java.io.*;

public class FileSplitter {

    public static void splitFile(String inputFilePath, String outputDirectory, int pieceSize) {
        File inputFile = new File(inputFilePath);
        if (!inputFile.exists() || !inputFile.isFile()) {
            System.err.println("Input file does not exist or is not a regular file.");
            return;
        }

        File outputDir = new File(outputDirectory);
        if (!outputDir.exists() && !outputDir.mkdirs()) {
            System.err.println("Unable to create output directory.");
            return;
        }

        try (FileInputStream fis = new FileInputStream(inputFile)) {
            byte[] buffer = new byte[pieceSize];
            int bytesRead;
            int pieceNumber = 0;

            while ((bytesRead = fis.read(buffer)) != -1) {
                String pieceFilePath = outputDirectory + File.separator + "piece_" + pieceNumber + ".dat";
                try (FileOutputStream fos = new FileOutputStream(pieceFilePath)) {
                    fos.write(buffer, 0, bytesRead);
                }

                pieceNumber++;
            }

            System.out.println("Split " + inputFilePath + " into " + pieceNumber + " pieces.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String inputFilePath = "C:\\Users\\Shivansh Chhokar\\Downloads\\SQA.pdf";
        String outputDirectory = "output_pieces";
        int pieceSize = 1024 * 1024; // 1 MB piece size (adjust as needed)

        splitFile(inputFilePath, outputDirectory, pieceSize);
    }
}
