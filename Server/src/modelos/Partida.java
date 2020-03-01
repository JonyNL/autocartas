package modelos;

import main.Main;
import metodos.Metodos;

import java.rmi.server.UID;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Partida {

	private enum PlayResult {
		P1, P2, EMPATE;
	}

	private UID idGame;
	private String caracteristica;
	private Sesion sesion;
	private String idCocheP2;

	private String laslPlayP1id;
	private String lastPlayP2id;
	private User userPlayer;
	private User cpu;
	private boolean p1First;
	private int ronda;
	private int vPlayer;
	private int empates;
	private int vCpu;
	private ArrayList<Coche> manoP1;
	private ArrayList<Coche> manoP2;

	public Partida() {
	}

	public Partida(Sesion sesion, User userPlayer) {
		this.idGame = new UID();
		this.sesion = sesion;
		this.userPlayer = userPlayer;
		this.cpu = new User("CPU", "cpu", "cpu");
		this.p1First = true;
		this.ronda = 1;
		this.vPlayer = 0;
		this.vCpu = 0;
		manoP1 = new ArrayList<>();
		manoP2 = new ArrayList<>();
		setManos();

	}

	private void setManos() {
		List<Coche> coches = new ArrayList<>();
		Connection c = Main.conectarMySQL();
		String sql = "SELECT * FROM coches";
		try {
			PreparedStatement pst = c.prepareStatement(sql);
			ResultSet rs = pst.executeQuery();
			if (rs.first()) {
				do {
					/*
					 * 1 = id 2 = modelo 3 = pais 4 = motor 5 = cilindros 6 = potencia 7 = 6revXmin
					 * 8 = velocidad 9 = consAt100Km
					 */
					coches.add(new Coche(rs.getString(1), rs.getString(2), rs.getString(3), rs.getInt(4), rs.getInt(5),
							rs.getInt(6), rs.getInt(7), rs.getInt(8), rs.getDouble(9)));
				} while (rs.next());
				c.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println(coches);
		if (!coches.isEmpty()) {
			for (int i = 12; i > 0; i -= 2) {

				int posCoche = Metodos.randomNumber(0, coches.size() - 1);
				manoP1.add(coches.get(posCoche));
				coches.remove(posCoche);

				posCoche = Metodos.randomNumber(0, coches.size() - 1);
				manoP2.add(coches.get(posCoche));
				coches.remove(posCoche);
			}
		} else
			System.out.println("Lista coches está vacia");
	}

	/* GET & SET idGame */

	public UID getIdGame() {
		return idGame;
	}

	public void setIdGame(UID idGame) {
		this.idGame = idGame;
	}

	/* GET & SET Sesion */

	public Sesion getSesion() {
		return sesion;
	}

	public void setSesion(Sesion sesion) {
		this.sesion = sesion;
	}

	/* GET & SET Caracteristica */

	public String getCaracteristica() {
		return caracteristica;
	}

	public void setCaracteristica(String caracteristica) {
		this.caracteristica = caracteristica;
	}

	/* GET & SET userPlayer */

	public User getUserPlayer() {
		return userPlayer;
	}

	public void setUserPlayer(User userPlayer) {
		this.userPlayer = userPlayer;
	}

	/* GET & SET cpu */

	public User getCpu() {
		return cpu;
	}

	public void setCpu(User cpu) {
		this.cpu = cpu;
	}

	/* GET & SET ronda */

	public int getRonda() {
		return ronda;
	}

	public void setRonda(int ronda) {
		this.ronda = ronda;
	}

	/* GET & SET vPlayer */

	public int getvPlayer() {
		return vPlayer;
	}

	public void setvPlayer(int vPlayer) {
		this.vPlayer = vPlayer;
	}

	/* GET & SET empates */

	public int getEmpates() {
		return empates;
	}

	public void setEmpates(int empates) {
		this.empates = empates;
	}

	/* GET & SET vCpu */

	public int getvCpu() {
		return vCpu;
	}

	public void setvCpu(int vCpu) {
		this.vCpu = vCpu;
	}

	/* GET & SET manoP1 */

	public ArrayList<Coche> getManoP1() {
		return manoP1;
	}

	public void setManoP1(ArrayList<Coche> manoP1) {
		this.manoP1 = manoP1;
	}

	/* GET & SET manoP2 */

	public ArrayList<Coche> getManoP2() {
		return manoP2;
	}

	public void setManoP2(ArrayList<Coche> manoP2) {
		this.manoP2 = manoP2;
	}

	/* GET & SET P1First */

	public boolean isP1First() {
		return p1First;
	}

	public void setP1First(boolean p1First) {
		this.p1First = p1First;
	}

	/* GET & SET idCocheP2 */

	public String getIdCocheP2() {
		return idCocheP2;
	}

	public void setIdCocheP2(String idCocheP2) {
		this.idCocheP2 = idCocheP2;
	}

	/* GET & SET lastPlayP1id */

	public String getLaslPlayP1id() {
		return laslPlayP1id;
	}

	public void setLaslPlayP1id(String laslPlayP1id) {
		this.laslPlayP1id = laslPlayP1id;
	}

	/* GET & SET lastPlayP2id */

	public String getLastPlayP2id() {
		return lastPlayP2id;
	}

	public void setLastPlayP2id(String lastPlayP2id) {
		this.lastPlayP2id = lastPlayP2id;
	}

	/*
	 * Devuelve el id de la carta que jugará el cpu de forma aleatoria
	 * 
	 */
	public String jugadaCPU() {
		return manoP2.get(Metodos.randomNumber(0, manoP2.size() - 1)).getId();
	}

	/*
	 * Devuelve una caracteristica aleatoria para el turno del cpu
	 * 
	 */
	public String randomCaracteristica() {
		int numCaracteristica = Metodos.randomNumber(1, 6);
		/*
		 * do { numCaracteristica = Metodos.randomNumber(1, 6); } while
		 * (numCaracteristica < 1);
		 */
		switch (numCaracteristica) {
		case 1:
			return "MOTOR";
		case 2:
			return "CILINDROS";
		case 3:
			return "POTENCIA";
		case 4:
			return "REVXMIN";
		case 5:
			return "VELOCIDAD";
		case 6:
			return "COSTE100KM";
		default:
			return "randomCaracteristicas:error";

		}
	}

	/*
	 * Metodo en el que se juega una ronda
	 * 
	 */
	public int playRound(String idCocheP1, String idCocheP2, String caracteristica) {
		int posCocheP1 = searchCarta(manoP1, idCocheP1);
		int posCocheP2 = searchCarta(manoP2, idCocheP2);
		if (posCocheP1 == -1 || posCocheP2 == -1) {
			System.out.println(idCocheP1 + idCocheP2);
			return -1;
		}

		setLaslPlayP1id(idCocheP1);
		setLastPlayP2id(idCocheP2);

		Coche cocheP1 = manoP1.get(posCocheP1);
		Coche cocheP2 = manoP2.get(posCocheP2);
		this.ronda++;
		if (this.p1First)
			this.p1First = false;
		else
			this.p1First = true;

		switch (caracteristica) {

		case "MOTOR":
			if (cocheP1.getMotor() > cocheP2.getMotor()) {
				playResult(PlayResult.P1, posCocheP1, posCocheP2);
				return 0;
			} else if (cocheP1.getMotor() < cocheP2.getMotor()) {
				playResult(PlayResult.P2, posCocheP1, posCocheP2);
				return 1;
			} else {
				playResult(PlayResult.EMPATE, posCocheP1, posCocheP2);
				return 2;
			}

		case "CILINDROS":
			if (cocheP1.getCilindros() > cocheP2.getCilindros()) {
				playResult(PlayResult.P1, posCocheP1, posCocheP2);
				return 0;
			} else if (cocheP1.getCilindros() < cocheP2.getCilindros()) {
				playResult(PlayResult.P2, posCocheP1, posCocheP2);
				return 1;
			} else {
				playResult(PlayResult.EMPATE, posCocheP1, posCocheP2);
				return 2;
			}

		case "POTENCIA":
			if (cocheP1.getPotencia() > cocheP2.getPotencia()) {
				playResult(PlayResult.P1, posCocheP1, posCocheP2);
				return 0;
			} else if (cocheP1.getPotencia() < cocheP2.getPotencia()) {
				playResult(PlayResult.P2, posCocheP1, posCocheP2);
				return 1;
			} else {
				playResult(PlayResult.EMPATE, posCocheP1, posCocheP2);
				return 2;
			}

		case "REVXMIN":
			if (cocheP1.getRevXmin() < cocheP2.getRevXmin()) {
				playResult(PlayResult.P1, posCocheP1, posCocheP2);
				return 0;
			} else if (cocheP1.getRevXmin() > cocheP2.getRevXmin()) {
				playResult(PlayResult.P2, posCocheP1, posCocheP2);
				return 1;
			} else {
				playResult(PlayResult.EMPATE, posCocheP1, posCocheP2);
				return 2;
			}

		case "VELOCIDAD":
			if (cocheP1.getVelocidad() > cocheP2.getVelocidad()) {
				playResult(PlayResult.P1, posCocheP1, posCocheP2);
				return 0;
			} else if (cocheP1.getVelocidad() < cocheP2.getVelocidad()) {
				playResult(PlayResult.P2, posCocheP1, posCocheP2);
				return 1;
			} else {
				playResult(PlayResult.EMPATE, posCocheP1, posCocheP2);
				return 2;
			}

		case "COSTE100KM":
			if (cocheP1.getConsAt100Km() < cocheP2.getConsAt100Km()) {
				playResult(PlayResult.P1, posCocheP1, posCocheP2);
				return 0;
			} else if (cocheP1.getConsAt100Km() > cocheP2.getConsAt100Km()) {
				playResult(PlayResult.P2, posCocheP1, posCocheP2);
				return 1;
			} else {
				playResult(PlayResult.EMPATE, posCocheP1, posCocheP2);
				return 2;
			}

		default:
			System.out.println(caracteristica);
			return -2;
		}

	}

	/**
	 * Metodo que prepara las manos para la siguiente ronda y arregla el marcador de
	 * victorias
	 * 
	 */

	private void playResult(PlayResult winner, int posCocheP1, int posCocheP2) {
		manoP1.remove(posCocheP1);
		manoP2.remove(posCocheP2);
		switch (winner) {
		case P1:
			vPlayer++;
			break;

		case P2:
			vCpu++;
			break;

		case EMPATE:
			empates++;
			break;

		default:
			ronda--;
			System.out.println("error en el metodo Partida.playResult");
			break;
		}
	}

	private int searchCarta(List<Coche> mano, String idCoche) {
		for (int i = 0; i < mano.size(); i++) {
			if (mano.get(i).getId().equals(idCoche))
				return i;
		}
		return -1;
	}

}
