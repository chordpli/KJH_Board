package com.kjh.borad.domain.dto;

import java.time.LocalDateTime;

import com.kjh.borad.domain.entity.Board;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class ReadPostResponse {

	private String title;
	private String content;
	private LocalDateTime createdAt;

	public static ReadPostResponse fromEntity(Board board) {
		return ReadPostResponse.builder()
			.title(board.getTitle())
			.content(board.getContent())
			.createdAt(board.getCreatedAt())
			.build();
	}
}
