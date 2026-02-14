package com.hypeng.example.boardgamebuddy.service;

import com.hypeng.example.boardgamebuddy.dto.Question;
import reactor.core.publisher.Flux;

public interface BoardGameService {
    Flux<String> askQuestion(Question question);
}
