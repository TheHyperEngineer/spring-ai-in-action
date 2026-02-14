package com.hypeng.example.boardgamebuddy.controller;

import com.hypeng.example.boardgamebuddy.dto.Question;
import com.hypeng.example.boardgamebuddy.service.BoardGameService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
public class AskController {

    private final BoardGameService boardGameService;

    public AskController(BoardGameService boardGameService) {
        this.boardGameService = boardGameService;
    }

    @PostMapping(path = "/ask", produces = "application/ndjson")
    public Flux<String> ask(@RequestBody @Valid Question question) {
        return boardGameService.askQuestion(question);
    }
    @GetMapping(path = "/question", produces = "text/event-stream")
    public Flux<String> question(@RequestParam String gameTitle, @RequestParam String question) {
        Question question1 = new Question(gameTitle, question);
        return boardGameService.askQuestion(question1);
    }


}
