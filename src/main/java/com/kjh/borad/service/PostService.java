package com.kjh.borad.service;

import java.util.ArrayList;

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
		return ReadPostResponse.fromEntity(post, new ArrayList<>());
	}

}