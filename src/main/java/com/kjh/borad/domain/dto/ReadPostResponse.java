package com.kjh.borad.domain.dto;

import java.time.LocalDateTime;
import java.util.List;

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
public class ReadPostResponse {

	private String title;
	private String content;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss", timezone = "Asia/Seoul")
	private LocalDateTime createdAt;
	private List<RelatedPostResponse> relatedPosts;

	public static ReadPostResponse fromEntity(Post board, List<RelatedPostResponse> relatedPosts) {
		return ReadPostResponse.builder()
			.title(board.getTitle())
			.content(board.getContent())
			.createdAt(board.getCreatedAt())
			.relatedPosts(relatedPosts)
			.build();
	}
}
