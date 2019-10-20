package ServerPackage;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.Scanner;

public class Servidor {

	public final static int SOCKET_PORT = 13267, size = 1500;
	public static int conectados = 0, cantidad = 0;
	public static boolean mode = true;
	public static String archivo;

	public static void main(String[] args) throws IOException {
		ServerSocket servsuck = null;
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in);
		try {
			/*_______________CONEXION DEL SERVIDOR____________________*/
			String result = "";
			System.out.println("First of all, choose file to send to all users \n available files:"
					+ "\n\tfoca.mp4\n\tsuri.mp4");
			archivo = sc.nextLine();
			System.out.println("Please set up initial mode:\n\t[y/s/1] for client based connection\n\t[ow] for server based connection");
			result = sc.nextLine();
			if(result.contains("y")||result.contains("s")||result.contains("1"))
				mode=false;
			if(mode) {
				System.out.println("Due to server connection mode...\n enter buffer size...");
				cantidad=sc.nextInt();
				System.out.println();
			}
			System.out.println("Waiting for "+((mode)?cantidad+" ":"")+"connections in mode "+((mode)?"server":"client")+"...");
			/*________________________________________________________*/

			servsuck = new ServerSocket(SOCKET_PORT);
			if (!mode)
				while (true)
					(new TcpProtocolThreadB(archivo,size, servsuck.accept())).start();
			else {
				TcpProtocolThreadB[] hilos = new TcpProtocolThreadB[cantidad];
				while (true) {
					hilos[conectados++]=new TcpProtocolThreadB(archivo, size, servsuck.accept());
					System.out.println("hay "+conectados+" conectados");
					if (conectados == cantidad) {
						for (TcpProtocolThreadB t : hilos)
							t.start();
						conectados = 0;
					}
				}
			}

		} catch (IOException e) {
			System.err.println("Could not listen on port " + SOCKET_PORT);
			System.exit(-1);
		} finally {
			if (servsuck != null)
				servsuck.close();
		}
	}
}
