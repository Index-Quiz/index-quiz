package com.example.indexquiz;

import com.example.indexquiz.common.config.JpaAuditingConfig;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@Import({
        JpaAuditingConfig.class,
        //generator 등등
})
@DataJpaTest
public abstract class BaseRepositoryTest {

}

