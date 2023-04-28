package com.kjh.borad.domain.entity;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Board {

	// ID, 제목, 본문, 생성날짜로 구성되며 제목과 본문은 각각 텍스트 입니다.

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long boardId;
	private String title;
	private String content;

	private LocalDateTime createdAt;
}
