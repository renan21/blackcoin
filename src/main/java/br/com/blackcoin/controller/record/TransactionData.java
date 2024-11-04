package br.com.blackcoin.controller.record;

import java.math.BigDecimal;

public record TransactionData(String fromPublicKey, String toPublicKey, BigDecimal amount) {

}
