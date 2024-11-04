package br.com.blackcoin.domain;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import org.bouncycastle.jcajce.provider.digest.SHA256;
import org.bouncycastle.util.encoders.Hex;

public class Transaction {
	
	private String fromKey;
	private String toKey;
	private BigDecimal amount;
	private String signature;

	public Transaction(String fromKey, String toKey, BigDecimal amount) {
		this.fromKey = fromKey;
		this.toKey = toKey;
		this.amount = amount;
	}
	
	private String calculateHash() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        String dataToHash = fromKey + toKey + amount;
        SHA256.Digest sha256 = new SHA256.Digest();
        byte[] hash = sha256.digest(dataToHash.getBytes());
        return new String(Hex.encode(hash));
	}
	
	public void signTransaction(Key key) throws Exception  {
        if (!key.getPublicKey().equals(fromKey)) {
            throw new Exception("You cannot sign transactions for other wallets!");
        }
				
		String transactionHash = calculateHash();
		
		signature = key.sign(transactionHash);
		

	}
	
	//TODO: Refactor
	public boolean isValid() throws Exception{
		if(fromKey.equals("")) {
			return true;
		}
		
		if(signature == null || signature.length() == 0) {
			throw new RuntimeException("No signature in this transaction");
		}
		
        byte[] publicKeyBytes = Base64.getDecoder().decode(fromKey);		

        X509EncodedKeySpec spec = new X509EncodedKeySpec(publicKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("ECDSA");
        PublicKey publicKey = keyFactory.generatePublic(spec);
        
        Signature ecdsaVerify = Signature.getInstance("SHA256withECDSA");
        ecdsaVerify.initVerify(publicKey);
        ecdsaVerify.update(calculateHash().getBytes());
        return ecdsaVerify.verify(Base64.getDecoder().decode(signature));
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

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
	

}
