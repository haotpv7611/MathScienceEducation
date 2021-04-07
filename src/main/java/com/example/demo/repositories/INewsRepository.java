package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.models.News;

@Repository
public interface INewsRepository extends JpaRepository<News, Long> {
	List<News> findByIsDisableFalseOrderByCreatedDateDesc();

	List<News> findTop3ByIsDisableFalseOrderByCreatedDateDesc();

	News findByIdAndIsDisableFalse(long id);
}
