package br.com.blackcoin.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import br.com.blackcoin.adapter.LocalDateTimeAdapter;

public class Main {

	public static void main(String[] args) throws Exception {		

		Key key = new Key("MIGNAgEAMBAGByqGSM49AgEGBSuBBAAKBHYwdAIBAQQgqU0vN+VdtCF/6GqmBkQVxw4VSq+9UOwNj40cGVBcJuqgBwYFK4EEAAqhRANCAAS+AbOLawBuDcsxQDHwbfFtPf8Q8/XPu1jjmivqedR8nyhVWOT5/mcRM174Ojik9srUUGNPNzyxXITmg+orLVPI");
	
		Blockchain blackcoin = new Blockchain();
		
		
		// ---		
		Transaction tx1 = new Transaction(key.getPublicKey(), "public key someone else", new BigDecimal(10));
		tx1.signTransaction(key);
		blackcoin.addTransaction(tx1);
		
		System.out.println("Staring the miner...");		
		blackcoin.minePendingTransactions(key.getPublicKey());
		// ---
		
		System.out.println();
		
		// ---
		Transaction tx2 = new Transaction(key.getPublicKey(), "public key someone else", new BigDecimal(20));
		tx2.signTransaction(key);
		blackcoin.addTransaction(tx2);
		
		System.out.println("Staring the miner...");		
		blackcoin.minePendingTransactions(key.getPublicKey());
		// ---
		
		System.out.println();
		
		// ---
		Transaction tx3 = new Transaction(key.getPublicKey(), "public key someone else", new BigDecimal(30));
		tx3.signTransaction(key);
		blackcoin.addTransaction(tx3);		
		
		System.out.println("Staring the miner...");		
		blackcoin.minePendingTransactions(key.getPublicKey());
		// ---
		
		
		System.out.println();
		
		
		System.out.println("Balance is: " + blackcoin.getBalanceOfKey(key.getPublicKey()));
				
		System.out.println("Is valid? : " + blackcoin.isChainValid());
		
		blackcoin.getChain().get(1).getTransactions().get(0).setAmount(new BigDecimal(500));		
		System.out.println("Is valid? : " + blackcoin.isChainValid());
		
		System.out.println();
		
		System.out.println(toJson(blackcoin));
	}

	private static String toJson(Blockchain blockchain) {
		Gson gson = new GsonBuilder()
							.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
							.create();
		
		return gson.toJson(blockchain);
	}
	
	

}
