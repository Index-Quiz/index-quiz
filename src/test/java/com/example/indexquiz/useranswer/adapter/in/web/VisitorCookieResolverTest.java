package com.example.indexquiz.useranswer.adapter.in.web;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.servlet.http.Cookie;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

class VisitorCookieResolverTest {

    private VisitorCookieResolver visitorCookieResolver;

    @BeforeEach
    void setUp() {
        visitorCookieResolver = new VisitorCookieResolver();
    }

    @Nested
    class Resolve {

        @Test
        void 쿠키가_없으면_새_쿠키를_생성하고_visitorId를_반환한다() {
            MockHttpServletRequest request = new MockHttpServletRequest();
            MockHttpServletResponse response = new MockHttpServletResponse();

            String visitorId = visitorCookieResolver.resolve(request, response);

            assertThat(visitorId).isNotNull();
            UUID.fromString(visitorId);
            Cookie cookie = response.getCookie("visitor_id");
            assertThat(cookie).isNotNull();
            assertThat(cookie.getValue()).isEqualTo(visitorId);
            assertThat(cookie.isHttpOnly()).isTrue();
            assertThat(cookie.getPath()).isEqualTo("/");
        }

        @Test
        void 유효한_쿠키가_있으면_기존_visitorId를_반환한다() {
            String existingId = UUID.randomUUID().toString();
            MockHttpServletRequest request = new MockHttpServletRequest();
            request.setCookies(new Cookie("visitor_id", existingId));
            MockHttpServletResponse response = new MockHttpServletResponse();

            String visitorId = visitorCookieResolver.resolve(request, response);

            assertThat(visitorId).isEqualTo(existingId);
            assertThat(response.getCookie("visitor_id")).isNull();
        }

        @Test
        void 유효하지_않은_쿠키값이면_새_쿠키를_생성한다() {
            MockHttpServletRequest request = new MockHttpServletRequest();
            request.setCookies(new Cookie("visitor_id", "invalid-value"));
            MockHttpServletResponse response = new MockHttpServletResponse();

            String visitorId = visitorCookieResolver.resolve(request, response);

            assertThat(visitorId).isNotEqualTo("invalid-value");
            UUID.fromString(visitorId);
            assertThat(response.getCookie("visitor_id")).isNotNull();
        }
    }

    @Nested
    class FindExisting {

        @Test
        void 쿠키가_있으면_visitorId를_반환한다() {
            String existingId = UUID.randomUUID().toString();
            MockHttpServletRequest request = new MockHttpServletRequest();
            request.setCookies(new Cookie("visitor_id", existingId));

            Optional<String> result = visitorCookieResolver.findExisting(request);

            assertThat(result).contains(existingId);
        }

        @Test
        void 쿠키가_없으면_빈_Optional을_반환한다() {
            MockHttpServletRequest request = new MockHttpServletRequest();

            Optional<String> result = visitorCookieResolver.findExisting(request);

            assertThat(result).isEmpty();
        }

        @Test
        void 유효하지_않은_쿠키값이면_빈_Optional을_반환한다() {
            MockHttpServletRequest request = new MockHttpServletRequest();
            request.setCookies(new Cookie("visitor_id", "not-a-uuid"));

            Optional<String> result = visitorCookieResolver.findExisting(request);

            assertThat(result).isEmpty();
        }
    }
}
