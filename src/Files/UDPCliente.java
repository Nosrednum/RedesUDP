package Files;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.security.MessageDigest;
import java.util.Properties;

//client code
public class UDPCliente 
{
	DatagramSocket clientSocket;
	DatagramPacket sendPacket, receivePacket;
	private InputStream inStream = null;
	String checkSumValue, RUTA="C:\\Users\\ander\\Desktop\\Vid1.mp4", SERVER="localhost";

	public static void main(String[] args) {
		UDPCliente c = new UDPCliente();
		c.comunicar();
	}

	public void comunicar() 
	{
		byte[] sendData = new byte[1024];
		byte[] receiveData = new byte[1024];
		String  checkSumValue;
		try {

			String serverHostname = SERVER;
			int port = 5432;

			clientSocket = new DatagramSocket();
			InetAddress ipAddress = InetAddress.getByName(serverHostname);
			System.out.println("Attempting to connect to " + ipAddress + " via UDP " + port);
			//integridad
			checkSumValue = digest(RUTA);
			sendData = checkSumValue.getBytes();
			sendPacket = new DatagramPacket(sendData, sendData.length, ipAddress, port);
			clientSocket.send(sendPacket);
			System.out.println("Client Checksum sent to server : " + new String(sendPacket.getData()));

			receivePacket = new DatagramPacket(receiveData, receiveData.length);
			clientSocket.receive(receivePacket);
			String res =new String(receivePacket.getData());

			System.out.println("Reply: "+ res);

			if (res.equalsIgnoreCase("n")) 
			{
				transferenciaArchivo(RUTA);
				clientSocket.close();
			}
			else
			{
				Thread.sleep(1000);
				clientSocket.close();
			}
		} catch (Exception ex) {
			System.out.println("Exception :" + ex.getMessage());
		}
	}
	//Metodo para la integridad de los mensajes
	public String digest(String path)
	{
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			FileInputStream fis = new FileInputStream(path);

			byte[] dataBytes = new byte[1024];

			int nread = 0;
			while ((nread = fis.read(dataBytes)) != -1) {
				md.update(dataBytes, 0, nread);
			};
			byte[] mdbytes = md.digest();
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < mdbytes.length; i++) {
				sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
			}
			checkSumValue = sb.toString();
			System.out.println("Digest(in hex format):: " + checkSumValue);
		} catch (Exception ex) {
			System.out.println("Exception : " + ex.getMessage());
		}
		return checkSumValue;
	}

	public void transferenciaArchivo(String path)
	{
		try {
			FileInputStream fstream = new FileInputStream(RUTA);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));

			File file = new File(RUTA);
			FileInputStream fis = new FileInputStream(file);
			byte[] fsize = new byte[(int) file.length()];
			int size = fis.read(fsize);
			System.out.println("Size = " + size);
			InetAddress addr = InetAddress.getByName(SERVER);
			byte[] buf = new byte[10000];

			String DataLine;

			while ((DataLine = br.readLine()) != null) 
			{
				receivePacket=new DatagramPacket(DataLine.getBytes(), DataLine.length());
				System.out.println(DataLine);
				DatagramSocket socket = new DatagramSocket();
				socket.send(receivePacket);
				System.out.println("Sent Packet: "+new String(receivePacket.getData()));
			}

		} 
		catch (Exception ex) 
		{
			System.out.println("Exception in file: " + ex.getMessage());
		}
	}
}