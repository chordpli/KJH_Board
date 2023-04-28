package com.kjh.borad.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kjh.borad.domain.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
}
