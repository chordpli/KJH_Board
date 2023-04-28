package com.kjh.borad.domain.dto;

import java.time.LocalDateTime;

import com.kjh.borad.domain.entity.Post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class BoardPostRequest {

	private String title;
	private String content;

	public static Post toEntity(BoardPostRequest request) {
		return Post.builder()
			.title(request.getTitle())
			.content(request.getContent())
			.createdAt(LocalDateTime.now())
			.build();
	}
}
