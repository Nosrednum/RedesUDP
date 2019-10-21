package ClientPackage;

import java.io.*;
import java.net.Socket;

import LoggerPackage.Info;
import LoggerPackage.Logger;
import UtilityPackage.FileCheckSumMD5;
import UtilityPackage.Timer;

public class ClienteBi {

	public final static int SOCKET_PORT = 13267;
	public final static String SERVER = "18.208.134.243", RUTA = "./data2/";

	public final static int FILE_SIZE = (int) (Math.pow(2, 20) * 251); // Espacio para el archivo

	public static void main(String[] args) throws Exception {
		int bytesRead, current = 0;
		Timer t = new Timer();
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;
		PrintWriter pw = null;
		BufferedReader bf=null;
		Socket sock = null;
		String hash = new String();

		try {
			sock = new Socket(SERVER, SOCKET_PORT);
			System.out.println("Connecting...");
			System.out.println("Connection established successfully");
			bf = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			//			pw = new PrintWriter(sock.getOutputStream());
			hash = bf.readLine();
			//			pw.println("Estoy recibiendo el archivo...");
			//			pw.flush();
			sock.getOutputStream().flush();
			t.start();
			InputStream is = sock.getInputStream(); // recepción del socket
			byte mybytearray[] = new byte[FILE_SIZE]; // representación byte a byte del archivo
			fos = new FileOutputStream(RUTA + "fileof" + sock.getLocalPort() + ".mp4"); // Stream de envio de archivos
			bos = new BufferedOutputStream(fos); // Stream de escritura del cliente
			bytesRead = is.read(mybytearray, 0, mybytearray.length);
			current = bytesRead;
			int div =bytesRead;
			/* Lectura del archivo */
			do {
				bytesRead = is.read(mybytearray, current, (mybytearray.length - current));
				if (bytesRead >= 0)
					current += bytesRead;
			} while (bytesRead > -1);
			bos.write(mybytearray, 0, current);
			bos.flush();
			int size =(int) (new File(RUTA + "fileof" + sock.getLocalPort() + ".mp4").length()), paqs =(int) size/div +1;
			sock.getOutputStream().flush();
			boolean correcto = hash.equals(FileCheckSumMD5.checksum(RUTA + "fileof" + sock.getLocalPort() + ".mp4"));
			System.out.println("El archivo fue recibido "+((correcto)?"correctamente":"incorrectamente"));
			System.out.println("File downloaded (" + current + " bytes read)");
			Logger.loggear(new Info("socket "+sock.getLocalPort(), t.stop()+"", paqs, paqs, size, size),
					RUTA + "fileof" + sock.getLocalPort() + ".mp4",
					size);
		} finally {
			if (fos != null)
				fos.close();
			if (bos != null)
				bos.close();
			if (sock != null)
				sock.close();
			if (pw != null)
				pw.close();
			if (bf != null)
				bf.close();
		}
	}
}
