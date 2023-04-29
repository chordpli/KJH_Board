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
		Map<Post, Double> relatedPosts = getRelatedPosts(post, excludedWords);

		return ReadPostResponse.fromEntity(post, new ArrayList<>());
	}

	public Map<Post, Double> getRelatedPosts(Post post, Set<String> excludedWords) {
		List<Post> allPosts = postRepository.findAll();
		Map<Post, Double> relatedPosts = new HashMap<>();

		// 현재 게시물의 단어 갯수 체크
		Map<String, Integer> currentPostWordCnt = getWordCnt(post.getContent());

		// 현재 게시물에서 제외 단어를 제외시켜야함.
		currentPostWordCnt.keySet().removeAll(excludedWords);

		// 남은 단어를 기준으로 현재 게시물을 제외한 다른 게시물과 연관도를 계산하여 추가.
		// 연관도가 몇인지 계산해서 Map에 작성해야 해당 수치로 정렬할 수 있음.

		return relatedPosts;
	}

	public Map<String, Integer> getWordCnt(String content) {
		Map<String, Integer> wordsCnt = new HashMap<>();
		String[] words = content.toLowerCase().split("\\s+");

		for (String word : words) {
			wordsCnt.put(word, wordsCnt.getOrDefault(word, 0) + 1);
		}

		return wordsCnt;
	}

	public Set<String> calculateExcludedWords() {
		List<Post> posts = postRepository.findAll();
		int postCnt = posts.size();

		Map<String, Integer> wordCnt = countWordsInPosts(posts);

		// Count된 Words를 가중치 60%에 맞춰 60% 이상 단어들을 수집한다.
		return wordCnt.entrySet().stream()
			.peek(entry -> System.out.println(
				"단어: " + entry.getKey() + ", 횟수: " + entry.getValue() + ", 가중치: " + (entry.getValue()
					/ (double)postCnt)))
			.filter(entry -> entry.getValue() / (double)postCnt >= FREQUENCY_THRESHOLD)
			.map(Map.Entry::getKey)
			.collect(Collectors.toSet());
	}

	public Map<String, Integer> countWordsInPosts(List<Post> posts) {
		Map<String, Integer> wordCnt = new HashMap<>();
		for (Post post : posts) {
			Set<String> words = new HashSet<>(
				Arrays.asList(post.getContent().toLowerCase().replaceAll("[^a-zA-Z0-9가-힣\\s]", "").split("\\s+")));
			for (String word : words) {
				wordCnt.put(word, wordCnt.getOrDefault(word, 0) + 1);
			}
		}

		for (String word : wordCnt.keySet()) {
			System.out.println("단어 : " + word + "/ 값 : " + wordCnt.get(word));
		}

		return wordCnt;
	}

}