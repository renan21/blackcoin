package br.com.blackcoin.domain;

import java.math.BigDecimal;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.LocalDateTime;
import java.util.Base64;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import br.com.blackcoin.adapter.LocalDateTimeAdapter;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.jce.spec.ECPrivateKeySpec;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.Security;

public class Main {

	public static void main(String[] args) throws Exception {		

		Key key = new Key("MIGNAgEAMBAGByqGSM49AgEGBSuBBAAKBHYwdAIBAQQgqU0vN+VdtCF/6GqmBkQVxw4VSq+9UOwNj40cGVBcJuqgBwYFK4EEAAqhRANCAAS+AbOLawBuDcsxQDHwbfFtPf8Q8/XPu1jjmivqedR8nyhVWOT5/mcRM174Ojik9srUUGNPNzyxXITmg+orLVPI");
	
		Blockchain blackcoin = new Blockchain();
		
		Transaction tx1 = new Transaction(key.getPublicKey(), "public key someone else", new BigDecimal(200));
		tx1.signTransaction(key.getKeyPair());
			
		blackcoin.addTransaction(tx1);
		
		System.out.println("Staring the miner...");		
		blackcoin.minePendingTransactions("chave3");
		
		System.out.println("Chave3 balance is: " + blackcoin.getBalanceOfKey("chave3"));
				
		//System.out.println(toJson(blackcoin));
	}

	private static String toJson(Blockchain blockchain) {
		Gson gson = new GsonBuilder()
							.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
							.create();
		
		return gson.toJson(blockchain);
	}
	
	

}
