package com.example.demo;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

class DemoApplicationTests {
	private static final Logger logger = LoggerFactory.getLogger(DemoApplicationTests.class);

	@Test
	void contextLoads() {
	}

	@Test
	void testCalculateTotalAmountsByDebitType() {
		// Arrange - Preparar datos
		Transaction transaction = new Transaction();
		
		List<Transaction> transactionList = Arrays.asList(
			createTransaction("ACC001", new BigDecimal("100.00"), "DEBIT"),
			createTransaction("ACC001", new BigDecimal("50.00"), "DEBIT"),
			createTransaction("ACC002", new BigDecimal("200.00"), "DEBIT"),
			createTransaction("ACC001", new BigDecimal("75.00"), "CREDIT"),  // No debe contar
			createTransaction("ACC002", new BigDecimal("150.00"), "CREDIT")   // No debe contar
		);
		
		// Act - Ejecutar el método
		Map<String, BigDecimal> resultado = transaction.calculateTotalAmountsByDebitType(transactionList);
		
		// Assert - Validar resultados
		assertEquals(2, resultado.size(), "Debe haber 2 cuentas con transacciones DEBIT");
		assertEquals(new BigDecimal("150.00"), resultado.get("ACC001"), "ACC001 debe tener 150.00");
		assertEquals(new BigDecimal("200.00"), resultado.get("ACC002"), "ACC002 debe tener 200.00");
	}

	@Test
	void testCalculateTotalAmountsByDebitType_SinDebits() {
		// Arrange - Preparar datos sin DEBITS
		Transaction transaction = new Transaction();
		
		List<Transaction> transactionList = Arrays.asList(
			createTransaction("ACC001", new BigDecimal("100.00"), "CREDIT"),
			createTransaction("ACC002", new BigDecimal("200.00"), "CREDIT")
		);
		
		// Act - Ejecutar el método
		Map<String, BigDecimal> resultado = transaction.calculateTotalAmountsByDebitType(transactionList);
		
		// Assert - Validar que el resultado esté vacío
		assertEquals(0, resultado.size(), "No debe haber DEBITS en el resultado");
		assertTrue(resultado.isEmpty(), "El mapa debe estar vacío");
	}

	@Test
	void testCalculateTotalAmountsByDebitType_ListaVacia() {
		// Arrange - Lista vacía
		Transaction transaction = new Transaction();
		List<Transaction> transactionList = Arrays.asList();
		
		// Act - Ejecutar el método
		Map<String, BigDecimal> resultado = transaction.calculateTotalAmountsByDebitType(transactionList);
		
		// Assert - Validar que el resultado esté vacío
		assertEquals(0, resultado.size(), "No debe haber transacciones");
	}

	@Test
	void testCalculateTotalAmountsByDebitType_CaseSensitivity() {
		// Arrange - Probar diferentes variaciones de "DEBIT"
		Transaction transaction = new Transaction();
		
		List<Transaction> transactionList = Arrays.asList(
			createTransaction("ACC001", new BigDecimal("100.00"), "DEBIT"),
			createTransaction("ACC001", new BigDecimal("50.00"), "debit"),
			createTransaction("ACC001", new BigDecimal("25.00"), "Debit")
		);
		
		// Act - Ejecutar el método
		Map<String, BigDecimal> resultado = transaction.calculateTotalAmountsByDebitType(transactionList);
		
		// Assert - Validar que agrupe todas las variaciones (case-insensitive)
		assertEquals(1, resultado.size(), "Debe agrupar todas las variaciones de DEBIT");
		assertEquals(new BigDecimal("175.00"), resultado.get("ACC001"), 
			"Debe sumar todas las transacciones DEBIT independientemente de mayúsculas/minúsculas");
	}

	// Método auxiliar para crear transacciones de prueba
	private Transaction createTransaction(String accountId, BigDecimal amount, String type) {
		return new Transaction(accountId, amount, LocalDateTime.now(), type);
	}
}