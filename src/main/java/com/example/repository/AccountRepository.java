package com.example.repository;

import com.example.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Tomasz Wąsik, tomaszwasik@live.com
 */
public interface AccountRepository extends JpaRepository<Account, Long> {
}
