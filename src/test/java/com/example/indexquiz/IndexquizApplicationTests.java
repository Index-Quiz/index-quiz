package com.example.indexquiz;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class IndexquizApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void failTest() {
		assertThat(1).isEqualTo(2);
	}

}
