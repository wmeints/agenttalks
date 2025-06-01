package nl.fizzylogic.newscast.reader.agent;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.core.credential.KeyCredential;
import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.agents.chatcompletion.ChatCompletionAgent;
import com.microsoft.semantickernel.aiservices.openai.chatcompletion.OpenAIChatCompletion;
import com.microsoft.semantickernel.implementation.templateengine.tokenizer.DefaultPromptTemplate;
import com.microsoft.semantickernel.semanticfunctions.PromptTemplate;
import com.microsoft.semantickernel.semanticfunctions.PromptTemplateConfig;
import com.microsoft.semantickernel.services.chatcompletion.AuthorRole;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import com.microsoft.semantickernel.services.chatcompletion.ChatMessageContent;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.RequestScoped;
import mutiny.zero.flow.adapters.AdaptersToFlow;
import nl.fizzylogic.newscast.reader.shared.EmbeddedResource;

@RequestScoped
public class SummarizerAgentImpl implements SummarizerAgent {
    @ConfigProperty(name = "language-model.api-key")
    String languageModelApiKey;

    @ConfigProperty(name = "language-model.endpoint")
    String languageModelEndpoint;

    @ConfigProperty(name = "language-model.chat-completion.model-id")
    String languageModelChatCompletionModelId;

    public Uni<String> summarizeContent(String content) {
        ChatCompletionAgent agent = createCompletionAgent();
        ChatMessageContent<?> input = new ChatMessageContent<>(AuthorRole.USER, createUserInstructions(content));

        var response = agent.invokeAsync(input);
        var responseStream = Uni.createFrom().publisher(AdaptersToFlow.publisher(response));
        return responseStream.onItem().transform(item -> item.getFirst().getMessage().getContent());
    }

    private ChatCompletionAgent createCompletionAgent() {
        return ChatCompletionAgent.builder()
                .withKernel(createKernel())
                .withName("Summarizer")
                .withDescription("Summarizes content by extracting key points and actionable advice.")
                .withTemplate(createSystemInstructions())
                .build();
    }

    private String createUserInstructions(String content) {
        String instructions = EmbeddedResource.read("user-instructions.txt");
        if (instructions == null || instructions.isEmpty()) {
            throw new IllegalStateException("user-instructions.txt not found or empty");
        }

        return instructions.replace("{{article_content}}", content);
    }

    private PromptTemplate createSystemInstructions() {
        String instructions = EmbeddedResource.read("system-instructions.txt");

        if (instructions == null || instructions.isEmpty()) {
            throw new IllegalStateException("system-instructions.txt not found or empty");
        }

        return DefaultPromptTemplate.build(PromptTemplateConfig.builder().withTemplate(instructions).build());
    }

    private Kernel createKernel() {
        return Kernel.builder().withAIService(ChatCompletionService.class, createChatCompletionService()).build();
    }

    private ChatCompletionService createChatCompletionService() {
        return OpenAIChatCompletion.builder().withOpenAIAsyncClient(createOpenAIClient())
                .withModelId(languageModelChatCompletionModelId)
                .build();
    }

    private OpenAIAsyncClient createOpenAIClient() {
        OpenAIAsyncClient client = new OpenAIClientBuilder()
                .endpoint(languageModelEndpoint)
                .credential(new KeyCredential(languageModelApiKey))
                .buildAsyncClient();

        return client;
    }
}
