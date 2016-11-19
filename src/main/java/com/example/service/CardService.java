package com.example.service;

import com.example.model.Card;

/**
 * Created by Tomasz Wąsik, tomaszwasik@live.com
 */
public interface CardService {

    Card findCardById(Long id);

    void updateCard(Card card);
}
