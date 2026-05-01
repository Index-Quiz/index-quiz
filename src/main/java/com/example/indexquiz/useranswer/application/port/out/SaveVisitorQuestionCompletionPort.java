package com.example.indexquiz.useranswer.application.port.out;

import com.example.indexquiz.useranswer.domain.VisitorQuestionCompletion;

public interface SaveVisitorQuestionCompletionPort {

    void saveIfNotExists(VisitorQuestionCompletion completion);
}
