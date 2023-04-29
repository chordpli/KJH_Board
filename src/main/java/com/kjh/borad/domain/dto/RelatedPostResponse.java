package com.kjh.borad.domain.dto;

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
}
