package LoggerPackage;

public class Info {
	private String cliente;
	private boolean entrega;
	private String ttransferencia;
	private int paquetesEnviados;
	private int paquetesRecibidos;
	private int bytesTransmitidos;
	private int bytesRecibidos;

	public Info(String cliente, String ttransferencia, int paquetesEnviados, int paquetesRecibidos,
			int bytesTransmitidos, int bytesRecibidos) {
		this.cliente = cliente;
		this.ttransferencia = ttransferencia;
		this.paquetesEnviados = paquetesEnviados;
		this.paquetesRecibidos = paquetesRecibidos;
		this.bytesTransmitidos = bytesTransmitidos;
		this.bytesRecibidos = bytesRecibidos;
	}

	public String getCliente() {
		return cliente;
	}

	public boolean isEntrega() {
		return entrega;
	}

	public String getTransferencia() {
		return ttransferencia;
	}

	public int getPaquetesEnviados() {
		return paquetesEnviados;
	}

	public int getPaquetesRecibidos() {
		return paquetesRecibidos;
	}

	public int getBytesTransmitidos() {
		return bytesTransmitidos;
	}

	public int getBytesRecibidos() {
		return bytesRecibidos;
	}
}
