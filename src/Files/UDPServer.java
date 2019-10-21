package Files;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.security.MessageDigest;

public class UDPServer 
{
	String checkSum;
	DatagramSocket serverSocket;
	DatagramPacket receivePacket, sendpacket;
	private OutputStream ouStream = null;
	static String RUTA="localhost";
	static int PORT=5432;

	public static void main(String[] args) {
		UDPServer c = new UDPServer();
		while(true)
			c.comunicar();
	}

	public void comunicar() 
	{
		try 
		{
			serverSocket = new DatagramSocket(5432);

			byte[] receiveData = new byte[1024];

			byte[] sendData = new byte[1024];

			receivePacket = new DatagramPacket(receiveData, receiveData.length);

			serverSocket.receive(receivePacket);

			InetAddress IPAddress = receivePacket.getAddress();

			System.out.println("Esperando por for datagrama. . .\n");

			//Integridad de los datos transferidos
			checkSum = new String(digest(RUTA));

			String msg = new String(receivePacket.getData());

			System.out.println("Checksum of Client: " + msg);

			//no hubo cambios
			if (checkSum.equalsIgnoreCase(msg)) 
			{
				System.out.println("The file didn't update");
				String reply = "y";
				sendData = reply.getBytes();
				sendpacket = new DatagramPacket(sendData, sendData.length);
				serverSocket.send(sendpacket);
				System.out.println("Reply :" + new String(sendData));
			} 
			// hubo cambios
			else 
			{
				String reply = "n";
				sendData = reply.getBytes();
				System.out.println("Reply :" + new String(sendData));
				sendpacket = new DatagramPacket(sendData, sendData.length);
				serverSocket.send(sendpacket);
				System.out.println("The file updated");
				transferenciaArchivo(RUTA);
			}
		} 
		catch (Exception ex) {
			System.out.println("Error msg: " + ex.getMessage());
		}

	}

	public String digest(String path) {
		try {

			MessageDigest md = MessageDigest.getInstance("MD5");
			FileInputStream fis = new FileInputStream(path);
			byte[] dataBytes = new byte[1024];
			int nread = 0;

			while ((nread = fis.read(dataBytes)) != -1) 
			{
				md.update(dataBytes, 0, nread);
			};


			byte[] mdbytes = md.digest();
			StringBuffer sb = new StringBuffer();

			for (int i = 0; i < mdbytes.length; i++)
			{
				sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
			}

			checkSum = sb.toString();
			System.out.println("Digest(in hex format):: " + checkSum);
		} catch (Exception ex) {
			System.out.println("Error msg: " + ex);
		}
		return checkSum;
	}

	public void transferenciaArchivo(String path) {
		try {
			FileWriter fw = new FileWriter(new File(RUTA));
			byte[] receiveData = new byte[1000000];

			while (receiveData != null) 
			{
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
				serverSocket.receive(receivePacket);
				String sentence = new String(receivePacket.getData());
				fw.write(sentence.trim());
				fw.flush();
				System.out.printf("RECEIVED: %s ", new String(receivePacket.getData()));
			}

			fw.flush();
			fw.close();
			serverSocket.close();

		} catch (Exception e) {
			System.err.println(e);
		}
	}
}