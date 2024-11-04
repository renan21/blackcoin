package br.com.blackcoin.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import br.com.blackcoin.controller.record.MiningData;
import br.com.blackcoin.controller.record.ResponseMessage;
import br.com.blackcoin.controller.record.TransactionData;
import br.com.blackcoin.domain.Blockchain;
import br.com.blackcoin.domain.Key;
import br.com.blackcoin.domain.Transaction;

//TODO: Refactor all class..
@Service
public class BlackcoinService {
	
	private final Blockchain blackcoin = new Blockchain();
	
	
	public ResponseMessage addTransaction(TransactionData transactionData) {
		try {
			Key key = new Key(transactionData.fromPublicKey());
			Transaction transaction = new Transaction(key.getPublicKey(), transactionData.toPublicKey(), transactionData.amount());
			transaction.signTransaction(key);
			blackcoin.addTransaction(transaction);
		} catch (Exception e) {
			return new ResponseMessage("Erro ao adicionar transação: " + e.getMessage());
		}
		return new ResponseMessage("Transação adicionada com sucesso!");
	}

	public ResponseMessage mineBlock(MiningData miningData) {
		System.out.println("Staring the miner...");	
		try {
			Key key = new Key(miningData.publicKey());
			blackcoin.minePendingTransactions(key.getPublicKey());
		} catch (Exception e) {
			return new ResponseMessage("Erro ao realizar mineração: " + e.getMessage());
		}
		return new ResponseMessage("Mineração realizada com sucesso!");		
	}

	public ResponseMessage getBalance(String publicKey) {
		BigDecimal balance;
		try {
			Key key = new Key(publicKey);
			balance = blackcoin.getBalanceOfKey(key.getPublicKey());
		} catch (Exception e) {
			return new ResponseMessage("Erro ao realizar mineração: " + e.getMessage());
		}
		return new ResponseMessage("Seu saldo é: " + balance);
	}

	public Blockchain getBlockchain() {
		return blackcoin;
	}

}