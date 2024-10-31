package br.com.blackcoin.domain;

import java.math.BigDecimal;
import java.security.PublicKey;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

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
		return this.chain.get(chain.size() - 1);
	}

	public void minePendingTransactions(String miningRewardAddress) {
		Transaction rewardTx = new Transaction(null, miningRewardAddress, this.miningReward);
		pendingTransactions.add(rewardTx);

		Block block = new Block(LocalDateTime.now(), this.pendingTransactions, this.getLatestBlock().getHash());
		block.mineBlock(this.difficulty);

		chain.add(block);

		pendingTransactions.clear();
	}

	public void addTransaction(Transaction transaction) {
		if (!(transaction.getFromKey() == null) || !transaction.getToKey().isBlank()) {
			throw new Error("Transaction must include from and to address");
		}

		if (!transaction.isValid()) {
			throw new Error("Cannot add invalid transaction to chain");
		}

		if (transaction.getAmount().compareTo(new BigDecimal(0)) <= 0) {
			throw new Error("Transaction amount should be higher than 0");
		}

		BigDecimal walletBalance = getBalanceOfAddress(transaction.getFromKey());
		if (walletBalance.compareTo(transaction.getAmount()) < 0) {
			throw new Error("Not enough balance");
		}

		List<Transaction> pendingTxForWallet = pendingTransactions.stream()
				.filter(tx -> tx.getFromKey().equals(transaction.getFromKey())).toList();

		if (pendingTxForWallet.size() > 0) {
			BigDecimal totalPendingAmount = pendingTxForWallet.stream().map(Transaction::getAmount)
					.reduce(BigDecimal.ZERO, BigDecimal::add);

			BigDecimal totalAmount = totalPendingAmount.add(transaction.getAmount());

			if (totalAmount.compareTo(walletBalance) > 0) {
				throw new Error("Pending transactions for this wallet is higher than its balance.");
			}
		}
		this.pendingTransactions.add(transaction);
	}

	private BigDecimal getBalanceOfAddress(PublicKey publicKey) {
		BigDecimal balance = BigDecimal.ZERO;

		for (Block block : chain) {
			for (Transaction trans : block.getTransactions()) {
				if (trans.getFromKey().equals(publicKey)) {
					balance = balance.subtract(trans.getAmount());
				}

				if (trans.getToKey().equals(publicKey)) {
					balance = balance.add(trans.getAmount());
				}
			}
		}

		return balance;
	}

	public List<Transaction> getAllTransactionsForWallet(String key) {
		List<Transaction> txs = new ArrayList<Transaction>();

		for (Block block : chain) {
			for (Transaction tx : block.getTransactions()) {
				if (tx.getFromKey().equals(key) || tx.getToKey().equals(key)) {
					txs.add(tx);
				}
			}
		}

		return txs;
	}

	public boolean isChainValid() {

		String realGenesis = new Gson().toJson(createGenesisBlock());

		if (realGenesis.equals(new Gson().toJson(chain.get(0)))) {
			return false;
		}

		for (int i = 1; i < chain.size(); i++) {
			Block currentBlock = chain.get(i);
			Block previousBlock = chain.get(i - 1);

			if (!previousBlock.getHash().equals(currentBlock.getPreviousHash())) {
				return false;
			}

			if (!currentBlock.hasValidTransactions()) {
				return false;
			}

			if (!currentBlock.getHash().equals(currentBlock.calculateHash())) {
				return false;
			}
		}

		return true;
	}

}