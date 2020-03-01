package modelos;

public class User implements Comparable<User>{
	
	private String user;
	private String nombre;
	private String passwd;
	private int pGanadas;
	private int pEmpatadas;
	private int pPerdidas;
	
	public User() {}

	/*  Constructor para usuario no registrado en BD  */
	
	public User(String user, String nombre, String passwd) {
		super();
		this.user = user;
		this.nombre = nombre;
		this.passwd = passwd;
		this.pGanadas = 0;
		this.pEmpatadas = 0;
		this.pPerdidas = 0;
	}
	
	/*  Constructor para usuario ya registrado en BD  */
	
	public User(String user, String nombre, String passwd, int pGanadas, int pEmpatadas, int pPerdidas) {
		super();
		this.user = user;
		this.nombre = nombre;
		this.passwd = passwd;
		this.pGanadas = pGanadas;
		this.pEmpatadas = pEmpatadas;
		this.pPerdidas = pPerdidas;
	}

	/*	GET &  SET User  */
	
	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	/*	GET &  SET Nombre  */
	
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/*	GET &  SET Passwd  */
	
	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	/*	GET &  SET pGanadas  */
	
	public int getpGanadas() {
		return pGanadas;
	}

	public void setpGanadas(int pGanadas) {
		this.pGanadas = pGanadas;
	}
	
	/*	GET &  SET pEmpatadas  */

	public int getpEmpatadas() {
		return pEmpatadas;
	}

	public void setpEmpatadas(int pEmpatadas) {
		this.pEmpatadas = pEmpatadas;
	}
	
	/*	GET &  SET pPerdidas  */

	public int getpPerdidas() {
		return pPerdidas;
	}

	public void setpPerdidas(int pPerdidas) {
		this.pPerdidas = pPerdidas;
	}
	
	@Override
	public String toString() {
		return "User: " + this.user +"\tNombre: "+ this.nombre + "\tPasswd: " + this.passwd;
	}

	@Override
	public int compareTo(User o) {
		int res = 0;
		if (this.pGanadas>o.getpGanadas())
			res = -1;
		else if (this.pGanadas<o.getpGanadas())
			res = 1;
		else if (this.pPerdidas<o.getpPerdidas())
			res = -1;
		else if (this.pPerdidas>o.getpPerdidas())
			res = 1;
		else if (this.pEmpatadas<o.getpEmpatadas())
			res= -1;
		else if (this.pEmpatadas>o.getpEmpatadas())
			res = 1;
		
		return res;
	}	
	
}
