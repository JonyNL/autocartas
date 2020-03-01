package modelos;

import java.rmi.server.UID;

public class Sesion {
	
	private UID idSession;
	private User usuario;
	private Partida partida;
	
	public Sesion() {}
	
	public Sesion(User usuario) {
		
		idSession = new UID();
		this.usuario = usuario;
		partida = null;
		
	}
	
	/*  GET & SET idSession  */

	public UID getIdSession() {
		return idSession;
	}

	public void setIdSession(UID idSession) {
		this.idSession = idSession;
	}

	/*  GET & SET usuario  */

	public User getUsuario() {
		return usuario;
	}

	public void setUsuario(User usuario) {
		this.usuario = usuario;
	}

	/*  GET & SET partida  */

	public Partida getPartida() {
		return partida;
	}

	public void setPartida(Partida partida) {
		this.partida = partida;
	}
	
}
