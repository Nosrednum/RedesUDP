package ServerPackage;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;

import UtilityPackage.FileCheckSumMD5;

public class TcpProtocolThreadB extends Thread {

	private DatagramSocket sock = null;

	public String FILE_TO_SEND = "./data/";
	public int size = 3000, port;

	public TcpProtocolThreadB(String archivo, int tamanio, DatagramSocket socket, int pport) {
		this.sock = socket;
		this.size = tamanio;
		this.FILE_TO_SEND += archivo;
		this.port = pport;
	}

	public void run() {
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		OutputStream os = null;
		PrintWriter pw = null;
		//		BufferedReader bf = null;
		try {
			System.out.println("Accepted connection : " + sock);
			// send file
			File myFile = new File(FILE_TO_SEND);

			pw.println(FileCheckSumMD5.checksum(myFile));
			pw.flush();
			fis = new FileInputStream(myFile);
			bis = new BufferedInputStream(fis);
			byte[] send, mybytearray = new byte[(int) myFile.length()];
			bis.read(mybytearray, 0, mybytearray.length); //carga del archivo en bytes
			System.out.println("Sending " + FILE_TO_SEND + "(" + mybytearray.length + " bytes)");
			int i = 0;
			DatagramPacket dato=null;
			send = new byte[size];
			dato = new DatagramPacket(
					send, // El array de bytes
					size, // Su longitud
					InetAddress.getByName("localhost"),  // Destinatario
					port);   // Puerto del destinatario
			while (i + size < mybytearray.length) {
				for(int j=i;j<size;++j)
					send[j-i]=mybytearray[j];//reconfigura el arreglo
				dato.setData(send);//altera el paquete
				sock.send(dato);//envía el paquete
				i += size;
			}
			send=new byte[mybytearray.length-i];//envío de los últimos bytes
			for(int j=i;j<mybytearray.length;++j)
				send[j-i]=mybytearray[j];
			dato.setData(send);
			sock.send(dato);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (bis != null)
					bis.close();
				if (os != null)
					os.close();
				if (sock != null)
					sock.close();
				//				bf.close();
				pw.close();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
