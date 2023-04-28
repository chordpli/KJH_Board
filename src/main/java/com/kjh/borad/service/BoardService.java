package com.kjh.borad.service;

import org.springframework.stereotype.Service;

import com.kjh.borad.domain.dto.BoardPostRequest;
import com.kjh.borad.domain.dto.BoardPostResponse;
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
}