package br.com.blackcoin.domain__;

import java.time.LocalDateTime;
import java.util.List;

import org.bouncycastle.jcajce.provider.digest.SHA256;
import org.bouncycastle.util.encoders.Hex;

import com.google.gson.Gson;

public class Block_ {
	
	private String previousHash;
	private LocalDateTime timestamp;
	private List<Transaction_> transactions;
	private int nonce;
	private String hash;

	Block_(LocalDateTime timestamp, List<Transaction_> transactions, String previousHash) {
	    this.previousHash = previousHash;
	    this.timestamp = timestamp;
	    this.transactions = transactions;
	    this.nonce = 0;
	    this.hash = this.calculateHash();
	}


    public String calculateHash() {
        String dataToHash = previousHash + timestamp + new Gson().toJson(transactions) + nonce;
        SHA256.Digest sha256 = new SHA256.Digest();
        byte[] hash = sha256.digest(dataToHash.getBytes());
        return new String(Hex.encode(hash));
    }

    public void mineBlock(int difficulty) {
        String target = new String(new char[difficulty]).replace('\0', '0');
        while (!hash.substring(0, difficulty).equals(target)) {
            nonce++;
            hash = calculateHash();
        }
        System.out.println("Block mined: " + hash);
    }
    
	boolean hasValidTransactions() {
        for (Transaction_ tx : transactions) {
            if (!tx.isValid()) return false;
        }
        return true;
	}


	public String getPreviousHash() {
		return previousHash;
	}


	public LocalDateTime getTimestamp() {
		return timestamp;
	}


	public List<Transaction_> getTransactions() {
		return transactions;
	}


	public int getNonce() {
		return nonce;
	}


	public String getHash() {
		return hash;
	}
	
}