package com.kjh.borad.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kjh.borad.domain.entity.Board;

public interface BoardRepository extends JpaRepository<Board, Long> {
}
