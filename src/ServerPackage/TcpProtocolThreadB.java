package ServerPackage;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import UtilityPackage.FileCheckSumMD5;

public class TcpProtocolThreadB extends Thread {

	private Socket sock = null;

	public String FILE_TO_SEND = "./data/";
	public int size = 1500;

	public TcpProtocolThreadB(String archivo, int tamanio, Socket socket) {
		this.sock = socket;
		this.size = tamanio;
		this.FILE_TO_SEND += archivo;
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
			pw = new PrintWriter(sock.getOutputStream());
			//			bf = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			pw.println(FileCheckSumMD5.checksum(myFile));
			//			System.out.println("paso???");
			//			System.out.println(bf.readLine());
			pw.flush();
			byte[] mybytearray = new byte[(int) myFile.length()];
			fis = new FileInputStream(myFile);
			bis = new BufferedInputStream(fis);
			bis.read(mybytearray, 0, mybytearray.length);
			os = sock.getOutputStream();
			System.out.println("Sending " + FILE_TO_SEND + "(" + mybytearray.length + " bytes)");
			int i = 0;
			while (i + size < mybytearray.length) {
				os.write(mybytearray, i, (size));
				i += size;
			}
			os.write(mybytearray, i, mybytearray.length - i);
			os.flush();
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
