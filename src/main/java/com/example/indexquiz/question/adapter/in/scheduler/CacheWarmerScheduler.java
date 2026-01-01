package com.example.indexquiz.question.adapter.in.scheduler;

import com.example.indexquiz.question.application.port.in.RefreshDifficultQuestionCacheUseCase;
import com.example.indexquiz.question.application.port.in.dto.GetQuestionResponses;
import com.example.indexquiz.question.application.port.in.mapper.QuestionWithOptionsMapper;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CacheWarmerScheduler {

    private static final String CACHE_NAME = "questionCache";
    private static final String DIFFICULT_QUESTION_KEY = "question:BEST_DIFFICULT";

    private final RefreshDifficultQuestionCacheUseCase refreshDifficultQuestionCacheUseCase;
    private final CacheManager cacheManager;

    @Scheduled(cron = "0 55 23 * * *", zone = "Asia/Seoul")
    public void warmUpCache() {
        try {
            GetQuestionResponses difficultQuestion = refreshDifficultQuestionCacheUseCase.getDifficultQuestion();
            Objects.requireNonNull(cacheManager.getCache(CACHE_NAME))
                    .put(DIFFICULT_QUESTION_KEY, difficultQuestion);
            log.info("오답률 Best 문제집 캐시 프리워밍 완료");
        }catch (Exception e) {
            log.error("오답률 Best 문제집 캐시 프리워밍 실패 : {}, {} ", e.getMessage(), e.getStackTrace());
            Cache cache = Objects.requireNonNull(cacheManager.getCache(CACHE_NAME));
            GetQuestionResponses existsCacheContent = cache.get(DIFFICULT_QUESTION_KEY, GetQuestionResponses.class);
            cache.put(DIFFICULT_QUESTION_KEY, existsCacheContent);
            log.info("기존 캐시 내역 TTL 연장 완료");
        }
    }
}
