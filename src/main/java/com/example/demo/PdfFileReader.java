package com.example.demo;

import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.OpenSearchVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.ai.document.Document;

import jakarta.annotation.PostConstruct;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import net.sourceforge.tess4j.TesseractException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@Component
@RestController
public class PdfFileReader {

	private final VectorStore vectorStore;
//	private final ImageToPdfService  pdfService;

	@Autowired
	private PDFService pdfService;

	@Value("classpath:*.pdf")
	private Resource[] pdfResource;

	public PdfFileReader(VectorStore vectorStore) {
		this.vectorStore = vectorStore;
	}

	@CrossOrigin(origins = "http://127.0.0.1:4200")
	@GetMapping("/demo/process")
	public String process() {

		var config = PdfDocumentReaderConfig.builder()
				.withPageExtractedTextFormatter(new ExtractedTextFormatter.Builder().build()).build();

		for (Resource resource : pdfResource) {
			var pdfReader = new PagePdfDocumentReader(resource, config);
			var textSplitter = new TokenTextSplitter();
			vectorStore.accept(textSplitter.apply(pdfReader.get()));
		}

		/*
		 * for (Resource resource : pdfResource) { List<Document> documents = new
		 * ArrayList<>(); var pdfReader = new PagePdfDocumentReader(resource, config);
		 * var textSplitter = new TokenTextSplitter();
		 * documents.addAll(textSplitter.apply(pdfReader.get()));
		 * System.out.println(" Id--"+documents.getLast().getId());
		 * vectorStore.add(documents); }
		 */

		return "Document Processed Successfully";
	}

	@CrossOrigin(origins = "http://127.0.0.1:4200")
	@GetMapping("/demo/convert-to-pdf")
	public String extractTextFromPdf() {
		try {

			File pdfSourceFile = new File(
					"D:\\GenAIWSDB\\GenAIDemoDB\\src\\main\\resources\\pdfImage\\PrintingNeeded.pdf");

			// Convert MultipartFile to File
			// File pdfFile = convertMultiPartToFile(pdfSourceFile);

			// Extract text from PDF images
			List<String> extractedText = pdfService.extractTextFromPdf(pdfSourceFile);

			// Create a new document
			PDDocument convertedDocument = new PDDocument();

			for (int pageNum = 0; pageNum < extractedText.size(); pageNum++) {
				pdfService.generatePdf(extractedText.get(pageNum), convertedDocument);
			}

			File dir = new File("D:\\GenAIWSDB\\GenAIDemoDB\\src\\main\\resources");
			// Save the PDF to the specified folder
			convertedDocument.save(new File(dir, "Output" + ".pdf"));
			convertedDocument.close();

			// Set headers
			/*
			 * HttpHeaders headers = new HttpHeaders();
			 * headers.setContentType(MediaType.APPLICATION_PDF);
			 * headers.setContentDispositionFormData("inline", "document.pdf");
			 */

			return "PDF Created Successfully";

			// Return the PDF as a response
			// return new ResponseEntity<byte[]>(pdfBytes, headers, HttpStatus.OK);

			// return new ResponseEntity<>(extractedText, HttpStatus.OK);
		} catch (IOException | TesseractException e) {
			e.printStackTrace();
			return HttpStatus.INTERNAL_SERVER_ERROR.toString();
		}
	}

	private File convertMultiPartToFile(MultipartFile file) throws IOException {
		File convFile = new File("D:\\GenAIDoc\\testing\\" + file.getOriginalFilename());
		file.transferTo(convFile);
		return convFile;
	}

	/*
	 * @CrossOrigin(origins = "http://127.0.0.1:4200")
	 * 
	 * @GetMapping("/demo/convert-to-pdf") public void processImage() {
	 * 
	 * String imageDir = "D:\\GenAIWSDB\\GenAIDemoDB\\src\\main\\resources\\images";
	 * String pdfOutputPath = "/GenAIDemoDB/src/main/resources/output.pdf";
	 * 
	 * try { this.pdfService.convertImagesToPdf(imageDir, pdfOutputPath);
	 * 
	 * } catch (Exception e) { e.printStackTrace();
	 * 
	 * }
	 * 
	 * 
	 * 
	 * }
	 */

}