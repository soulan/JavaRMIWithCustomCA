package de.soulan.example.rmi.ssl;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.LinkedList;

class EasyRSAHelper {
	/**
	 * extracts a private key from a given path, the private key is expected to be in the RSA format
	 * 
	 * @param keyPath path to the key
	 * @return the private key
	 * @throws NoSuchAlgorithmException
	 * @throws IOException
	 * @throws InvalidKeySpecException
	 */
	public static RSAPrivateKey getPrivateKeyFromFile(String keyPath) throws NoSuchAlgorithmException, IOException, InvalidKeySpecException {
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(keyPath));
        byte[] key = new byte[bis.available()];
        while (bis.available() > 0) {
        	bis.read(key);
        }
        bis.close();
        
        String privateKey = new String(key, "UTF-8");
    	privateKey = privateKey.replaceAll("(-+BEGIN PRIVATE KEY-+\\r?\\n|-+END PRIVATE KEY-+\\r?\\n?|\\r?\\n?)", "");
    	
    	PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey));
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return (RSAPrivateKey) kf.generatePrivate(keySpec);
	}
	
	/**
	 * extracts a certificate from a given path, the certificate is expected to be in the RSA format
	 * 
	 * @param path
	 * @return the certificate
	 * @throws CertificateException
	 * @throws IOException
	 */
	public static Certificate[] getCertificateFromFile(String path) throws CertificateException, IOException {
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
		LinkedList<Certificate> certs = new LinkedList<>(); 
        
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(path));
        while (bis.available() > 0) {
        	certs.add(cf.generateCertificate(bis));
        }
        Certificate[] certsResult = new Certificate[0];
        return certs.toArray(certsResult);
	}
}
