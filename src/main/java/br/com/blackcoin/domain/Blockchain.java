package br.com.blackcoin.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class Blockchain {

	private List<Block> chain;
	private int difficulty;
	private List<Transaction> pendingTransactions;
	private BigDecimal miningReward;

	Blockchain() {
		chain = new ArrayList<Block>();
		chain.add(createGenesisBlock());
		difficulty = 2;
		pendingTransactions = new ArrayList<Transaction>();
		miningReward = new BigDecimal(100);
	}

	private Block createGenesisBlock() {
		return new Block(LocalDateTime.now(), new ArrayList<Transaction>(), "0");
	}

	private Block getLatestBlock() {
		return chain.get(chain.size() - 1);
	}

	public void minePendingTransactions(String miningRewardAddress) {
		Block block = new Block(LocalDateTime.now(), this.pendingTransactions, getLatestBlock().getHash());
		block.mineBlock(difficulty);

		System.out.println("Block successfully mined!");
		chain.add(block);

		pendingTransactions = new ArrayList<>();
		pendingTransactions.add(new Transaction(null, miningRewardAddress, miningReward));
	}

	public void addTransaction(Transaction transaction) throws Exception {
        if (transaction.getFromKey() == null || transaction.getToKey() == null) {
            throw new Exception("Transaction must include from and to address");
        }

        if (!transaction.isValid()) {
            throw new Exception("Cannot add invalid transaction to chain");
        }

        this.pendingTransactions.add(transaction);
	}

	public BigDecimal getBalanceOfAddress(String address) {
		BigDecimal balance = BigDecimal.ZERO;
		for (Block block : chain) {
			for (Transaction trans : block.getTransactions()) {
				if (trans.getFromKey().equals(address)) {
					balance = balance.subtract(trans.getAmount());
				}

				if (trans.getToKey().equals(address)) {
					balance = balance.add(trans.getAmount());
				}
			}
		}
		return balance;
	}

//	public List<Transaction> getAllTransactionsForWallet(String key) {
//		List<Transaction> txs = new ArrayList<Transaction>();
//
//		for (Block block : chain) {
//			for (Transaction tx : block.getTransactions()) {
//				if (tx.getFromKey().equals(key) || tx.getToKey().equals(key)) {
//					txs.add(tx);
//				}
//			}
//		}
//
//		return txs;
//	}

    public boolean isChainValid() {
        for (int i = 1; i < chain.size(); i++) {
            Block currentBlock = chain.get(i);
            Block previousBlock = chain.get(i - 1);
            if (!currentBlock.getHash().equals(currentBlock.calculateHash()) || !currentBlock.getPreviousHash().equals(previousBlock.getHash())) {
                return false;
            }
        }
        return true;
    }

}