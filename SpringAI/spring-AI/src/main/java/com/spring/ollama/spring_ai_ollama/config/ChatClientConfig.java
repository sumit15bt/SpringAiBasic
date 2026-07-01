package com.spring.ollama.spring_ai_ollama.config;

import com.spring.ollama.spring_ai_ollama.advisors.TokenPrintAdvisor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SafeGuardAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.google.genai.GoogleGenAiChatModel;
//import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.google.genai.GoogleGenAiChatOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ChatClientConfig {

    Logger logger = LoggerFactory.getLogger(ChatClientConfig.class);

//    @Bean("geminiChatClient")
    @Bean
    public ChatClient geminiChatClient(
            GoogleGenAiChatModel googleGenAiChatModel, ChatMemory chatMemory) {

        logger.info("In ChatClientConfig"+ chatMemory.getClass().getName());
        MessageChatMemoryAdvisor messageChatMemoryAdvisor = MessageChatMemoryAdvisor.builder(chatMemory).build();


        return ChatClient.builder(googleGenAiChatModel)
                .defaultAdvisors(messageChatMemoryAdvisor, new SafeGuardAdvisor(List.of("games","sex")))
                .defaultOptions(GoogleGenAiChatOptions.builder()
                        .temperature(0.3)
                        .maxOutputTokens(500)
                .build()).build();
    }

//    @Bean("ollamaChatClient")
//    public ChatClient ollamaChatClient(
//            OllamaChatModel ollamaChatModel) {
//
//        return ChatClient.builder(ollamaChatModel)
//                .build();
//    }
}
