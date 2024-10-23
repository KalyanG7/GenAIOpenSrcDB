package com.example.demo;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatController {

    private final ChatClient aiClient;
    private final VectorStore vectorStore;

    public ChatController(ChatClient aiClient, VectorStore vectorStore) {
        this.aiClient = aiClient;
        this.vectorStore = vectorStore;
    }

    @CrossOrigin(origins = "http://127.0.0.1:4200")
    @GetMapping("/demo/answerpdf")
    public ResponseEntity<String> generateAnswer(@RequestParam String query) {
    	
    	 String customQuery = query; //+ " and it page number";
    	
    	  List<Document> similarDocuments = vectorStore.similaritySearch(customQuery);
          String information = similarDocuments.stream()
                  .map(Document::getContent)
                  .collect(Collectors.joining(System.lineSeparator()));
          var systemPromptTemplate = new SystemPromptTemplate(
                  """
                              You are a helpful assistant.
                              Use only the following information to answer the question.
                              Do not use any other information. If you do not know, simply answer: Unknown.

                              {information}
                          """);
          var systemMessage = systemPromptTemplate.createMessage(Map.of("information", information));
          var userPromptTemplate = new PromptTemplate("{query}");
          var userMessage = userPromptTemplate.createMessage(Map.of("query", customQuery));
          var prompt = new Prompt(List.of(systemMessage, userMessage));
          var aiResponse = aiClient.prompt(prompt).call().content();         
       //   System.out.println("Ai response"+aiResponse);
       //   aiResponse.replace("The document provided does not have page numbers", "");
          return ResponseEntity.ok(aiResponse);
      
    }
}