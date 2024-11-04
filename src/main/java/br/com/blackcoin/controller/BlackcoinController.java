package br.com.blackcoin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.blackcoin.controller.record.MiningData;
import br.com.blackcoin.controller.record.ResponseMessage;
import br.com.blackcoin.controller.record.TransactionData;
import br.com.blackcoin.domain.Blockchain;
import br.com.blackcoin.service.BlackcoinService;

@RestController
@RequestMapping("blackcoin")
public class BlackcoinController {
	
	private final BlackcoinService blackcoinService;
	
	BlackcoinController(BlackcoinService blackcoinService){
		this.blackcoinService = blackcoinService;
	}

	
	@PostMapping("transaction")
	public ResponseEntity<ResponseMessage> transaction(@RequestBody /* @Valid */ TransactionData transactionData) {
		return ResponseEntity.ok(blackcoinService.addTransaction(transactionData));
	}
	
	@PostMapping("mine-block")
	public ResponseEntity<ResponseMessage> mineBlock(@RequestBody /* @Valid */ MiningData miningData) {
		return ResponseEntity.ok(blackcoinService.mineBlock(miningData));
	}
	
	
	@GetMapping("balance/{publicKey}")
	public ResponseEntity<ResponseMessage> getBalance(@PathVariable String publicKey) {
		return ResponseEntity.ok(blackcoinService.getBalance(publicKey));
	}
	

	@GetMapping("blockchain")
	public ResponseEntity<Blockchain> getBlockchain() {
		return ResponseEntity.ok(blackcoinService.getBlockchain());
	}
		
}