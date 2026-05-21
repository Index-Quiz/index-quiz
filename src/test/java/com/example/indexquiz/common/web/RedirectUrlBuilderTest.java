package com.example.indexquiz.common.web;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class RedirectUrlBuilderTest {

    @Nested
    class Build {

        @Test
        void 파라미터_없이_리다이렉트_URL을_생성한다() {
            // given
            RedirectUrlBuilder builder = new RedirectUrlBuilder("/learn");

            // when
            String actual = builder.build();

            // then
            assertThat(actual).isEqualTo("redirect:/learn");
        }

        @Test
        void 단일_파라미터로_리다이렉트_URL을_생성한다() {
            // given
            RedirectUrlBuilder builder = new RedirectUrlBuilder("/learn")
                    .addParam("set", Optional.of("A"));

            // when
            String actual = builder.build();

            // then
            assertThat(actual).isEqualTo("redirect:/learn?set=A");
        }

        @Test
        void 복수_파라미터로_리다이렉트_URL을_생성한다() {
            // given
            RedirectUrlBuilder builder = new RedirectUrlBuilder("/learn")
                    .addParam("set", Optional.of("A"))
                    .addParam("id", Optional.of("1"));

            // when
            String actual = builder.build();

            // then
            assertThat(actual).isEqualTo("redirect:/learn?set=A&id=1");
        }

        @Test
        void 빈_Optional_파라미터는_무시한다() {
            // given
            RedirectUrlBuilder builder = new RedirectUrlBuilder("/learn")
                    .addParam("set", Optional.of("A"))
                    .addParam("id", Optional.empty());

            // when
            String actual = builder.build();

            // then
            assertThat(actual).isEqualTo("redirect:/learn?set=A");
        }

        @Test
        void 모든_파라미터가_빈_경우_쿼리스트링_없이_생성한다() {
            // given
            RedirectUrlBuilder builder = new RedirectUrlBuilder("/learn")
                    .addParam("set", Optional.empty())
                    .addParam("id", Optional.empty());

            // when
            String actual = builder.build();

            // then
            assertThat(actual).isEqualTo("redirect:/learn");
        }
    }
}
