package com.hypeng.example.boardgamebuddy;


/*@RestClientTest(components = {
    AiConfig.class,
    GameTools.class,
    SpringAiBoardGameService.class,
    BoardGameService.class})*/
public class SpringAiBoardGameServiceMockOpenAiTests {

    /*@MockitoBean
    GameRepository gameRepository;

    @MockitoBean
    VectorStore vectorStore;

    @Autowired
    MockRestServiceServer mockServer;

    @Autowired
    SpringAiBoardGameService service;

    @TestConfiguration
    public static class TestConfig {
        @Bean
        public ChatClient.Builder chatClientBuilder(RestClient.Builder restClientBuilder, WebClient.Builder webClientBuilder) {
            var openAiChatModel = OpenAiChatModel.builder()
                .openAiApi(
                    OpenAiApi.builder()
                        .baseUrl("https://api.openai.com")
                        .apiKey("TEST_API_KEY")
                        .restClientBuilder(restClientBuilder)
                        .webClientBuilder(webClientBuilder)
                        .build())
                .build();
            return ChatClient.builder(openAiChatModel);
        }
    }

    @Test
    public void testStuff() throws Exception {
        var expectedAnswer = "Checkers is a game for two people.";
        var content = "{\\\"gameTitle\\\":\\\"Checkers\\\", \\\"answer\\\":\\\"" + expectedAnswer + "\\\"}";
        mockOpenAiChatResponse(content);
        var answer = service.askQuestion(new Question("Checkers","How many can play?"), "conversation-id-1");
        Assertions.assertThat(answer.answer()).isEqualTo(expectedAnswer);
    }

    private void mockOpenAiChatResponse(String content) throws Exception {
        var responseResource = new ClassPathResource("/response.json");
        var st = new ST(StreamUtils.copyToString(responseResource.getInputStream(), Charset.defaultCharset()), '$', '$');
        st = st.add("content", content);
        mockServer.expect(requestTo("https://api.openai.com/v1/chat/completions"))
            .andRespond(withSuccess(st.render(), MediaType.APPLICATION_JSON));
    }

    @TestConfiguration
    public static class TestConfig2 {
        @Bean
        ToolCallbackResolver toolCallbackResolver(GenericApplicationContext applicationContext) {
            return SpringBeanToolCallbackResolver.builder()
                .applicationContext(applicationContext)
                .build();
        }
    }*/

}