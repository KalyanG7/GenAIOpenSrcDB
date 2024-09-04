package com.example.demo;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.opensearch.action.admin.indices.delete.DeleteIndexRequest;
import org.opensearch.action.delete.DeleteRequest;
import org.opensearch.action.delete.DeleteResponse;
import org.opensearch.action.get.GetRequest;
import org.opensearch.action.get.GetResponse;
import org.opensearch.action.index.IndexRequest;
import org.opensearch.action.index.IndexResponse;
import org.opensearch.action.support.master.AcknowledgedResponse;
import org.opensearch.client.OpenSearchClient;
import org.opensearch.client.RequestOptions;
import org.opensearch.client.RestClient;
import org.opensearch.client.RestClientBuilder;
import org.opensearch.client.RestHighLevelClient;
import org.opensearch.client.indices.CreateIndexRequest;
import org.opensearch.client.indices.CreateIndexResponse;
import org.opensearch.common.settings.Settings;
import org.springframework.ai.document.Document;
import org.springframework.stereotype.Component;

@Component
public class OpenSearchService {
	
	  RestClientBuilder builder;
	  RestHighLevelClient client; 
	
	 private final OpenSearchClient openSearchCli = null;
	
	 public OpenSearchService() {
		  builder = RestClient.builder(new HttpHost("localhost", 9200, "http"));
	      client = new RestHighLevelClient(builder);	      
	    
	}
	 
	 
	 public void createIndex() {
		  //Create a non-default index with custom settings and mappings.
	      CreateIndexRequest createIndexRequest = new CreateIndexRequest("custom-index");
	      
	      createIndexRequest.settings(Settings.builder() //Specify in the settings how many shards you want in the index.
	    	      .put("index.number_of_shards", 4)
	    	      .put("index.number_of_replicas", 3)
	    	      );
	    	    //Create a set of maps for the index's mappings.
	    	    HashMap<String, String> typeMapping = new HashMap<String,String>();
	    	    typeMapping.put("type", "integer");
	    	    HashMap<String, Object> ageMapping = new HashMap<String, Object>();
	    	    ageMapping.put("age", typeMapping);
	    	    HashMap<String, Object> mapping = new HashMap<String, Object>();
	    	    mapping.put("properties", ageMapping);
	    	    createIndexRequest.mapping(mapping);
	    	    try {
					CreateIndexResponse createIndexResponse = client.indices().create(createIndexRequest, RequestOptions.DEFAULT);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

	 }

	//Adding data to the index.
	public void addData(String id, String message) {
		  IndexRequest request = new IndexRequest("custom-index"); //Add a document to the custom-index we created.
		    request.id(id); //Assign an ID to the document.

		    HashMap<String, String> stringMapping = new HashMap<String, String>();
		    stringMapping.put("message:", message);
		    request.source(stringMapping); 
		    System.out.print("'Message Saved");
		    //Place your content into the index's source.
		/*    try {
				IndexResponse indexResponse = client.index(request, RequestOptions.DEFAULT);
				  //Getting back the document
			    GetRequest getRequest = new GetRequest("custom-index", "1");
			    GetResponse response = client.get(getRequest, RequestOptions.DEFAULT);

			    System.out.println(response.getSourceAsString()); 
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/

		  
	}
  
	public void addData(List<Document> documents) {
		    IndexRequest request = new IndexRequest("pdfdocuments"); //Add a document to the custom-index we created.
		    request.source(documents); 
		    
		    //Place your content into the index's source.
		/*    try {
				IndexResponse indexResponse = client.index(request, RequestOptions.DEFAULT);
				  //Getting back the document
			    GetRequest getRequest = new GetRequest("custom-index", "1");
			    GetResponse response = client.get(getRequest, RequestOptions.DEFAULT);

			    System.out.println(response.getSourceAsString()); 
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/

		  
	}


	

}
