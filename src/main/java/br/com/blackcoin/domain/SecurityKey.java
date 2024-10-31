package br.com.blackcoin.domain;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.util.Base64;

public class SecurityKey {
	
	private KeyPair keyPair;
	
	SecurityKey() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
    	
        Security.addProvider(new BouncyCastleProvider());

        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC");
        ECGenParameterSpec ecSpec = new ECGenParameterSpec("secp256k1");
        keyPairGenerator.initialize(ecSpec, new SecureRandom());
        keyPair = keyPairGenerator.generateKeyPair();
    }
	
	public String signHash(String hash) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, SignatureException {		
        Signature signer = Signature.getInstance("SHA256withECDSA", "BC");
        signer.initSign(keyPair.getPrivate());
        signer.update(hash.getBytes());
        byte[] signature = signer.sign();
        return bytesToHex(signature);		 
	}

    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
    
    public PublicKey getPublicKey() {
    	return keyPair.getPublic();
    }
    
    public PrivateKey getPrivateKey() {
    	return keyPair.getPrivate();
    }

}