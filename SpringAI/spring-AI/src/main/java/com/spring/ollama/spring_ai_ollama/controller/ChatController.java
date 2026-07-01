package com.spring.ollama.spring_ai_ollama.controller;


import com.spring.ollama.spring_ai_ollama.services.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.beans.factory.annotation.Qualifier;
import reactor.core.publisher.Flux;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/")
public class ChatController {

    @Autowired
    ChatService chatService;

    @GetMapping("/chat")
    public ResponseEntity<String> chatGoogle(
            @RequestParam("q") String query) {
        return ResponseEntity.ok(chatService.chatGemini(query));
    }

    @GetMapping("/chatAdvisory")
    public ResponseEntity<String> chatAdvisoryImp(
            @RequestParam("q") String query) throws IOException {
        return ResponseEntity.ok(chatService.chatTemplate(query));
    }


    @GetMapping("/streamChat")
    public ResponseEntity<Flux<String>> streamChat(
            @RequestParam("q") String query, @RequestHeader("userId") String userId) throws IOException {
        return ResponseEntity.ok(chatService.streamChat(query, userId));
    }
}