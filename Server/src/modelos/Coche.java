package modelos;

import java.io.Serializable;

public class Coche implements Serializable {

	private String id;
	private String modelo;
	private String pais;

	private int motor;
	private int cilindros;
	private int potencia;
	private int revXmin;
	private int velocidad;
	private double costAt100Km;

	public Coche() {
	}

	public Coche(String id, String modelo, String pais, int motor, int cilindros, int potencia, int revXmin,
			int velocidad, double costAt100Km) {
		super();
		this.id = id;
		this.modelo = modelo;
		this.pais = pais;
		this.motor = motor;
		this.cilindros = cilindros;
		this.potencia = potencia;
		this.revXmin = revXmin;
		this.velocidad = velocidad;
		this.costAt100Km = costAt100Km;
	}

	/* GET & SET ID */

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	/* GET & SET Modelo */

	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) {
		this.modelo = modelo;
	}

	/* GET & SET Pais */

	public String getPais() {
		return pais;
	}

	public void setPais(String pais) {
		this.pais = pais;
	}

	/* GET & SET Motor */

	public int getMotor() {
		return motor;
	}

	public void setMotor(int motor) {
		this.motor = motor;
	}

	/* GET & SET Cilindros */

	public int getCilindros() {
		return cilindros;
	}

	public void setCilindros(int cilindros) {
		this.cilindros = cilindros;
	}

	/* GET & SET Potencia */

	public int getPotencia() {
		return potencia;
	}

	public void setPotencia(int potencia) {
		this.potencia = potencia;
	}

	/* GET & SET Rev. x min. */

	public int getRevXmin() {
		return revXmin;
	}

	public void setRevXmin(int revXmin) {
		this.revXmin = revXmin;
	}

	/* GET & SET Velocidad */

	public int getVelocidad() {
		return velocidad;
	}

	public void setVelocidad(int velocidad) {
		this.velocidad = velocidad;
	}

	/* GET & SET Consumo/100 Km */

	public double getConsAt100Km() {
		return costAt100Km;
	}

	public void setConsAt100Km(double costAt100Km) {
		this.costAt100Km = costAt100Km;
	}

	@Override
	public String toString() {
		return "=======Coche=======\nID: " + this.id + "\nModelo: " + this.modelo + "\nPais: " + this.pais + "\nMotor: "
				+ this.motor + "\nCilindros: " + this.cilindros + "\nPotencia: " + this.potencia + "\nRevXmin: "
				+ this.revXmin + "\nVelocidad: " + this.velocidad + "\nConsumo 100km: " + this.costAt100Km + "\n";
	}

}
