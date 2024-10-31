package br.com.blackcoin.domain;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.SignatureException;
import java.time.LocalDateTime;

public class Transaction {

	private PublicKey fromKey;
	private String toKey;
	private BigDecimal amount;
	private LocalDateTime timeStamp;
	private String signature;

	Transaction(PublicKey fromKey, String toKey, BigDecimal amount) {
		this.fromKey = fromKey;
		this.toKey = toKey;
		this.amount = amount;
		this.timeStamp = LocalDateTime.now();
	}

	private String calculateHash() throws NoSuchAlgorithmException, UnsupportedEncodingException {
		String dataToHash = fromKey + toKey + amount + timeStamp;
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		byte[] hash = digest.digest(dataToHash.getBytes("UTF-8"));
		StringBuilder hexString = new StringBuilder();

		for (byte b : hash) {
			String hex = Integer.toHexString(0xff & b);
			if (hex.length() == 1) {
				hexString.append('0');
			}
			hexString.append(hex);
		}
		return hexString.toString();
	}

	private void signTransaction(SecurityKey signingKey) throws NoSuchAlgorithmException, UnsupportedEncodingException,
			InvalidKeyException, NoSuchProviderException, SignatureException {
		if (signingKey.getPublicKey() != this.fromKey) {
			throw new Error("You cannot sign transactions for other wallets!");
		}

		String hashTx = calculateHash();
		signature = signingKey.signHash(hashTx);
	}

	public boolean isValid() {
		if (fromKey == null) {
			return true;
		}

		if (signature.isBlank() || signature.length() == 0) {
			throw new Error("No signature in this transaction");
		}

		return true;
		//publicKey = ec.keyFromPublic(this.fromAddress, 'hex');
		//return publicKey.verify(this.calculateHash(), this.signature);
	}

	public PublicKey getFromKey() {
		return fromKey;
	}

	public String getToKey() {
		return toKey;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public LocalDateTime getTimeStamp() {
		return timeStamp;
	}

}