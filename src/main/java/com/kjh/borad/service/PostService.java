package com.kjh.borad.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.kjh.borad.domain.dto.BoardPostRequest;
import com.kjh.borad.domain.dto.BoardPostResponse;
import com.kjh.borad.domain.dto.ListPostResponse;
import com.kjh.borad.domain.dto.ReadPostResponse;
import com.kjh.borad.domain.entity.Post;
import com.kjh.borad.repository.PostRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostService {

	private final PostRepository postRepository;

	private static final double FREQUENCY_THRESHOLD = 0.6;
	private static final double RELATED_POST_THRESHOLD = 0.4;
	private static final int MINIMUM_MATCHING_WORDS = 2;

	public BoardPostResponse posting(BoardPostRequest request) {
		Post post = postRepository.save(BoardPostRequest.toEntity(request));
		log.info("board ={}", post.getPostId());
		return BoardPostResponse.fromEntity(post);
	}

	public Page<ListPostResponse> getListPost(Pageable pageable) {
		return postRepository.findAll(pageable).map(ListPostResponse::of);
	}

	public ReadPostResponse getReadPost(Long postId) {
		Post post = postRepository.findById(postId).orElseThrow(() -> {
			throw new RuntimeException();
		});

		// 모든 게시물 내에서 제외해야하는 단어 수집.
		Set<String> excludedWords = calculateExcludedWords();
		// 연관 게시물 수집

		return ReadPostResponse.fromEntity(post, new ArrayList<>());
	}

	private Set<String> calculateExcludedWords() {
		List<Post> posts = postRepository.findAll();
		int postCnt = posts.size();

		Map<String, Integer> wordCnt = countWordsInPosts(posts);

		// Count된 Words를 가중치 60%에 맞춰 60% 이상 단어들을 수집한다.
		return wordCnt.entrySet().stream()
			.filter(entry -> entry.getValue() / (double)postCnt >= FREQUENCY_THRESHOLD)
			.map(Map.Entry::getKey)
			.collect(Collectors.toSet());
	}

	private Map<String, Integer> countWordsInPosts(List<Post> posts) {
		Map<String, Integer> wordCnt = new HashMap<>();
		for (Post post : posts) {
			Set<String> words = new HashSet<>(Arrays.asList(post.getContent().toLowerCase().split("\\s")));
			for (String word : words) {
				wordCnt.put(word, wordCnt.getOrDefault(word, 0) + 1);
			}
		}
		return wordCnt;
	}

}