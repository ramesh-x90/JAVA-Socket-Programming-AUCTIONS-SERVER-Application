package serverSrc;


import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

public class Security {

	private KeyPairGenerator generator;
	private PublicKey publickey;
	private PrivateKey PrivateKey;

	/**
	 * 
	 */
	public Security() {
		
		try {
			generator = KeyPairGenerator.getInstance("RSA");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		
		generator.initialize(2048);
		KeyPair pair = generator.generateKeyPair();
		publickey = pair.getPublic();
		PrivateKey = pair.getPrivate();
		

		
		
		
	}
	
	
	
	

	public PublicKey getPublickey() {
		return publickey;
	}

	public void setPublickey(PublicKey publickey) {
		this.publickey = publickey;
	}

	public PrivateKey getPrivateKey() {
		return PrivateKey;
	}

	public void setPrivateKey(PrivateKey privateKey) {
		PrivateKey = privateKey;
	}
	
	
	
	

}
