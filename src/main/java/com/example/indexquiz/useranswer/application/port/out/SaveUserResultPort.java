package com.example.indexquiz.useranswer.application.port.out;

import com.example.indexquiz.useranswer.domain.UserResult;

public interface SaveUserResultPort {

    UserResult saveUserResult(UserResult userResult);
}
