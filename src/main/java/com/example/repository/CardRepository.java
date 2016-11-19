package com.example.repository;

import com.example.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Tomasz Wąsik, tomaszwasik@live.com
 */
public interface CardRepository extends JpaRepository<Card, Long> {
}
