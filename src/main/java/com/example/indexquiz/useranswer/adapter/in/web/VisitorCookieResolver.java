package com.example.indexquiz.useranswer.adapter.in.web;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class VisitorCookieResolver {

    private static final String COOKIE_NAME = "visitor_id";
    private static final int MAX_AGE_SECONDS = 60 * 60 * 24 * 365;

    public String resolve(HttpServletRequest request, HttpServletResponse response) {
        return findVisitorId(request)
                .orElseGet(() -> createVisitorCookie(response));
    }

    public Optional<String> findExisting(HttpServletRequest request) {
        return findVisitorId(request);
    }

    private Optional<String> findVisitorId(HttpServletRequest request) {
        List<Cookie> cookies = Optional.ofNullable(request.getCookies())
                .map(Arrays::asList)
                .orElseGet(Collections::emptyList);
        return cookies.stream()
                .filter(cookie -> COOKIE_NAME.equals(cookie.getName()))
                .map(Cookie::getValue)
                .filter(this::isValidUuid)
                .findAny();
    }

    private boolean isValidUuid(String value) {
        try {
            UUID.fromString(value);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private String createVisitorCookie(HttpServletResponse response) {
        String visitorId = UUID.randomUUID().toString();
        Cookie cookie = new Cookie(COOKIE_NAME, visitorId);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(MAX_AGE_SECONDS);
        response.addCookie(cookie);
        return visitorId;
    }
}
