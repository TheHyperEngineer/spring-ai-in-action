package com.hypeng.example.boardgamebuddy.service;

import com.hypeng.example.boardgamebuddy.dto.Answer;
import com.hypeng.example.boardgamebuddy.dto.Question;

public interface BoardGameService {
    Answer askQuestion(Question question, String conversationId);
}

