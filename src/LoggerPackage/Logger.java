package LoggerPackage;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.Date;

public class Logger {
	@SuppressWarnings("deprecation")
	public static void loggear(Info in, String nombre, int tamanio) {

		FileWriter fichero = null;
		PrintWriter pw = null;
		Timestamp time = new Timestamp(System.currentTimeMillis());
		try {
			fichero = new FileWriter("./data/prueba.txt", true);
			pw = new PrintWriter(fichero, true);
			pw.println("Fecha de prueba: " + new Date(time.getTime()));
			pw.println("Hora de prueba: " + time.getHours() + ":" + time.getMinutes());
			pw.println("Se transfiere el archivo: " + nombre + " con tamaño: " + tamanio);
			String cadena = "";
			cadena += "Cliente: " + in.getCliente() + "\n";
			cadena += "Tiempo de transferencia: " + in.getTransferencia() + " ms\n";
			cadena += "Entregado: " + true + "\n";
			cadena += "Paquetes enviados: " + in.getPaquetesEnviados() + "\n";
			cadena += "Paquetes recibidos: " + in.getPaquetesRecibidos() + "\n";
			cadena += "Bytes recibidos: " + in.getBytesRecibidos() + "\n";
			cadena += "Bytes transmitidos: " + in.getBytesTransmitidos() + "\n";
			pw.println(cadena);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != fichero)
					fichero.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}

		}

	}
}