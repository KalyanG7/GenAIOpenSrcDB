package com.example.demo;


import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import net.sourceforge.tess4j.util.LoadLibs;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class PDFService {

    // Set Tesseract OCR data path
    private static final String TESSERACT_DATA_PATH = "D:/GenAIWSDB/GenAIDemoDB/Tesseract-OCR";

    private final ITesseract tesseract;

    public PDFService() {
        tesseract = new Tesseract();
        tesseract.setDatapath(TESSERACT_DATA_PATH); // Set Tesseract data path
    }
    
    public static BufferedImage preprocessImage(BufferedImage image) throws Exception {
        //BufferedImage image = ImageIO.read(imageFile);

        // Convert to grayscale (optional but improves results)
        BufferedImage grayImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        grayImage.getGraphics().drawImage(image, 0, 0, null);

        // Apply additional preprocessing as needed (e.g., thresholding, resizing, etc.)
        // Example: Resize image (increase size by 2x)
        //BufferedImage resizedImage = new BufferedImage(image.getWidth() * 2, image.getHeight() * 2, BufferedImage.TYPE_INT_RGB);
        //resizedImage.getGraphics().drawImage(grayImage, 0, 0, image.getWidth() * 2, image.getHeight() * 2, null);

        return grayImage;
    }

    public List<String> extractTextFromPdf(File pdfFile) throws IOException, TesseractException {
        List<String> extractedTexts = new ArrayList<>();
        
        File tessDataFolder = LoadLibs.extractTessResources("tessdata");

      //Set the tessdata path
        tesseract.setDatapath(tessDataFolder.getAbsolutePath());
        
        // Load PDF document   
        
        PDDocument document =  Loader.loadPDF(pdfFile);
        PDFRenderer pdfRenderer = new PDFRenderer(document);
        
     // Set the language to English
        tesseract.setLanguage("eng");
        //tesseract.setTessVariable("tessedit_char_whitelist", "0123456789/-");

        
        // Loop through each page
        for (int page = 0; page < document.getNumberOfPages(); page++) {
            // Render the page as an image
            BufferedImage bufferedImage = pdfRenderer.renderImageWithDPI(page, 400);          
            try {
				bufferedImage = preprocessImage(bufferedImage);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

            // Save image for debugging (optional)
            File outputImage = new File("page_" + page + ".png");
            ImageIO.write(bufferedImage, "png", outputImage);
            
            

            // Perform OCR on the image
            String extractedText = tesseract.doOCR(outputImage);
            extractedTexts.add(extractedText);
        }

        document.close();
        return extractedTexts;
    }
    
    public void generatePdf(String content, PDDocument document) throws IOException {
       

        // Add a page to the document
        PDPage page = new PDPage();        
        
     // Split the content into lines using '\n' as delimiter
        String[] lines = content.split("\n");

        // Create a content stream for writing to the page
      //  ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
            // Set font and write content
            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);
            contentStream.beginText();
            //contentStream.setLeading(15.5f);            
            contentStream.newLineAtOffset(100, 700);
            //contentStream.showText(content);
         // Loop through each line and write to the PDF
            for (String line : lines) {
                contentStream.showText(line);  // Show text line
                //contentStream.newLine();       // Move to the next line
                contentStream.newLineAtOffset(0, -15);
            }
            contentStream.endText();
            contentStream.close();
            document.addPage(page);            
        }
    }
}

