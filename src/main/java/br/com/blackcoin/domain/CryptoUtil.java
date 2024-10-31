package br.com.blackcoin.domain;

import org.bouncycastle.util.encoders.Hex;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;

public class CryptoUtil {
	
    public static String getStringFromKey(PrivateKey key) {
        return Hex.toHexString(key.getEncoded());
    }

    public static PublicKey getKeyFromString(String key) throws Exception {
        byte[] byteKey = Hex.decode(key);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(byteKey);
        KeyFactory keyFactory = KeyFactory.getInstance("ECDSA");
        return keyFactory.generatePublic(spec);
    }

    public static boolean verifySignature(PublicKey publicKey, String data, byte[] signature) throws Exception {
        Signature ecdsaVerify = Signature.getInstance("SHA256withECDSA");
        ecdsaVerify.initVerify(publicKey);
        ecdsaVerify.update(data.getBytes());
        return ecdsaVerify.verify(signature);
    }

}