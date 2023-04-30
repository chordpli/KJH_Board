package com.kjh.borad.domain.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kjh.borad.domain.entity.Post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class ListPostResponse {
	private String title;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss", timezone = "Asia/Seoul")
	private LocalDateTime createdAt;

	public static ListPostResponse of(Post board) {
		return ListPostResponse.builder()
			.title(board.getTitle())
			.createdAt(board.getCreatedAt())
			.build();
	}
}
