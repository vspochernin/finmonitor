package ru.hackathon.finmonitor.repository;

import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;
import ru.hackathon.finmonitor.controller.dto.TransactionFilterDto;
import ru.hackathon.finmonitor.model.Transaction;

import java.util.ArrayList;
import java.util.List;

public class TransactionSpecification {


    public static Specification<Transaction> withFilters(TransactionFilterDto filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.getSenderBankId() != null) {
                predicates.add(cb.equal(root.get("senderBank").get("id"), filter.getSenderBankId()));
            }

            if (filter.getReceiverBankId() != null) {
                predicates.add(cb.equal(root.get("receiverBank").get("id"), filter.getReceiverBankId()));
            }

            if (filter.getStartDate() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("operationDateTime"), filter.getStartDate().atStartOfDay()));
            }

            if (filter.getEndDate() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("operationDateTime"), filter.getEndDate().atTime(23, 59, 59)));
            }

            if (filter.getStatus() != null) {
                predicates.add(cb.equal(root.get("status"), filter.getStatus()));
            }

            if (filter.getReceiverInn() != null) {
                predicates.add(cb.equal(root.get("receiverInn"), filter.getReceiverInn()));
            }

            if (filter.getAmountFrom() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("amount"), filter.getAmountFrom()));
            }

            if (filter.getAmountTo() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("amount"), filter.getAmountTo()));
            }

            if (filter.getTransactionType() != null) {
                predicates.add(cb.equal(root.get("transactionType"), filter.getTransactionType()));
            }

            if (filter.getCategoryId() != null) {
                predicates.add(cb.equal(root.get("category").get("id"), filter.getCategoryId()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}

