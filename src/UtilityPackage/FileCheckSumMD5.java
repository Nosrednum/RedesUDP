package UtilityPackage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;

public class FileCheckSumMD5 {
	public static String checksum(String filepath) throws Exception {
		MessageDigest md = MessageDigest.getInstance("MD5");
		// DigestInputStream is better, but you also can hash file like this.
		try (InputStream fis = new FileInputStream(filepath)) {
			byte[] buffer = new byte[1024];
			int nread;
			while ((nread = fis.read(buffer)) != -1) {
				md.update(buffer, 0, nread);
			}
		}
		// bytes to hex
		StringBuilder result = new StringBuilder();
		for (byte b : md.digest())
			result.append(String.format("%02x", b));
		return result.toString();
	}
	public static String checksum(File f) throws Exception {
		MessageDigest md = MessageDigest.getInstance("MD5");
		// DigestInputStream is better, but you also can hash file like this.
		try (InputStream fis = new FileInputStream(f)) {
			byte[] buffer = new byte[1024];
			int nread;
			while ((nread = fis.read(buffer)) != -1) {
				md.update(buffer, 0, nread);
			}
		}
		// bytes to hex
		StringBuilder result = new StringBuilder();
		for (byte b : md.digest())
			result.append(String.format("%02x", b));
		return result.toString();
	}
}
