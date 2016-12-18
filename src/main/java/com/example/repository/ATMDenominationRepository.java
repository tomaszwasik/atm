package com.example.repository;

import com.example.model.ATMDenomination;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Tomasz Wąsik, tomaszwasik@live.com
 */
public interface ATMDenominationRepository extends JpaRepository<ATMDenomination, Long> {

    List<ATMDenomination> findByAccountId(Long accountId);
}
