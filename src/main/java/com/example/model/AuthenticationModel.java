package com.example.model;

import lombok.Data;

/**
 * Created by Tomasz Wąsik, tomaszwasik@live.com
 */
@Data
public class AuthenticationModel {

    private String atmNumber;
    private CardModel card;
}
