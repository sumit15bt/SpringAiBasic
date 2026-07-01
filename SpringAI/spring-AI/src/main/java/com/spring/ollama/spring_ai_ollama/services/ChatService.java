package com.spring.ollama.spring_ai_ollama.services;

import reactor.core.publisher.Flux;

import java.io.IOException;

public interface ChatService {
     String chatGemini(String query);

     String ChatTemplate();

     String SystemPromtTemplate();

     String fluentChatAPIExample() throws IOException;

     String chatTemplate(String query) throws IOException;

     Flux<String> streamChat(String query, String userId) throws IOException;
}
