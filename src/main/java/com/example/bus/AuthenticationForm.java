package com.example.bus;

import lombok.Data;

/**
 * Created by Tomasz Wąsik, tomaszwasik@live.com
 */
@Data
public class AuthenticationForm {

    private String atmNumber;
    private CardAuthenticationForm card;
}
