package com.spring.ollama.spring_ai_ollama.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.google.genai.GoogleGenAiChatModel;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatClientConfig {

    @Bean("geminiChatClient")
    public ChatClient geminiChatClient(
            GoogleGenAiChatModel googleGenAiChatModel) {

        return ChatClient.builder(googleGenAiChatModel)
                .build();
    }

    @Bean("ollamaChatClient")
    public ChatClient ollamaChatClient(
            OllamaChatModel ollamaChatModel) {

        return ChatClient.builder(ollamaChatModel)
                .build();
    }
}
