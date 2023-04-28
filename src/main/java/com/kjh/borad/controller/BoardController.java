package com.kjh.borad.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kjh.borad.domain.dto.BoardPostRequest;
import com.kjh.borad.domain.dto.BoardPostResponse;
import com.kjh.borad.domain.dto.ListPostResponse;
import com.kjh.borad.domain.dto.ReadPostResponse;
import com.kjh.borad.service.BoardService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/board")
@RequiredArgsConstructor
@Slf4j
public class BoardController {

	private final BoardService boardService;

	// 게시글 목록(게시글 제목과 날짜 정보)
	@GetMapping
	public ResponseEntity<Page<ListPostResponse>> getListPost(Pageable pageable) {
		return ResponseEntity.ok().body(boardService.getListPost(pageable));

	}

	// 게시글 불러오기 (연관 게시글을 가지고 있음, 연관 게시글이 보여지는 순서는 연관도가 높은 순서)
	@GetMapping("/{boardId}")
	public ResponseEntity<ReadPostResponse> getReadPost(@PathVariable Long boardId){
		return ResponseEntity.ok().body(boardService.getReadPost(boardId));
	}

	// 게시글 등록(제목과 내용만 작성)
	@PostMapping("/post")
	public ResponseEntity<BoardPostResponse> posting(@RequestBody final BoardPostRequest request){
		log.info("request.title = {}", request.getTitle());
		log.info("request.content = {}", request.getContent());
		return ResponseEntity.ok().body(boardService.posting(request));
	}
}
