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
	private PostCacheService postCacheService;

	@Mock
	private PostRepository postRepository;

	List<Post> samplePosts;

	Set<String> excludedWords;
	Post post1, post2, post3, post4, post5, post6, post7, post8, post9, post10, post11, post12, post13, post14, post15, post16, post17, post18, post19;

	@BeforeEach
	public void setUp() {
		post1 = new Post(1L, "Title1", "안녕하세요. 글로벌 널리지 게시판 과제 제출합니다. 김준호입니다. 인사", LocalDateTime.now());
		post2 = new Post(2L, "Title2", "안녕하세요. 글로벌 널리지 게시판 과제 제출합니다. 김준호입니다.", LocalDateTime.now());
		post3 = new Post(3L, "Title3", "안녕하세요. 글로벌 널리지 지원자 김준호입니다. 인사", LocalDateTime.now());
		post4 = new Post(4L, "Title4", "안녕하세요. 글로벌 널리지 개발자 과제 제출합니다.", LocalDateTime.now());
		post5 = new Post(5L, "Title5", "안녕하세요. 글로벌 널리지 과제 제출 완료했습니다. 잘 부탁드립니다.", LocalDateTime.now());
		post6 = new Post(6L, "Title6", "안녕하세요. 글로벌 널리지 과제 제출합니다.", LocalDateTime.now());
		post7 = new Post(7L, "Title7", "안녕하세요. 사람인 글로벌 널리지 과제 제출합니다.", LocalDateTime.now());
		post8 = new Post(8L, "Title8", "안녕하세요. 게시판 과제 제출합니다.", LocalDateTime.now());
		post9 = new Post(9L, "Title9", "안녕하세요. 글로벌 널리지 첫번째 게시판 과제 입니다.", LocalDateTime.now());
		post10 = new Post(10L, "Title10", "안녕하세요. 좋은 아침입니다. 잘 부탁드립니다.", LocalDateTime.now());
		post11 = new Post(11L, "Title11", "글로벌 널리지 여러분 점심은 잘 드셨나요? 행복한 하루 되세요", LocalDateTime.now());
		post12 = new Post(12L, "Title12", "오늘도 고생하셨습니다. 좋은 밤 되세요!", LocalDateTime.now());
		post13 = new Post(13L, "Title13", "안녕하세요.오늘 과제 모두 잘 끝내셨나요? 즐거운 퇴근길 되시길 바랍니다.", LocalDateTime.now());
		post14 = new Post(14L, "Title14", "안녕하세요. 오늘 점심은 어떤 것을 드셨나요?", LocalDateTime.now());
		post15 = new Post(15L, "Title15", "굿모닝입니다! 다들 푹 주무셨나요? 오늘 과제 및 수업 내용 알려드립니다.", LocalDateTime.now());
		post16 = new Post(16L, "Title16", "안녕하세요. 글로벌 널리지 오늘 교육과 과제 내용은 다음과 같습니다. 다들 집중해주세요.", LocalDateTime.now());
		post17 = new Post(17L, "Title17", "글로벌 널리지 내일 교육 및 과제 내용은 다음과 같이 진행됩니다. 과제 내용 숙지 한 번 더 잘 부탁드립니다. 다들 잘 아시겠죠?",
			LocalDateTime.now());
		post18 = new Post(18L, "Title18", "글로벌 널리지 여러분 식사 맛있게 하셨나요? 남은 시간도 힘내주세요!", LocalDateTime.now());
		post19 = new Post(19L, "Title19", "고생하셨습니다. 과제 내용은 잊고 오늘은 일찍 퇴근하시죠!", LocalDateTime.now());

		samplePosts = Arrays.asList(post1, post2, post3, post4, post5, post6, post7, post8, post9, post10, post11,
			post12, post13, post14, post15, post16, post17, post18, post19);
		when(postRepository.findAll()).thenReturn(samplePosts);
		excludedWords = postCacheService.calculateExcludedWords();
	}

	@Test
	public void testCalculateExcludedWords() {
		assertEquals(Set.of("글로벌", "과제", "안녕하세요", "널리지"), excludedWords);
	}

	@Test
	public void testGetRelatedPosts() {
		Map<Post, Double> relatedPosts = postCacheService.getRelatedPosts(post1, excludedWords);
		Map<Post, Double> expectedRelatedPosts = Map.of(post2, 3.0, post3, 2.0, post8, 2.0);
		assertEquals(expectedRelatedPosts, relatedPosts);
	}
}