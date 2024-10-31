package br.com.blackcoin.domain;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.time.LocalDateTime;

import org.bouncycastle.jcajce.provider.digest.SHA256;
import org.bouncycastle.util.encoders.Hex;

public class Transaction {

	private String fromKey;
	private String toKey;
	private BigDecimal amount;
	private LocalDateTime timeStamp;
	private String signature;

	Transaction(String fromKey, String toKey, BigDecimal amount) {
		this.fromKey = fromKey;
		this.toKey = toKey;
		this.amount = amount;
		this.timeStamp = LocalDateTime.now();
	}

	private String calculateHash() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        String dataToHash = fromKey + toKey + amount + timeStamp;
        SHA256.Digest sha256 = new SHA256.Digest();
        byte[] hash = sha256.digest(dataToHash.getBytes());
        return new String(Hex.encode(hash));
	}

	private void signTransaction(PrivateKey privateKey) throws Exception {
        if (!fromKey.equals(CryptoUtil.getStringFromKey(privateKey))) {
            throw new Exception("You cannot sign transactions for other wallets!");
        }
        String data = calculateHash();
        Signature rsa = Signature.getInstance("SHA256withECDSA");
        rsa.initSign(privateKey);
        rsa.update(data.getBytes());
        byte[] signature = rsa.sign();
        this.signature = Hex.toHexString(signature);
	}

	public boolean isValid() {
        if (fromKey == null) return true;
        if (signature == null) throw new RuntimeException("No signature in this transaction");

        try {
            PublicKey publicKey = CryptoUtil.getKeyFromString(fromKey);
            return CryptoUtil.verifySignature(publicKey, calculateHash(), Hex.decode(signature));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
	}
	
	public String getFromKey() {
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

	public String getSignature() {
		return signature;
	}

}