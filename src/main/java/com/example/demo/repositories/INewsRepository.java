package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.models.News;

@Repository
public interface INewsRepository extends JpaRepository<News, Long> {	
	
	public List<News> findByIsDisableOrderByCreatedDateDesc(boolean isDisable);
	public List<News> findTop3ByIsDisableOrderByCreatedDateDesc(boolean isDisable);
}
