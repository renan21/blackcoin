package br.com.blackcoin.domain;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.interfaces.ECPrivateKey;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;
import org.bouncycastle.jce.spec.ECPrivateKeySpec;
import org.bouncycastle.jce.spec.ECPublicKeySpec;

public class Key {
	
	private KeyPair keyPair;

	public Key(String privateKey) throws Exception{
		Security.addProvider(new BouncyCastleProvider());
		PrivateKey privateKey_ = loadPrivateKey(privateKey);
		keyPair = reconstructKeyPair(privateKey_);
	}
	
	public String getPrivateKey() {
		return encodeToString(keyPair.getPrivate().getEncoded());
	}

	public String getPublicKey() {
		return encodeToString(keyPair.getPublic().getEncoded());
	}
		
    private KeyPair reconstructKeyPair(PrivateKey privateKey) throws Exception {
        ECNamedCurveParameterSpec ecSpec = ECNamedCurveTable.getParameterSpec("secp256k1");
        
        ECPrivateKeySpec privateKeySpec = new ECPrivateKeySpec(((ECPrivateKey) privateKey).getD(), ecSpec);

        KeyFactory keyFactory = KeyFactory.getInstance("EC", "BC");

        PublicKey publicKey = keyFactory.generatePublic(new ECPublicKeySpec(ecSpec.getG().multiply(privateKeySpec.getD()), ecSpec));

        return new KeyPair(publicKey, privateKey);
    }
	
    private static PrivateKey loadPrivateKey(String privateKeyPEM) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(privateKeyPEM);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);

        KeyFactory keyFactory = KeyFactory.getInstance("EC", "BC");
        return keyFactory.generatePrivate(keySpec);
    }
    	
	private String encodeToString(byte[] encodedeKey) {
        return Base64.getEncoder().encodeToString(encodedeKey);
	}

	public String sign(String transactionHash) throws Exception {
        Signature edca = Signature.getInstance("SHA256withECDSA", "BC");
        edca.initSign(keyPair.getPrivate());
        edca.update(transactionHash.getBytes());
        byte[] sig = edca.sign();
        return Base64.getEncoder().encodeToString(sig);
	}

}
