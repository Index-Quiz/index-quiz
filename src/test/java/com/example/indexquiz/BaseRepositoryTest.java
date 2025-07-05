package com.example.indexquiz;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@Import({
        //generator 등등
})
@DataJpaTest
public abstract class BaseRepositoryTest {

}

