package com.example.demo;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import javax.imageio.ImageIO;

import org.springframework.stereotype.Service;



import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ImageToPdfService {

    public void convertImagesToPdf(String imageDirectoryPath, String pdfOutputPath) throws Exception {
    	
    	/*
        // Create a PDF document
        Document document = new Document(PageSize.A4);
       
        
        File pdfFile = new File("Output.pdf");
        PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
        
        // Open the document to write into it
        document.open();
        
        // Read all image files from the directory
        File imageDirectory = new File(imageDirectoryPath);
        File[] imageFiles = imageDirectory.listFiles(file -> 
          //  file.isFile() && isImageFile(file)
        	file.isFile() 
        );
        
        if (imageFiles != null) {
            // Sort files by name or modify the comparator as needed
            Arrays.sort(imageFiles);

            // Loop through the images and add them to the PDF
            for (File imageFile : imageFiles) {
                Image image = Image.getInstance(imageFile.getAbsolutePath());
                image.scaleToFit(PageSize.A4.getWidth(), PageSize.A4.getHeight());
                document.add(image);
                document.newPage();  // Create a new page for each image
            }
        }
        
        // Close the document after writing
        document.close();*/
    }

    private boolean isImageFile(File file) {
        try {
            return ImageIO.read(file) != null;
        } catch (IOException e) {
            return false;
        }
    }
}
