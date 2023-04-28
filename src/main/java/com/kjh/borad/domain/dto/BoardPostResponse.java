package com.kjh.borad.domain.dto;

import com.kjh.borad.domain.entity.Board;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class BoardPostResponse {

	private Long boardId;
	private String title;

	public static BoardPostResponse fromEntity(Board board) {
		return BoardPostResponse.builder()
			.boardId(board.getBoardId())
			.title(board.getTitle())
			.build();
	}
}
