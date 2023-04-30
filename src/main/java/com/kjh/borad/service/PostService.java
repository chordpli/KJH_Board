package com.kjh.borad.service;

import java.util.Comparator;
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
import com.kjh.borad.domain.dto.RelatedPostResponse;
import com.kjh.borad.domain.entity.Post;
import com.kjh.borad.repository.PostRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostService {

	private final PostRepository postRepository;
	private final PostCacheService postCacheService;

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
		Set<String> excludedWords = postCacheService.calculateExcludedWords();
		// 연관 게시물 수집
		Map<Post, Double> relatedPosts = postCacheService.getRelatedPosts(post, excludedWords);

		List<RelatedPostResponse> relatedPostResponses = relatedPosts.entrySet().stream()
			.sorted(Map.Entry.<Post, Double>comparingByValue(Comparator.reverseOrder())
				.thenComparing(Map.Entry.comparingByKey(Comparator.comparing(Post::getPostId))))
			.map(entry -> RelatedPostResponse.fromEntity(entry.getKey(), entry.getValue()))
			.collect(Collectors.toList());

		return ReadPostResponse.fromEntity(post, relatedPostResponses);
	}
}