package br.com.blackcoin.domain;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.spec.ECParameterSpec;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.util.Base64;

public class KeyGenerator {
	
    public static void main(String[] args) throws Exception {
        Security.addProvider(new BouncyCastleProvider());

        ECParameterSpec ecSpec = ECNamedCurveTable.getParameterSpec("secp256k1");

        KeyPairGenerator g = KeyPairGenerator.getInstance("ECDSA", "BC");
        g.initialize(ecSpec);

        KeyPair keyPair = g.generateKeyPair();
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();

        String privateKeyHex = Base64.getEncoder().encodeToString(privateKey.getEncoded());
        String publicKeyHex = Base64.getEncoder().encodeToString(publicKey.getEncoded());

        System.out.println("Your public key (also your wallet address, freely shareable): \n" + publicKeyHex);
        System.out.println("\nYour private key (keep this secret! To sign transactions): \n" + privateKeyHex);
    }

}
