package com.example.demo;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class StreamExamples {

    static class Transaction {
        private String accountId;
        private BigDecimal amount;
        private String type;

        public Transaction(String accountId, BigDecimal amount, String type) {
            this.accountId = accountId;
            this.amount = amount;
            this.type = type;
        }

        public String getAccountId() { return accountId; }
        public BigDecimal getAmount() { return amount; }
        public String getType() { return type; }
    }

    // =========================
    // FILTER
    // =========================

    /**
     * Filtra solo transacciones DEBIT
     */
    public static List<Transaction> filterOnlyDebits(List<Transaction> list) {
        return list.stream()
                .filter(t -> "DEBIT".equalsIgnoreCase(t.getType()))
                .collect(Collectors.toList());
    }

    /**
     * Filtra por importe mayor que X
     */
    public static List<Transaction> filterByAmountGreaterThan(
            List<Transaction> list, BigDecimal minAmount) {

        return list.stream()
                .filter(t -> t.getAmount().compareTo(minAmount) > 0)
                .collect(Collectors.toList());
    }

    // =========================
    // MAP
    // =========================

    /**
     * Convierte Transaction -> accountId (String)
     */
    public static List<String> mapToAccountIds(List<Transaction> list) {
        return list.stream()
                .map(Transaction::getAccountId) // extrae solo el campo
                .collect(Collectors.toList());
    }

    /**
     * Convierte Transaction -> amount (BigDecimal)
     */
    public static List<BigDecimal> mapToAmounts(List<Transaction> list) {
        return list.stream()
                .map(Transaction::getAmount)
                .collect(Collectors.toList());
    }

    /**
     * Aplica transformación: aplicar comisión del 2%
     */
    public static List<BigDecimal> mapApplyCommission(List<Transaction> list) {
        return list.stream()
                .map(t -> t.getAmount().multiply(new BigDecimal("0.98")))
                .collect(Collectors.toList());
    }

    // =========================
    // COLLECT - Casos más importantes
    // =========================

    /**
     * Agrupar por accountId
     * Devuelve: Map<accountId, List<Transaction>>
     */
    public static Map<String, List<Transaction>> collectGroupByAccount(
            List<Transaction> list) {

        return list.stream()
                .collect(Collectors.groupingBy(Transaction::getAccountId));
    }

    /**
     * Agrupar por tipo (DEBIT/CREDIT)
     */
    public static Map<String, List<Transaction>> collectGroupByType(
            List<Transaction> list) {

        return list.stream()
                .collect(Collectors.groupingBy(Transaction::getType));
    }

    /**
     * Agrupar por cuenta y sumar importes
     * Devuelve: Map<accountId, totalAmount>
     */
    public static Map<String, BigDecimal> collectSumAmountByAccount(
            List<Transaction> list) {

        return list.stream()
                .collect(Collectors.groupingBy(
                        Transaction::getAccountId,
                        Collectors.reducing(
                                BigDecimal.ZERO,        // valor inicial
                                Transaction::getAmount, // qué campo sumar
                                BigDecimal::add         // cómo sumar
                        )
                ));
    }

    /**
     * Contar transacciones por cuenta
     * Devuelve: Map<accountId, count>
     */
    public static Map<String, Long> collectCountByAccount(
            List<Transaction> list) {

        return list.stream()
                .collect(Collectors.groupingBy(
                        Transaction::getAccountId,
                        Collectors.counting()
                ));
    }

    /**
     * Convertir a Set (elimina duplicados)
     */
    public static Set<String> collectUniqueAccountIds(List<Transaction> list) {
        return list.stream()
                .map(Transaction::getAccountId)
                .collect(Collectors.toSet());
    }

    // =========================
    // REDUCE
    // =========================

    /**
     * Sumar todos los importes (reduce clásico)
     */
    public static BigDecimal reduceTotalAmount(List<Transaction> list) {
        return list.stream()
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Encontrar el mayor importe
     */
    public static Optional<BigDecimal> reduceMaxAmount(List<Transaction> list) {
        return list.stream()
                .map(Transaction::getAmount)
                .reduce(BigDecimal::max);
    }

    /**
     * Concatenar todos los accountId en un String
     */
    public static String reduceConcatAccountIds(List<Transaction> list) {
        return list.stream()
                .map(Transaction::getAccountId)
                .distinct()
                .reduce("", (a, b) -> a + "," + b);
    }
    
    // =========================
    // Ejemplos combinados
    /**
     * Filtrar DEBIT + agrupar por cuenta + sumar importes
     * (MUY típico en banca)
     */
    public static Map<String, BigDecimal> debitTotalByAccount(
            List<Transaction> list) {

        return list.stream()
                .filter(t -> "DEBIT".equalsIgnoreCase(t.getType()))
                .collect(Collectors.groupingBy(
                        Transaction::getAccountId,
                        Collectors.reducing(
                                BigDecimal.ZERO,
                                Transaction::getAmount,
                                BigDecimal::add
                        )
                ));
    }

    /**
     * Top N transacciones por importe
     */
    public static List<Transaction> topNByAmount(
            List<Transaction> list, int n) {

        return list.stream()
                .sorted(Comparator.comparing(Transaction::getAmount).reversed())
                .limit(n)
                .collect(Collectors.toList());
    }

    /*
    ¿filter?
    Elimina elementos según un predicado

    ¿map?
    Transforma cada elemento en otro

    ¿collect?
    Convierte el stream en una estructura final (List, Set, Map, agregados…)

    ¿reduce?
    Combina todos los elementos en uno solo
    */
}