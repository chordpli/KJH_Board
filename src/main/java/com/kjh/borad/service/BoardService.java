package com.kjh.borad.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.kjh.borad.domain.dto.BoardPostRequest;
import com.kjh.borad.domain.dto.BoardPostResponse;
import com.kjh.borad.domain.dto.ListPostResponse;
import com.kjh.borad.domain.dto.ReadPostResponse;
import com.kjh.borad.domain.entity.Board;
import com.kjh.borad.repository.BoardRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
@RequiredArgsConstructor
public class BoardService {

	private final BoardRepository boardRepository;

	public BoardPostResponse posting(BoardPostRequest request) {
		Board board = boardRepository.save(BoardPostRequest.toEntity(request));
		log.info("board ={}", board.getBoardId());
		return BoardPostResponse.fromEntity(board);
	}

	public Page<ListPostResponse> getListPost(Pageable pageable) {
		return boardRepository.findAll(pageable).map(ListPostResponse::of);
	}

	public ReadPostResponse getReadPost(Long boardId) {
		Board board = boardRepository.findById(boardId).orElseThrow(() -> {
			throw new IllegalArgumentException();
		});

		return ReadPostResponse.fromEntity(board);
	}
}