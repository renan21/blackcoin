package br.com.blackcoin.domain__;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class Blockchain_ {

	private List<Block_> chain;
	private int difficulty;
	private List<Transaction_> pendingTransactions;
	private BigDecimal miningReward;

	Blockchain_() {
		chain = new ArrayList<Block_>();
		chain.add(createGenesisBlock());
		difficulty = 2;
		pendingTransactions = new ArrayList<Transaction_>();
		miningReward = new BigDecimal(100);
	}

	private Block_ createGenesisBlock() {
		return new Block_(LocalDateTime.of(2024, 1, 1, 0, 0), new ArrayList<Transaction_>(), "0");
	}

	private Block_ getLatestBlock() {
		return chain.get(chain.size() - 1);
	}

	public void minePendingTransactions(String miningRewardAddress) {
		Block_ block = new Block_(LocalDateTime.now(), this.pendingTransactions, getLatestBlock().getHash());
		block.mineBlock(difficulty);

		System.out.println("Block successfully mined!");
		chain.add(block);

		pendingTransactions = new ArrayList<>();
		pendingTransactions.add(new Transaction_(null, miningRewardAddress, miningReward));
	}

	public void addTransaction(Transaction_ transaction) throws Exception {
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
		for (Block_ block : chain) {
			for (Transaction_ trans : block.getTransactions()) {
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
        	Block_ currentBlock = chain.get(i);
        	Block_ previousBlock = chain.get(i - 1);
            if (!currentBlock.getHash().equals(currentBlock.calculateHash()) || !currentBlock.getPreviousHash().equals(previousBlock.getHash())) {
                return false;
            }
        }
        return true;
    }

}