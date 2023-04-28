package com.kjh.borad.controller;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kjh.borad.domain.dto.BoardPostRequest;
import com.kjh.borad.domain.dto.BoardPostResponse;
import com.kjh.borad.service.PostService;

@WebMvcTest(PostRestController.class)
class PostRestControllerTest {

	@Autowired
	MockMvc mockMvc;

	@Autowired
	ObjectMapper objectMapper;

	@MockBean
	PostService postService;

	@Test
	void post_success() throws Exception {
		BoardPostRequest request = BoardPostRequest.builder()
			.title("제목")
			.content("본문")
			.build();

		BoardPostResponse response = BoardPostResponse.builder()
			.postId(1L)
			.title("제목")
			.build();

		given(postService.posting(any())).willReturn(response);

		mockMvc.perform(post("/api/v1/board/post")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsBytes(request)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.postId").value(1))
			.andExpect(jsonPath("$.title").value("제목"))
			.andDo(print());
	}
}