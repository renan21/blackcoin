package br.com.blackcoin.domain;

import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Block {
	
	private String previousHash;
	private LocalDateTime timestamp;
	private List<Transaction> transactions;
	private int nonce;
	private String hash;

	Block(LocalDateTime timestamp, List<Transaction> transactions, String previousHash) {
	    this.previousHash = previousHash;
	    this.timestamp = timestamp;
	    this.transactions = transactions;
	    this.nonce = 0;
	    this.hash = this.calculateHash();
	}


    public String calculateHash() {
        String transactionsJson = convertTransactionsToJson(transactions);
        String dataToHash = previousHash + timestamp + transactionsJson + nonce;

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(dataToHash.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String convertTransactionsToJson(List<Transaction> transactions) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(transactions);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Erro ao converter transações para JSON", e);
        }
    }

    public void mineBlock(int difficulty) {
    	String prefix = "0".repeat(difficulty);
    	while (hash.startsWith(prefix)) {
    		this.nonce++;
    		this.hash = this.calculateHash();
	    }
    }
    
	boolean hasValidTransactions() {
		for (Transaction transaction : transactions) {
			if (!transaction.isValid()) {
				return false;
			}
		}
		return true;
	}


	public String getPreviousHash() {
		return previousHash;
	}


	public LocalDateTime getTimestamp() {
		return timestamp;
	}


	public List<Transaction> getTransactions() {
		return transactions;
	}


	public int getNonce() {
		return nonce;
	}


	public String getHash() {
		return hash;
	}
	


}