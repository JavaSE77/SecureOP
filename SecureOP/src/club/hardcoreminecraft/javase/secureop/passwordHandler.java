package club.hardcoreminecraft.javase.secureop;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.bukkit.plugin.Plugin;

public class passwordHandler {

	private String password;
	private Plugin plugin;
	public passwordHandler(String password, Plugin plugin) {
		this.password = password;
		this.plugin = plugin;
	}
	
	public boolean verifyPassword() {
	
		//We are salting hash with password. That's a bad idea, but, if your password is strong, this is not a problem
		String hash = securePassword(password);
		
		if(!isPasswordSet()) {
			return true;
		}
		String currentPass = getPassword();
		if(hash.equals(currentPass)) {
			return true;
		}
		return false;
	}
	
	private boolean isPasswordSet() {
		String currentPass = getPassword();
		if(currentPass == null || currentPass.equals("")) {
			plugin.getLogger().severe("OP password is not set! Please set the op password to secure your server");
			return false;
		}
		return true;
	}
	
	private String getPassword() {

		File file = new File(plugin.getDataFolder() + "/password.txt");
		String fileContents = null;

		if(!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		  try (BufferedReader br = new BufferedReader(new FileReader(file))) {

			   String strCurrentLine;

			   fileContents = br.readLine();
			   br.close();

			  } catch (IOException e) {
			   e.printStackTrace();
			  }
			
		  return fileContents;
	}
	
	public void setPassword(String hash) {

		File file = new File(plugin.getDataFolder() + "/password.txt");

		if(!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		  try (BufferedWriter br = new BufferedWriter(new FileWriter(file))) {

			   br.write(hash);
			   br.close();
			   

			  } catch (IOException e) {
			   e.printStackTrace();
			  }
			
	}
	
	
	//Following methods modified from
	//https://www.appsdeveloperblog.com/encrypt-user-password-example-java/
	public String securePassword(String pwd) {

        byte[] securePassword = hash(pwd.toCharArray(), pwd.getBytes());
 
        String returnValue = Base64.getEncoder().encodeToString(securePassword);
        return returnValue;
	}
	
    private byte[] hash(char[] password, byte[] salt) {
        PBEKeySpec spec = new PBEKeySpec(password, salt, 1000, 256);
        Arrays.fill(password, Character.MIN_VALUE);
        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            return skf.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new AssertionError("Error while hashing a password: " + e.getMessage(), e);
        } finally {
            spec.clearPassword();
        }
    }
	
}
