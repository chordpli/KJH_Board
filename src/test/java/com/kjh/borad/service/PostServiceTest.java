package com.kjh.borad.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
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

	Set<String> excludedWords;
	Post post1;
	Post post2;
	Post post3;
	Post post4;
	Post post5;
	Post post6;
	Post post7;
	Post post8;

	@BeforeEach
	public void setUp() {
		post1 = new Post(1L, "Title1", "안녕하세요. 글로벌 날리지 게시판 과제 제출합니다. 김준호입니다. 인사", LocalDateTime.now());
		post2 = new Post(2L, "Title2", "안녕하세요. 글로벌 날리지 과제 제출합니다. 김준호입니다.", LocalDateTime.now());
		post3 = new Post(3L, "Title3", "안녕하세요. 글로벌 날리지 지원자 김준호입니다. 인사", LocalDateTime.now());
		post4 = new Post(4L, "Title4", "안녕하세요. 글로벌 인재 과제 제출합니다.", LocalDateTime.now());
		post5 = new Post(5L, "Title5", "안녕하세요. 잘 부탁드립니다.", LocalDateTime.now());
		post6 = new Post(6L, "Title6", "안녕하세요. 글로벌 날리지 과제 제출합니다.", LocalDateTime.now());
		post7 = new Post(7L, "Title7", "안녕하십니까. 사람인 과제 제출합니다.", LocalDateTime.now());
		post8 = new Post(8L, "Title8", "안녕하십니까. 게시판 과제 제출합니다.", LocalDateTime.now());

		samplePosts = Arrays.asList(post1, post2, post3, post4, post5, post6, post7, post8);
		when(postRepository.findAll()).thenReturn(samplePosts);
		excludedWords = postService.calculateExcludedWords();
	}

	@Test
	public void testCalculateExcludedWords() {
		assertEquals(Set.of("안녕하세요", "글로벌", "과제", "제출합니다"), excludedWords);
	}

	@Test
	public void testGetRelatedPosts() {
		Map<Post, Double> relatedPosts = postService.getRelatedPosts(post1, excludedWords);
		Map<Post, Double> expectedRelatedPosts = Map.of(post2, 2.0, post3, 3.0);
		assertEquals(expectedRelatedPosts, relatedPosts);
	}

}