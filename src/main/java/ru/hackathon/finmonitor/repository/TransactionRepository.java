package ru.hackathon.finmonitor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hackathon.finmonitor.model.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
