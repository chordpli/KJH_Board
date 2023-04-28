package com.kjh.borad.service;

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

	public BoardPostResponse posting(BoardPostRequest request) {
		Post post = postRepository.save(BoardPostRequest.toEntity(request));
		log.info("board ={}", post.getPostId());
		return BoardPostResponse.fromEntity(post);
	}

	public Page<ListPostResponse> getListPost(Pageable pageable) {
		return postRepository.findAll(pageable).map(ListPostResponse::of);
	}

	public ReadPostResponse getReadPost(Long boardId) {
		Post post = postRepository.findById(boardId).orElseThrow(() -> {
			throw new IllegalArgumentException();
		});

		return ReadPostResponse.fromEntity(post);
	}
}