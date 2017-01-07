package com.example.repository;

import com.example.model.History;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Tomasz Wąsik, tomaszwasik@live.com
 */
public interface HistoryRepository extends JpaRepository<History, Long> {
}
