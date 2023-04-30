package com.kjh.borad.domain.dto;

import org.hibernate.tool.schema.extract.spi.ExtractionContext;

import com.kjh.borad.domain.entity.Post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class RelatedPostResponse {
	private Long postId;
	private String title;
	private double relatedness;

	public static RelatedPostResponse fromEntity(Post post, double relatedness) {
		return RelatedPostResponse.builder()
			.postId(post.getPostId())
			.title(post.getTitle())
			.relatedness(relatedness)
			.build();
	}
}
