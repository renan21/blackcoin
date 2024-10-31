package br.com.blackcoin.service;

import br.com.blackcoin.controller.record.TransactionData;
import br.com.blackcoin.domain.Blockchain;

@@Service
public class BlackcoinService {
	
	private final Blockchain blockchain;
	
	BlackcoinController(Blockchain blockchain){
		this.blockchain = blockchain;
	}

	public Object addTransaction(TransactionData transactionData) {
		// TODO Auto-generated method stub
		return null;
	}

}
