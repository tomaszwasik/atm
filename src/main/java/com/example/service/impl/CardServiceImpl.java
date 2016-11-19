package com.example.service.impl;

import com.example.model.Card;
import com.example.repository.CardRepository;
import com.example.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Tomasz Wąsik, tomaszwasik@live.com
 */
@Service
public class CardServiceImpl implements CardService{

    @Autowired
    private CardRepository cardRepository;

    @Override
    public Card findCardById(Long id) {
        return cardRepository.findOne(id);
    }

    @Override
    public void updateCard(Card card){
        cardRepository.save(card);
    }
}
