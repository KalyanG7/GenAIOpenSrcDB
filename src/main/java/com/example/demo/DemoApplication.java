package com.example.demo;

import org.apache.http.HttpHost;
import org.opensearch.client.RestClient;
import org.opensearch.client.json.jackson.JacksonJsonpMapper;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.transport.OpenSearchTransport;
import org.opensearch.client.transport.Transport;
import org.opensearch.client.transport.rest_client.RestClientTransport;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.MetadataMode;
import org.springframework.ai.embedding.Embedding;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.openai.OpenAiEmbeddingOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.retry.RetryUtils;
import org.springframework.ai.vectorstore.OpenSearchVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;




@SpringBootApplication
public class DemoApplication {
	 


	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);	
		
	}

}

@Configuration
class AppConfig {
	
	
	 @Value("${spring.ai.openai.api-key}")
	 private OpenAiApi openaiApiKey;
	 

	// @Autowired
	// private EmbeddingModel embeddingModel; 
	

	 

	
 /*	@Bean
	VectorStore vectorStore(EmbeddingClient embedding) {
		return new SimpleVectorStore(embedding);
	}*/
	
 	
 	@Bean
 	VectorStore vectorStore(EmbeddingModel embeddingModel) {
 		
 		 RestClient restClient = RestClient.builder(new HttpHost("localhost", 9200, "http")).build(); 		  
 		 Transport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
 	     OpenSearchClient client = new OpenSearchClient((OpenSearchTransport) transport);
 	
 	     
      /* this.embeddingModel = new OpenAiEmbeddingModel(
 	           this.openaiApiKey,
 	           MetadataMode.EMBED,
 	           OpenAiEmbeddingOptions.builder()
 	                   .withModel("text-embedding-ada-002")
 	                   .withUser("user-6")
 	                   .build(),
 	           RetryUtils.DEFAULT_RETRY_TEMPLATE);*/
 	     
        OpenSearchVectorStore openSearchVectorStore =  new OpenSearchVectorStore(client,embeddingModel,false);
 		
 		return openSearchVectorStore;
              
	}
	
 	  @Bean
 	    ChatClient chatClient(ChatClient.Builder builder) {
 	        return builder.defaultSystem("You are a friendly chat bot that answers question in the voice of a {voice}")
 	                .build();
 	    }
	

	
	
}