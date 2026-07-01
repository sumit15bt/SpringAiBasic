package com.spring.ollama.spring_ai_ollama.controller;


import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.beans.factory.annotation.Qualifier;

@RestController
@RequestMapping("/api/v1/chat")
public class ChatController {


    private final ChatClient ollamaChatClient;


    private final ChatClient geminiChatClient;



    ChatController( @Qualifier("ollamaChatClient") ChatClient ollamaChatClient,
                    @Qualifier("geminiChatClient") ChatClient geminiChatClient) {
        this.ollamaChatClient = ollamaChatClient;
        this.geminiChatClient = geminiChatClient;
    }
    @GetMapping("/ollama")
    public ResponseEntity<String> chatOllama(
            @RequestParam("q") String query) {

        long start = System.currentTimeMillis();

        String response = ollamaChatClient
                .prompt()
                .user(query)
                .call()
                .content();

        System.out.println("chat -> " + (System.currentTimeMillis() - start) + " ms");

        return ResponseEntity.ok(response);
    }

    @GetMapping("/gemini")
    public ResponseEntity<String> chatGoogle(
            @RequestParam("q") String query) {

        long start = System.currentTimeMillis();

        String response = geminiChatClient
                .prompt()
                .user(query)
                .call()
                .content();

        System.out.println("chat -> " + (System.currentTimeMillis() - start) + " ms");

        return ResponseEntity.ok(response);
    }
}