package com.kjh.borad.domain.dto;

import com.kjh.borad.domain.entity.Post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class BoardPostResponse {

	private Long postId;
	private String title;

	public static BoardPostResponse fromEntity(Post board) {
		return BoardPostResponse.builder()
			.postId(board.getPostId())
			.title(board.getTitle())
			.build();
	}
}
