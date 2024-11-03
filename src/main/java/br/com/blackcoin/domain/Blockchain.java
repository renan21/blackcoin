package br.com.blackcoin.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Blockchain {
	
	public List<Block> getChain() {
		return chain;
	}

	private List<Block> chain;
	private int difficulty;
	private List<Transaction> pendingTransactions;
	private BigDecimal miningReward;

	Blockchain() {
		chain = new ArrayList<Block>();
		chain.add(createGenesisBlock());
		difficulty = 2;
		pendingTransactions = new ArrayList<>();
		miningReward = new BigDecimal(100);
	}
	
	private Block createGenesisBlock() {
		return new Block(LocalDateTime.of(2024, 1, 1, 0, 0), new ArrayList<>(), "0");
	}
	
	private Block getLatestBlock() {
		return chain.get(chain.size() - 1);
	}

	public void minePendingTransactions(String mineRewardKey){
		Block previousBlock = getLatestBlock();
		Block block = new Block(LocalDateTime.now(), pendingTransactions, previousBlock.getHash());
		block.mineBlock(difficulty);
		
		System.out.println("Block successfully mined!");
		chain.add(block);
		
		pendingTransactions =  new ArrayList<>();
		pendingTransactions.add(new Transaction("", mineRewardKey, miningReward));	
	}
	
	public void addTransaction(Transaction transaction) throws Exception {
		
        if (transaction.getFromKey() == null || transaction.getToKey() == null) {
            throw new Exception("Transaction must include from and to address");
        }

        if (!transaction.isValid()) {
            throw new Exception("Cannot add invalid transaction to chain");
        }
		
		
		pendingTransactions.add(transaction);
	}
	
	public BigDecimal getBalanceOfKey(String key){
		BigDecimal balance = BigDecimal.ZERO;
		
		for(Block block : chain) {
			for(Transaction trans : block.getTransactions()) {
				if (trans.getFromKey().equals(key)) {
					balance = balance.subtract(trans.getAmount());
				}

				if (trans.getToKey().equals(key)) {
					balance = balance.add(trans.getAmount());
				}
			}
		}
		
		return balance;
	}
	
	public boolean isChainValid() throws Exception {
		for(int i = 1; i < chain.size(); i++) {
			Block currentBlock = chain.get(i);
			Block previousBlock = chain.get(i - 1);
			
			if(!currentBlock.hasValidTransaction()) {
				return false;
			}
			
			if(!currentBlock.getHash().equals(currentBlock.calculateHash())) {
				return false;
			}
			
			if(!currentBlock.getPreviousHash().equals(previousBlock.getHash())) {
				return false;
			}
		}
		
		return true;
	}

}
