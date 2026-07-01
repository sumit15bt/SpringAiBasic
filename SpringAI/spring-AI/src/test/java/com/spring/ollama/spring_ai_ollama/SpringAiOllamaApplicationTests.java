package com.spring.ollama.spring_ai_ollama;

import com.spring.ollama.spring_ai_ollama.services.ChatService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
class SpringAiOllamaApplicationTests {

	@Test
	void contextLoads() {
	}

	@Autowired
	ChatService chatService;

	@Test
	void testUserTemplateRenderer() {
		System.out.println("test Template Renderer");
		var res = chatService.ChatTemplate();
		System.out.println(res.toString());
	}

	@Test
	void testSystemUserTemplateRenderer() {
		System.out.println("test system User Template Renderer");
		var res = chatService.SystemPromtTemplate();
		System.out.println(res.toString());
	}

	@Test
	void testfluentChatAPIExample() throws IOException {
		System.out.println("test fluentChatAPI");
		var res = chatService.fluentChatAPIExample();
		System.out.println(res.toString());
	}

}
