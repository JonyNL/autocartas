package modelos;

import java.io.Serializable;
import java.util.ArrayList;

public class ResPartida implements Serializable{

	private String idSesion;
	private String playP1id;
	private String playP2id;
	private String caracteristica;
	private boolean playerFirst;
	private boolean deletedGame;
	private int ronda;
	private int vPlayer;
	private int empates;
	private int vCpu;
	private ArrayList<Coche> manoP1;

	public ResPartida() {

	}

	public ResPartida(String idSesion, String playP1id, String playP2id, String caracteristica, int ronda, int vPlayer, int empates,
			int vCpu, ArrayList<Coche> manoP1) {
		this.idSesion = idSesion;
		this.playP1id = playP1id;
		this.playP2id = playP2id;
		this.caracteristica = caracteristica;
		this.playerFirst = true;
		this.deletedGame = false;
		this.ronda = ronda;
		this.vPlayer = vPlayer;
		this.empates = empates;
		this.vCpu = vCpu;
		this.manoP1 = manoP1;
	}

	/* GET & SET idSesion */

	public String getIdSesion() {
		return idSesion;
	}

	public void setIdSesion(String idSesion) {
		this.idSesion = idSesion;
	}

	/* GET & SET Caracteristica */

	public String getCaracteristica() {
		return caracteristica;
	}

	public void setCaracteristica(String caracteristica) {
		this.caracteristica = caracteristica;
	}

	/* GET & SET deletedGame */

	public boolean isDeletedGame() {
		return deletedGame;
	}

	public void setDeletedGame(boolean deletedGame) {
		this.deletedGame = deletedGame;
	}
	
	/* GET & SET playerFirst */

	public boolean isPlayerFirst() {
		return playerFirst;
	}

	public void setPlayerFirst(boolean playerFirst) {
		this.playerFirst = playerFirst;
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

	/* GET & SET vCpu */

	public int getvCpu() {
		return vCpu;
	}

	public void setvCpu(int vCpu) {
		this.vCpu = vCpu;
	}

	/* GET & SET empates */

	public int getEmpates() {
		return empates;
	}

	public void setEmpates(int empates) {
		this.empates = empates;
	}

	/* GET & SET manoP1 */

	public ArrayList<Coche> getManoP1() {
		return manoP1;
	}

	public void setManoP1(ArrayList<Coche> manoP1) {
		this.manoP1 = manoP1;
	}

	/* GET & SET lastPlayP1id */

	public String getPlayP1id() {
		return playP1id;
	}

	public void setLaslPlayP1id(String playP1id) {
		this.playP1id = playP1id;
	}

	/* GET & SET lastPlayP2id */

	public String getPlayP2id() {
		return playP2id;
	}

	public void setLastPlayP2id(String playP2id) {
		this.playP2id = playP2id;
	}

}
