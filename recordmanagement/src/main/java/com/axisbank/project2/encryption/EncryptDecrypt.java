package com.axisbank.project2.encryption;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public class EncryptDecrypt {
	private SecretKey secretKey;
	private SecureRandom random;
	private Cipher encryptCipher;
	private Cipher decryptCipher;
	private IvParameterSpec ivParams;
	
	public EncryptDecrypt()
	{
		random =new SecureRandom();
		try {
			
			secretKey = KeyGenerator.getInstance("AES").generateKey();
			encryptCipher =Cipher.getInstance("AES/CBC/PKCS5Padding");
			decryptCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			byte [] initializationVector = new byte[encryptCipher.getBlockSize()];
			random.nextBytes(initializationVector);
			ivParams= new IvParameterSpec(initializationVector);
			encryptCipher.init(Cipher.ENCRYPT_MODE,secretKey,ivParams);
			decryptCipher.init(Cipher.DECRYPT_MODE, secretKey,ivParams);
			
			
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String encrypt(String plainText) {
		byte[] bytes=plainText.getBytes();
		byte[] cipherText=null;
		try {
			cipherText = encryptCipher.doFinal(bytes);
			return Base64.getEncoder().encodeToString(cipherText);
		} catch (IllegalBlockSizeException | BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	

	public String encryptFile(FileInputStream fis) throws IOException {
		byte [] data =new byte[fis.available()];
		fis.read(data);
		byte[] cipherText = null;
		try {
			cipherText = encryptCipher.doFinal(data);
			return Base64.getEncoder().encodeToString(cipherText);
		}
		catch (IllegalBlockSizeException | BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}
	
	
	
	
	
	public String decrypt(String cipherText) {
		byte [] plainText = null;
		try {
			plainText = decryptCipher.doFinal(Base64.getDecoder().decode(cipherText.getBytes()));
			return new String(plainText,"UTF8");
		} catch (IllegalBlockSizeException | BadPaddingException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	public void  decryptFile(String str) throws IllegalBlockSizeException, BadPaddingException, IOException {
		FileOutputStream fos= new FileOutputStream("C:\\\\Users\\\\Welcome\\\\Desktop\\\\Green home Foods\\\\images\\\\addmealDecrypted.jpg");
		byte[] Encrypteddata=str.getBytes();
		byte[] decrypteddata = null;
		decrypteddata = decryptCipher.doFinal(Base64.getDecoder().decode(Encrypteddata));
		fos.write(decrypteddata);
		
		
	}
	
	public byte[] decryptByteArray(byte[] cypher) throws IllegalBlockSizeException, BadPaddingException {
		byte[] decrypteddata = null;
		decrypteddata = decryptCipher.doFinal(Base64.getDecoder().decode(cypher));
		return decrypteddata;
		
	}
	
	
}