package com.kjh.borad.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.kjh.borad.domain.entity.Post;
import com.kjh.borad.repository.PostRepository;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

	@InjectMocks
	private PostService postService;

	@Mock
	private PostRepository postRepository;

	List<Post> samplePosts;

	@BeforeEach
	public void setUp() {
		Post post1 = new Post(1L, "Title1", "안녕하세요. 글로벌 날리지 게시판 과제 제출합니다.", LocalDateTime.now());
		Post post2 = new Post(2L, "Title2", "안녕하세요. 글로벌 날리지 과제 제출합니다.", LocalDateTime.now());
		Post post3 = new Post(3L, "Title3", "안녕하세요. 글로벌 날리지 지원자 김준호입니다.", LocalDateTime.now());
		Post post4 = new Post(4L, "Title3", "안녕하세요. 글로벌 인재 과제 제출합니다.", LocalDateTime.now());
		Post post5 = new Post(5L, "Title3", "안녕하세요. 잘 부탁드립니다.", LocalDateTime.now());
		Post post6 = new Post(6L, "Title3", "안녕하세요. 글로벌 날리지 과제 제출합니다.", LocalDateTime.now());
		Post post7 = new Post(7L, "Title3", "안녕하십니까. 사람인 과제 제출합니다.", LocalDateTime.now());
		Post post8 = new Post(8L, "Title3", "안녕하십니까. 게시판 과제 제출합니다.", LocalDateTime.now());

		samplePosts = Arrays.asList(post1, post2, post3, post4, post5, post6, post7, post8);

	}

	@Test
	public void testCalculateExcludedWords() {
		when(postRepository.findAll()).thenReturn(samplePosts);

		Set<String> excludedWords = postService.calculateExcludedWords();
		assertEquals(Set.of("안녕하세요", "글로벌", "과제", "제출합니다"), excludedWords);
	}

}