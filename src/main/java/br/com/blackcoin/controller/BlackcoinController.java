package br.com.blackcoin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.blackcoin.controller.record.TransactionData;
import br.com.blackcoin.service.BlackcoinService;

@RestController
@RequestMapping("blackcoin")
public class BlackcoinController {
	
	private final BlackcoinService blackcoinService;
	
	BlackcoinController(BlackcoinService blackcoinService){
		this.blackcoinService = blackcoinService;
	}

	
	@PostMapping("transaction")
	public ResponseEntity transaction(@RequestBody @Valid TransactionData transactionData) {
		return ResponseEntity.ok(blackcoinService.addTransaction(transactionData));
	}
	
}