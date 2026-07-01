package com.spring.ollama.spring_ai_ollama.services;

import com.google.api.client.util.Value;


import com.spring.ollama.spring_ai_ollama.advisors.TokenPrintAdvisor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.google.genai.GoogleGenAiChatOptions;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@Service
public class ChatServiceImpl implements ChatService {

    private final ChatClient geminiChatClient;

    private final ResourceLoader resourceLoader;


    ChatServiceImpl(ChatClient geminiChatClient,
                    ResourceLoader resourceLoader) {
        this.geminiChatClient = geminiChatClient;
        this.resourceLoader = resourceLoader;
    }

    @Override
    public String chatGemini(String query) {
        long start = System.currentTimeMillis();
        System.out.println("Gemini chat service start");

        String queryStr = " As an expert in coding and programming . always write program in java. now reply for this query : {query}";

        var response = geminiChatClient
                .prompt()
                .user(promptUserSpec -> promptUserSpec.text(queryStr).param("query", query))
                .call()
                .content();

        System.out.println("chat -> " + (System.currentTimeMillis() - start) + " ms");
        return response;
    }


    @Override
    public String ChatTemplate() {
        // first stage
        PromptTemplate strTemplate = PromptTemplate.builder()
                .template("What is {techName}? tell me example of {exampleName}").build();

        // render the template
        String renderMsg = strTemplate.render(Map.of(
                "techName", "Spring",
                "exampleName", "Spring Boot"
        ));

        Prompt prompt = new Prompt(renderMsg);

        return geminiChatClient.prompt(prompt).call().content();
    }

    @Override
    public String SystemPromtTemplate() {
        var systemPromptTemplate = SystemPromptTemplate.builder()
                .template("You are a helpful coding assistant. You are an expert Java developer.")
                .build();

        String systemText = systemPromptTemplate.render();

        var userPromptTemplate = PromptTemplate.builder()
                .template("What is {techName}? Give an example of {exampleName}.")
                .build();

        String userText = userPromptTemplate.render(
                Map.of(
                        "techName", "Spring",
                        "exampleName", "Spring Exception"
                )
        );

        Prompt prompt = new Prompt(
                List.of(
                        new SystemMessage(systemText),
                        new UserMessage(userText)
                )
        );

        return geminiChatClient
                .prompt(prompt)
                .call()
                .content();
    }


    @Override
    public String fluentChatAPIExample() throws IOException {


        Resource userMessageResource =
                resourceLoader.getResource("classpath:prompts/user-message.st");

        String template = userMessageResource.getContentAsString(StandardCharsets.UTF_8);
        return geminiChatClient.prompt()
                .system(sys -> sys.text("You are a helpful coding assistant. You are an expert Java developer."))
                .user(usr -> usr.text(template).param("concept", "python"))
                .call()
                .content();
    }


    @Override
    public String chatTemplate(String query) throws IOException {


        Resource userMessageResource =
                resourceLoader.getResource("classpath:prompts/user-message.st");
        Resource sysMessageResource =
                resourceLoader.getResource("classpath:prompts/system-message.st");
        String userTemplate = userMessageResource.getContentAsString(StandardCharsets.UTF_8);

        String sysTemplate = sysMessageResource.getContentAsString(StandardCharsets.UTF_8);

        return geminiChatClient.prompt()
                .advisors(new TokenPrintAdvisor()/*, new SimpleLoggerAdvisor()*/)
                .system(sys -> sys.text(sysTemplate))
                .user(usr -> usr.text(userTemplate).param("concept", query))
                .call()
                .content();
    }

    @Override
    public Flux<String> streamChat(String query, String userId) throws IOException {

        System.out.println("Gemini chat service start");
        Resource userMessageResource =
                resourceLoader.getResource("classpath:prompts/user-message.st");
        Resource sysMessageResource =
                resourceLoader.getResource("classpath:prompts/system-message.st");
        String userTemplate = userMessageResource.getContentAsString(StandardCharsets.UTF_8);

        String sysTemplate = sysMessageResource.getContentAsString(StandardCharsets.UTF_8);

        return geminiChatClient.prompt()
                .advisors(advisorSpec -> advisorSpec.param(
                        ChatMemory.CONVERSATION_ID,
                        userId/*UUID.randomUUID().toString()*/
                ))
//                .advisors(new TokenPrintAdvisor()/*, new SimpleLoggerAdvisor()*/)
                .system(sys -> sys.text(sysTemplate))
                .user(usr -> usr.text(userTemplate).param("concept", query))
                .stream()
                .content();
    }

}
