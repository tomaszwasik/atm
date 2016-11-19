package com.example.bus;

import lombok.Data;

/**
 * Created by Tomasz Wąsik, tomaszwasik@live.com
 */
@Data
public class SessionConfigurationModel {

    private String language;
    private long userAccountId;
    private long userCardId;

    private String messageForUser;
    private String amountInfoForUser;
}
