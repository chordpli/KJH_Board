package com.kjh.borad.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.kjh.borad.domain.entity.Post;
import com.kjh.borad.repository.PostRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostCacheService {

	private final PostRepository postRepository;
	private final CacheManager cacheManager;
	private static final double FREQUENCY_THRESHOLD = 0.6;
	private static final double RELATED_POST_THRESHOLD = 0.4;
	private static final int MINIMUM_MATCHING_WORDS = 2;

	@Cacheable(value = "relatedPosts", key = "#post.postId")
	public Map<Post, Double> getRelatedPosts(Post post, Set<String> excludedWords) {
		return calculateRelatedPosts(post, excludedWords);
	}

	private Map<Post, Double> calculateRelatedPosts(Post post, Set<String> excludedWords) {
		Map<Post, Double> relatedPosts = new HashMap<>();

		List<Post> allPosts = postRepository.findAll();
		// 현재 게시물의 단어 갯수 체크
		Map<String, Integer> currentPostWordCnt = getWordCnt(post.getContent());

		// 현재 게시물에서 제외 단어를 제외시켜야함.
		currentPostWordCnt.keySet().removeAll(excludedWords);
		// 남은 단어를 기준으로 현재 게시물을 제외한 다른 게시물과 연관도를 계산하여 추가.
		// 연관도가 몇인지 계산해서 Map에 작성해야 해당 수치로 정렬할 수 있음.
		for (Post other : allPosts) {
			// 본인 게시물일 경우 Pass
			if (other.getPostId().equals(post.getPostId())) {
				continue;
			}

			// 게시물을 불러온 후, 단어를 수집하고, 제외해야 하는 단어를 삭제한다.
			Map<String, Integer> otherPostWordCnt = getWordCnt(other.getContent());
			otherPostWordCnt.keySet().removeAll(excludedWords);

			int matchCnt = 0;
			double relatedness = 0;

			for (String word : currentPostWordCnt.keySet()) {
				if (otherPostWordCnt.containsKey(word)) {
					// 현재 글의 단어가 otherPost에 포함되어 있다면, 일치 되는 단어 갯수 증가.
					matchCnt++;
					relatedness += currentPostWordCnt.get(word) * otherPostWordCnt.get(word);
				}
			}

			if (matchCnt >= MINIMUM_MATCHING_WORDS && relatedness >= RELATED_POST_THRESHOLD) {
				relatedPosts.put(other, relatedness);
			}
		}
		return relatedPosts;
	}

	public Map<String, Integer> getWordCnt(String content) {
		Map<String, Integer> wordsCnt = new HashMap<>();
		String[] words = content.toLowerCase().replaceAll("[^a-zA-Z0-9가-힣\\s]", "").split("\\s+");

		for (String word : words) {
			wordsCnt.put(word, wordsCnt.getOrDefault(word, 0) + 1);
		}

		return wordsCnt;
	}

	@Cacheable(value = "excludedWords")
	public Set<String> calculateExcludedWords() {
		List<Post> posts = postRepository.findAll();
		int postCnt = posts.size();

		Map<String, Integer> wordCnt = countWordsInPosts(posts);

		// Count된 Words를 가중치 60%에 맞춰 60% 이상 단어들을 수집한다.
		return wordCnt.entrySet().stream()
			.filter(entry -> entry.getValue() / (double)postCnt >= FREQUENCY_THRESHOLD)
			.map(Map.Entry::getKey)
			.collect(Collectors.toSet());
	}

	@Cacheable(value = "wordCounts")
	public Map<String, Integer> countWordsInPosts(List<Post> posts) {
		Map<String, Integer> wordCnt = new HashMap<>();
		for (Post post : posts) {
			Set<String> words = new HashSet<>(
				Arrays.asList(post.getContent().toLowerCase().replaceAll("[^a-zA-Z0-9가-힣\\s]", "").split("\\s+")));
			for (String word : words) {
				wordCnt.put(word, wordCnt.getOrDefault(word, 0) + 1);
			}
		}
		return wordCnt;
	}
}