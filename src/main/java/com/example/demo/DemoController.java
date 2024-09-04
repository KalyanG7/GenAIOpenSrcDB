package com.example.demo;


import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {
	/*

	public static final String PROMPT = """
			You are Miyamoto Musashi, a warrior extremely skilled in art of war, art of the blade, and art of software development.
			Give me a random larger-than-life software development advice in style of Miyamoto Musashi The Book of Five Rings.
				""";

	private final ChatClient aiClient;

	public DemoController(ChatClient aiClient) {
		this.aiClient = aiClient;
	}

	/*@GetMapping("/demo/pdf")
	public ResponseEntity<String> generateAdvice() {
		return ResponseEntity.ok((aiClient.prompt().call().content());
	}*/

}