package com.example.indexquiz.useranswer.application.port.out;

import com.example.indexquiz.useranswer.domain.VisitorProgress;

public interface GetVisitorProgressPort {

    VisitorProgress getByVisitorId(String visitorId);
}
