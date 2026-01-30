package com.example.demo;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class TechnicalTestSolutions {

    /**
     * Contexto: Clase Transaction
     * Se asume la estructura dada en el ejercicio.
     */
    public static class Transaction {
        private String accountId;
        private BigDecimal amount;
        private LocalDateTime timestamp;
        private String type; // DEBIT / CREDIT

        // Constructor para facilitar pruebas
        public Transaction(String accountId, BigDecimal amount, String type) {
            this.accountId = accountId;
            this.amount = amount;
            this.timestamp = LocalDateTime.now();
            this.type = type;
        }

        public String getAccountId() {
            return accountId;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        public LocalDateTime getTimestamp() {
            return timestamp;
        }

        public String getType() {
            return type;
        }

        @Override
        public String toString() {
            return "Transaction{id='" + accountId + "', amount=" + amount + ", type='" + type + "'}";
        }
    }

    // ==========================================
    // EJERCICIO 1 — Streams básicos
    // ==========================================

    /**
     * 1.1 Implementa un método que devuelva solo las transacciones de tipo CREDIT.
     */
    public List<Transaction> getOnlyCredits(List<Transaction> list) {
        return list.stream()
                .filter(t -> "CREDIT".equalsIgnoreCase(t.getType()))
                .collect(Collectors.toList());
    }

    /**
     * 1.2 Devuelve una lista con los accountId únicos.
     */
    public Set<String> getUniqueAccountIds(List<Transaction> list) {
        return list.stream()
                .map(Transaction::getAccountId)
                .collect(Collectors.toSet());
    }

    /**
     * 1.3 Devuelve una lista de importes (BigDecimal) solo de transacciones DEBIT
     * mayores de 100.
     */
    public List<BigDecimal> getBigDebits(List<Transaction> list) {
        return list.stream()
                .filter(t -> "DEBIT".equalsIgnoreCase(t.getType()))
                .filter(t -> t.getAmount().compareTo(new BigDecimal("100")) > 0)
                .map(Transaction::getAmount)
                .collect(Collectors.toList());
    }

    // ==========================================
    // EJERCICIO 2 — Agrupaciones y collect
    // ==========================================

    /**
     * 2.1 Agrupa las transacciones por accountId.
     */
    public Map<String, List<Transaction>> groupByAccount(List<Transaction> list) {
        return list.stream()
                .collect(Collectors.groupingBy(Transaction::getAccountId));
    }

    /**
     * 2.2 Devuelve el total de importe por cuenta.
     */
    public Map<String, BigDecimal> totalAmountByAccount(List<Transaction> list) {
        return list.stream()
                .collect(Collectors.groupingBy(
                        Transaction::getAccountId,
                        Collectors.reducing(
                                BigDecimal.ZERO,
                                Transaction::getAmount,
                                BigDecimal::add)));
    }

    /**
     * 2.3 Devuelve el número de transacciones por tipo (DEBIT, CREDIT).
     */
    public Map<String, Long> countByType(List<Transaction> list) {
        return list.stream()
                .collect(Collectors.groupingBy(
                        Transaction::getType,
                        Collectors.counting()));
    }

    // ==========================================
    // EJERCICIO 3 — Reduce
    // ==========================================

    /**
     * 3.1 Calcula el importe total de TODAS las transacciones usando reduce.
     */
    public BigDecimal calculateGlobalTotal(List<Transaction> list) {
        return list.stream()
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * 3.2 Devuelve el importe máximo.
     */
    public Optional<BigDecimal> getMaxAmount(List<Transaction> list) {
        return list.stream()
                .map(Transaction::getAmount)
                .reduce(BigDecimal::max);
    }

    // ==========================================
    // EJERCICIO 4 — Combinados (nivel entrevista)
    // ==========================================

    /**
     * 4.1 Para transacciones DEBIT, devuelve el total por cuenta.
     */
    public Map<String, BigDecimal> debitTotalByAccount(List<Transaction> list) {
        return list.stream()
                .filter(t -> "DEBIT".equalsIgnoreCase(t.getType()))
                .collect(Collectors.groupingBy(
                        Transaction::getAccountId,
                        Collectors.reducing(
                                BigDecimal.ZERO,
                                Transaction::getAmount,
                                BigDecimal::add)));
    }

    /**
     * 4.2 Devuelve las 5 transacciones con mayor importe.
     */
    public List<Transaction> top5ByAmount(List<Transaction> list) {
        return list.stream()
                .sorted(Comparator.comparing(Transaction::getAmount).reversed())
                .limit(5)
                .collect(Collectors.toList());
    }
}
