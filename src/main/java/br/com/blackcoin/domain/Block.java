package br.com.blackcoin.domain;

import java.time.LocalDateTime;
import java.util.List;

import org.bouncycastle.jcajce.provider.digest.SHA256;
import org.bouncycastle.util.encoders.Hex;

import com.google.gson.Gson;

public class Block {
	
	private LocalDateTime timestamp;
	private List<Transaction>  transactions;
	private String previousHash = "";
	private String hash;
	private int nounce;
	
	Block(LocalDateTime timestamp, List<Transaction> transactions, String previousHash){
		this.timestamp = timestamp;
		this.transactions = transactions;
		this.previousHash = previousHash;
		this.hash = calculateHash();
	}
	
    public String calculateHash() {
        String dataToHash = previousHash + timestamp + new Gson().toJson(transactions) + nounce;
        SHA256.Digest sha256 = new SHA256.Digest();
        byte[] hash = sha256.digest(dataToHash.getBytes());
        return new String(Hex.encode(hash));
    }
    
    public void mineBlock(int difficulty) {    	
        String target = new String(new char[difficulty]).replace('\0', '0');
        while (!hash.substring(0, difficulty).equals(target)) {
        	nounce++;
            hash = calculateHash();
        }
        System.out.println("Block mined: " + hash);
    }
    
    public boolean hasValidTransaction() throws Exception{
    	for(Transaction transaction : transactions) {
    		if(!transaction.isValid()) {
    			return false;
    		}
    	}
    	return true;
    }

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public List<Transaction> getTransactions() {
		return transactions;
	}

	public String getPreviousHash() {
		return previousHash;
	}

	public String getHash() {
		return hash;
	}

	public int getNounce() {
		return nounce;
	}

}
