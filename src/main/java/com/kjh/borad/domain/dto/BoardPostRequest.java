package com.kjh.borad.domain.dto;

import java.time.LocalDateTime;

import com.kjh.borad.domain.entity.Board;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class BoardPostRequest {

	private String title;
	private String content;

	public static Board toEntity(BoardPostRequest request) {
		return Board.builder()
			.title(request.getTitle())
			.content(request.getContent())
			.createdAt(LocalDateTime.now())
			.build();
	}
}
