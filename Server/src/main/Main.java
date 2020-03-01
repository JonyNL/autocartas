package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import metodos.Metodos;
import modelos.Coche;
import modelos.Partida;
import modelos.ResPartida;
import modelos.Sesion;
import modelos.User;

@Path("/restcoches")

public class Main {

	static Connection conexion;

	static List<Sesion> sesiones = new ArrayList<>();
	static List<Partida> partidas = new ArrayList<>();
	static User loggedUser = null;
	Gson g = new Gson();

	/**
	 * Conectar MySQL.
	 *
	 * @return the connection
	 */
	public static Connection conectarMySQL() {
		String urlConexion = "jdbc:mysql://localhost:3306/autocartas";
		String usuario = "root";
		String passwd = "root";
		Connection conexion;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");

			conexion = DriverManager.getConnection(urlConexion, usuario, passwd);
			if (conexion != null)
				System.out.println("OK-BBDD");
			return conexion;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Metodo para buscar la posicion de una partida a partir del ID de una sesion
	 * 
	 * @param id Id de la partida o sesion para buscar una partida
	 * @return int con la posicion
	 */
	private int searchPartida(String id) {
		for (int i = 0; i < partidas.size(); i++) {
			if (partidas.get(i).getSesion().getIdSession().toString().equals(id))
				return i;
			if (partidas.get(i).getIdGame().toString().equals(id))
				return i;
		}
		return -1;
	}

	/**
	 * Metodo para obtener la posicion de una sesion a traves de un usuario
	 * 
	 * @param user usuario de quien se busca la sesion
	 * @return int con la posicion
	 */
	private int searchSesion(User user) {
		for (int i = 0; i < sesiones.size(); i++) {
			if (sesiones.get(i).getUsuario().equals(user))
				return i;
		}

		return -1;
	}

	/**
	 * Metodo para obtener la posicion de una sesion a traves de su ID
	 * 
	 * @param idSesion para buscar una sesion
	 * @return int con la posicion
	 */
	private int searchSesion(String idSesion) {
		for (int i = 0; i < sesiones.size(); i++) {
			if (sesiones.get(i).getIdSession().toString().equals(idSesion))
				return i;
		}
		return -1;
	}

	/**
	 * @apiNote ENDPOINT POST="/login"
	 * 
	 * @param user   usuario que va a iniciar sesion
	 * @param passwd password del usuario
	 * @return un Response con el id de la sesion creada
	 */
	@POST
	@Path("/login")
	@Produces(MediaType.APPLICATION_JSON)
	public Response login(@FormParam("user") String u, @FormParam("passwd") String passwd) {

		conexion = conectarMySQL();

		String sql = "SELECT * FROM Users WHERE user = ? AND passwd = ?";
		if (loggedUser == null)
			try {
				PreparedStatement pst = conexion.prepareStatement(sql);
				pst.setString(1, u);
				pst.setString(2, passwd);
				ResultSet rs = pst.executeQuery();
				if (rs.first()) {
					loggedUser = new User(rs.getString(1), rs.getString(2), rs.getString(3), rs.getInt(4), rs.getInt(5),
							rs.getInt(6));

					int posSesion = searchSesion(loggedUser);

					if (posSesion != -1) {
						sesiones.remove(posSesion);
					}

					Sesion s = new Sesion(loggedUser);
					sesiones.add(s);
					conexion.close();
					// Gson g = new Gson();
					return Response.status(Response.Status.OK).entity(g.toJson(s.getIdSession().toString())).build();
				} else {
					conexion.close();
					return Response.status(Response.Status.BAD_REQUEST).entity("Error:no existe usr").build();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		loggedUser = null;
		return Response.status(Response.Status.PRECONDITION_FAILED).entity("login:error").build();
	}

	/**
	 * @apiNote ENDPOINT POST="/logout"
	 * 
	 * @param user     usuario con sesion iniciada
	 * @param idSesion id de la sesion iniciada
	 * @return un Response con el resultado de la operacion en String
	 */
	@POST
	@Path("/logout")
	@Produces(MediaType.APPLICATION_JSON)
	public Response logout(@FormParam("user") String user, @FormParam("idSesion") String idSesion) {
		// Gson g = new Gson();
		int posSesion = searchSesion(idSesion);
		if (posSesion != -1) {
			sesiones.remove(posSesion);
			loggedUser = null;
			return Response.status(Response.Status.OK).entity(g.toJson("user " + user + " logged out")).build();
		}

		return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("logout:error").build();
	}

	/**
	 * @apiNote ENDPOINT POST="/newgame"
	 * 
	 * @param idSesion id de la sesion que va a recibir una nueva partida
	 * @return un Response con el la partida almacenada en un Json
	 */
	@POST
	@Path("/newgame")
	@Produces(MediaType.APPLICATION_JSON)

	public Response getNewGame(@FormParam("idSesion") String idSesion) {
		// Gson g = new Gson();
		int posSesion = searchSesion(idSesion);
		if (posSesion != -1) {
			Sesion s = sesiones.get(posSesion);
			int posPartida = searchPartida(idSesion);
			if (posPartida != -1) {
				Partida p = partidas.get(posPartida);
				ResPartida res = new ResPartida(s.getIdSession().toString(), p.getLaslPlayP1id(), p.getLastPlayP2id(),
						p.getCaracteristica(), p.getRonda(), p.getvPlayer(), p.getEmpates(), p.getvCpu(),
						p.getManoP1());
				return Response.status(Response.Status.OK).entity(g.toJson(res)).build();
			}
			Partida p = new Partida(s, s.getUsuario());
			ResPartida partida = new ResPartida(s.getIdSession().toString(), null, null, null, p.getRonda(), 0, 0, 0,
					p.getManoP1());
			System.out.println(p.getIdGame().toString());
			partidas.add(p);
			String datosPartida = g.toJson(partida);
			return Response.status(Response.Status.CREATED).entity(datosPartida).build();
		}
		return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
	}

	/**
	 * @apiNote ENDPOINT POST="/resetgame"
	 * 
	 * @param idSesion sesion que va a resetear su partida
	 * @return un Response con la nueva partida almacenada en un Json
	 */
	@POST
	@Path("/resetgame")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getResetGame(@FormParam("idSesion") String idSesion) {
		int posPartida = searchPartida(idSesion);
		if (posPartida != -1) {
			partidas.remove(posPartida);
			System.out.println("Partida eliminada.");
		}
		Response r = getNewGame(idSesion);
		return Response.status(r.getStatus()).entity(r.getEntity()).build();

	}

	/**
	 * @apiNote ENDPOINT POST="/raffle"
	 * 
	 * @param idSesion id de la sesion para la que va a hacerse el raffle
	 * @return un Response con la partida almacenada en Json con los valores
	 *         modificados segun el resultado
	 */
	@POST
	@Path("/raffle")
	@Produces(MediaType.APPLICATION_JSON)
	public Response makeRaffle(@FormParam("idSesion") String idSesion) {
		int posSesion = searchSesion(idSesion), posPartida = searchPartida(idSesion);
		if (posSesion != -1 && posPartida != -1) {

			// Sorteamos el turno con un aleatorio entre 0 y 1
			// en 0 empezará el jugador - en 1 empezará el cpu

			int ordenPlay = Metodos.randomNumber(0, 1);
			// Gson g = new Gson();
			String primero;
			if (ordenPlay == 1) {
				partidas.get(posPartida).setP1First(false);
				primero = "P2";
			} else {
				partidas.get(posPartida).setP1First(true);
				primero = "P1";
			}
			return Response.status(Response.Status.OK).entity(g.toJson(primero)).build();

		}
		return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(g.toJson("raffle:error")).build();
	}

	/**
	 * @apiNote ENDPOINT POST="/playcard"
	 * 
	 * @param idSesion id de la sesion en la que se ha realizado la jugada
	 * @param idCocheP1 id de la carta que ha jugado el jugador
	 * @param caracteristica caracterista por la que compiten los jugadores
	 * @return un Response con la partida despues de la jugada almacenada en un Json
	 */
	@POST
	@Path("/playcard")
	@Produces(MediaType.APPLICATION_JSON)
	public Response playCard(@FormParam("idSesion") String idSesion, @FormParam("idCocheP1") String idCocheP1,
			@FormParam("Caracteristica") String caracteristica) {
		int posPartida = searchPartida(idSesion);

		// Si posPartida no vale -1 quiere decir que existe una partida
		if (posPartida != -1) {
			Partida p = partidas.get(posPartida);
			String idCocheP2 = p.getIdCocheP2();

			if (p.isP1First())
				p.setCaracteristica(caracteristica);

			if (idCocheP2 == null)
				idCocheP2 = p.jugadaCPU();

			if (p.getCaracteristica() == null) {
				p.setCaracteristica(p.randomCaracteristica());
			}

			// Jugamos una partida en la que guardamos el resultado como un entero:
			// 0 -> Gana P1; 1 -> Gana P2; 2 -> Empate

			int resPartida = p.playRound(idCocheP1, idCocheP2, p.getCaracteristica());

			// Si resPartida vale -1 algo ha ido mal al jugar la ronda
			if (resPartida == -1)
				return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("playcard.playround:error")
						.build();

			// Si resPartida vale -2 algo ha ido mal en referencia a la categoria
			if (resPartida == -2)
				return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
						.entity("playcard.playround.switchmethod:error").build();

			ResPartida partidaRes = new ResPartida(p.getSesion().getIdSession().toString(), p.getLaslPlayP1id(),
					p.getLastPlayP2id(), p.getCaracteristica(), p.getRonda(), p.getvPlayer(), p.getEmpates(),
					p.getvCpu(), p.getManoP1());
			partidaRes.setPlayerFirst(p.isP1First());

			p.setIdCocheP2(null);
			p.setCaracteristica(null);
			if (p.getRonda() > 6) {
				if (partidaRes.getvPlayer() > partidaRes.getvCpu()) {
					loggedUser.setpGanadas(loggedUser.getpGanadas() + 1);
					System.out.println(
							editUser(loggedUser.getUser(), "pGanadas", String.valueOf(loggedUser.getpGanadas())));
				} else if (partidaRes.getvPlayer() < partidaRes.getvCpu()) {
					loggedUser.setpPerdidas(loggedUser.getpPerdidas() + 1);
					System.out.println(
							editUser(loggedUser.getUser(), "pPerdidas", String.valueOf(loggedUser.getpPerdidas())));
				} else {
					loggedUser.setpEmpatadas(loggedUser.getpEmpatadas() + 1);
					System.out.println(
							editUser(loggedUser.getUser(), "pEmpatadas", String.valueOf(loggedUser.getpEmpatadas())));
				}

				if (partidas.remove(partidas.get(posPartida)))
					partidaRes.setDeletedGame(true);
			}

			// Gson g = new Gson();
			return Response.status(Response.Status.OK).entity(g.toJson(partidaRes)).build();
		}

		return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(g.toJson("playcard:error")).build();
	}

	/**
	 * @apiNote ENDPOINT POST="/ready"
	 * 
	 * @param idSesion id de la sesion que avisa que esta listo para jugar
	 * @return un Response con la partida preparada para que el jugador realice su jugada
	 */
	@POST
	@Path("/ready")
	@Produces(MediaType.APPLICATION_JSON)
	public Response ready(@FormParam("idSesion") String idSesion) {
		int posPartida = searchPartida(idSesion);
		if (posPartida != -1) {
			Partida p = partidas.get(posPartida);
			p.setIdCocheP2(p.jugadaCPU());
			p.setCaracteristica(p.randomCaracteristica());
			ResPartida partidaRes = new ResPartida(p.getSesion().getIdSession().toString(), null, p.getIdCocheP2(),
					p.getCaracteristica(), p.getRonda(), p.getvPlayer(), p.getEmpates(), p.getvCpu(), p.getManoP1());
			partidaRes.setPlayerFirst(p.isP1First());
			// Gson g = new Gson();
			return Response.status(Response.Status.OK).entity(g.toJson(partidaRes)).build();
		}
		return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
	}

	/**
	 * @apiNote ENDPOINT GET="/stats"
	 * 
	 * @return un Response con la lista de usuarios ordenada segun sus estadisticas en formato Json
	 */
	@SuppressWarnings("unchecked")
	@GET
	@Path("/stats")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getStats() {
		List<User> sortedUsers = new ArrayList<>();
		// Gson g = new Gson();
		sortedUsers = (List<User>) g.fromJson((String) readUsers().getEntity(), new TypeToken<ArrayList<User>>() {
		}.getType());

		if (!sortedUsers.isEmpty()) {
			Collections.sort(sortedUsers);
			return Response.status(Response.Status.OK).entity(g.toJson(sortedUsers)).build();
		}

		return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
	}

	/** 
	 * ========>CRUD<========
	 * 
	 */

	// CRUD para la tabla Users

	/**
	 * @apiNote ENDPOINT GET="/users" 
	 * 
	 * @return un Response con el listado de los usuarios registrados en un Json
	 */
	@GET
	@Path("/users")
	@Produces(MediaType.APPLICATION_JSON)
	public Response readUsers() {
		conexion = conectarMySQL();

		String sql = "SELECT * FROM Users";
		try {
			Statement s = conexion.createStatement();
			ResultSet rs = s.executeQuery(sql);
			if (rs.first()) {
				List<User> users = new ArrayList<>();
				do {
					users.add(new User(rs.getString(1), rs.getString(2), rs.getString(3), rs.getInt(4), rs.getInt(5),
							rs.getInt(6)));

				} while (rs.next());
				rs.close();
				conexion.close();
				return Response.status(Response.Status.OK).entity(g.toJson(users)).build();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
	}

	/**
	 * @apiNote ENDPOINT GET="/users/{user}" 
	 * 
	 * @return un Response con el usuario especificado en un Json en caso de encontrarlo
	 */
	@GET
	@Path("/users/{user}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response readUser(@PathParam("user") String user) {
		conexion = conectarMySQL();

		String sql = "SELECT * FROM Users WHERE user = ?";
		try {
			PreparedStatement ps = conexion.prepareStatement(sql);
			ps.setString(1, user);
			ResultSet rs = ps.executeQuery();
			if (rs.first()) {
				User usr = new User(rs.getString(1), rs.getString(2), rs.getString(3), rs.getInt(4), rs.getInt(5),
						rs.getInt(6));
				rs.close();
				conexion.close();
				// Gson g = new Gson();
				return Response.status(Response.Status.OK).entity(g.toJson(usr)).build();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
	}

	/**
	 * @apiNote ENDPOINT POST="/users" 
	 * 
	 * @param user nombre de usuario para el usuario a crear
	 * @param nombre nombre de pila del usuario que se va a registrar
	 * @param passwd password que va a usar el usuario para iniciar sesion
	 * @return un Response con el resultado de la operacion en un String
	 */
	@POST
	@Path("/users")
	@Produces(MediaType.APPLICATION_JSON)
	public Response createUser(@FormParam("user") String user, @FormParam("nombre") String nombre,
			@FormParam("passwd") String passwd) {

		conexion = conectarMySQL();
		// Gson g = new Gson();

		String sql = "INSERT INTO Users VALUES (?, ?, ?, ?, ?, ?);";

		try {
			PreparedStatement ps = conexion.prepareStatement(sql);
			ps.setString(1, user);
			ps.setString(2, nombre);
			ps.setString(3, passwd);
			ps.setInt(4, 0);
			ps.setInt(5, 0);
			ps.setInt(6, 0);
			int insertResult = ps.executeUpdate();
			conexion.close();
			if (insertResult > 0)
				return Response.status(Response.Status.OK).entity(g.toJson("OK")).build();
		} catch (SQLIntegrityConstraintViolationException ex) {
			ex.printStackTrace();
			return Response.status(Response.Status.NOT_ACCEPTABLE).build();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
	}

	/**
	 * @apiNote ENDPOINT PUT="/users/{user}/{attr}/{newvalue}" 
	 * 
	 * @param user usuario a editar
	 * @param attr atributo o campo a editar
	 * @param newvalue nuevo valor para el campo
	 * @return un Response con el resultado de la operacion en String
	 */
	@PUT
	@Path("/users/{user}/{attr}/{newvalue}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response editUser(@PathParam("user") String user, @PathParam("attr") String attr,
			@PathParam("newvalue") String newvalue) {

		String sql = "UPDATE Users SET " + attr + " = ? WHERE user = ?";

		conexion = conectarMySQL();

		try {
			PreparedStatement ps = conexion.prepareStatement(sql);
			ps.setString(2, user);
			switch (attr) {
			case "user":
			case "nombre":
			case "passwd":
				ps.setString(1, newvalue);
				break;
			case "pGanadas":
			case "pEmpatadas":
			case "pPerdidas":
				ps.setInt(1, Integer.parseInt(newvalue));
				break;
			default:
				break;
			}

			int result = ps.executeUpdate();
			if (result > 0)
				return Response.status(Response.Status.OK).entity(g.toJson("OK")).build();
		} catch (SQLIntegrityConstraintViolationException ex) {
			ex.printStackTrace();
			return Response.status(Response.Status.NOT_ACCEPTABLE).build();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
	}

	/**
	 * @apiNote ENDPOINT DELETE="/users/{user}" 
	 * 
	 * @param user usuario a eliminar
	 * @return un Response con el resultado de la operacion en String
	 */
	@DELETE
	@Path("/users/{user}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteUser(@PathParam("user") String user) {

		// Gson g = new Gson();

		String sql = "DELETE FROM Users WHERE user = ?";

		conexion = conectarMySQL();
		PreparedStatement ps;
		try {
			ps = conexion.prepareStatement(sql);
			ps.setString(1, user);
			int deleteResult = ps.executeUpdate();
			ps.close();
			if (deleteResult > 0)
				return Response.status(Response.Status.OK).entity(g.toJson("OK")).build();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
	}

	// CRUD para la tabla coches

	/**
	 * @apiNote ENDPOINT GET="/coches" 
	 * 
	 * @return un Response con el listado de coches de la base de datos en Json
	 */
	@GET
	@Path("/coches")
	@Produces(MediaType.APPLICATION_JSON)
	public Response readCoches() {
		conexion = conectarMySQL();

		String sql = "SELECT * FROM Coches";
		try {
			Statement ps = conexion.createStatement();
			ResultSet rs = ps.executeQuery(sql);
			if (rs.first()) {
				List<Coche> coches = new ArrayList<>();
				do {
					coches.add(new Coche(rs.getString(1), rs.getString(2), rs.getString(3), rs.getInt(4), rs.getInt(5),
							rs.getInt(6), rs.getInt(7), rs.getInt(8), rs.getDouble(9)));
				} while (rs.next());
				rs.close();
				conexion.close();
				// Gson g = new Gson();
				return Response.status(Response.Status.OK).entity(g.toJson(coches)).build();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
	}

	/**
	 * @apiNote ENDPOINT GET="/coches/{id}" 
	 * 
	 * @param id id del coche a buscar
	 * @return un Response con el coche correspondiente al id en Json en caso de encontrarlo 
	 */
	@GET
	@Path("/coches/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response readCoche(@PathParam("id") String id) {
		conexion = conectarMySQL();

		String sql = "SELECT * FROM Coches WHERE id = ?";
		try {
			PreparedStatement ps = conexion.prepareStatement(sql);
			ps.setString(1, id);
			ResultSet rs = ps.executeQuery();
			if (rs.first()) {
				Coche c = new Coche(rs.getString(1), rs.getString(2), rs.getString(3), rs.getInt(4), rs.getInt(5),
						rs.getInt(6), rs.getInt(7), rs.getInt(8), rs.getDouble(9));
				rs.close();
				conexion.close();
				// Gson g = new Gson();
				return Response.status(Response.Status.OK).entity(g.toJson(c)).build();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
	}

	/**
	 * @apiNote ENDPOINT POST="/coches" 
	 * 
	 * @param id id del coche a crear 
	 * @param modelo del coche a crear
	 * @param pais del coche a crear
	 * @param motor del coche a crear
	 * @param cilindros del coche a crear
	 * @param potencia del coche a crear
	 * @param revxmin del coche a crear
	 * @param velocidad del coche a crear
	 * @param consAt100km del coche a crear
	 * @return un Response con el resultado de la operacion en String
	 */
	@POST
	@Path("/coches")
	@Produces(MediaType.APPLICATION_JSON)
	public Response createCoche(@FormParam("id") String id, @FormParam("modelo") String modelo,
			@FormParam("pais") String pais, @FormParam("motor") int motor, @FormParam("cilindros") int cilindros,
			@FormParam("potencia") int potencia, @FormParam("revxmin") int revxmin,
			@FormParam("velocidad") int velocidad, @FormParam("consAt100km") int consAt100km) {

		// Gson g = new Gson();

		conexion = conectarMySQL();

		String sql = "INSERT INTO Coches VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";

		try {
			PreparedStatement ps = conexion.prepareStatement(sql);
			ps.setString(1, id);
			ps.setString(2, modelo);
			ps.setString(3, pais);
			ps.setInt(4, motor);
			ps.setInt(5, cilindros);
			ps.setInt(6, potencia);
			ps.setInt(7, revxmin);
			ps.setInt(8, velocidad);
			ps.setInt(9, consAt100km);
			int insertResult = ps.executeUpdate();
			conexion.close();
			if (insertResult > 0)
				return Response.status(Response.Status.OK).entity(g.toJson("OK")).build();
		} catch (SQLIntegrityConstraintViolationException ex) {
			ex.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Response.status(Response.Status.NOT_ACCEPTABLE).build();
		}
		return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
	}

	/**
	 * @apiNote ENDPOINT PUT="/coches/{idCoche}/{attr}/{newvalue}" 
	 * 
	 * @param idCoche id del coche a editar
	 * @param attr atributo o campo a editar
	 * @param newvalue nuevo valor para el campo
	 * @return un Response con el resultado de la operacion en String
	 */
	@PUT
	@Path("/coches/{idCoche}/{attr}/{newvalue}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response editCoche(@PathParam("idCoche") String idCoche, @PathParam("attr") String attr,
			@PathParam("newvalue") String newvalue) {

		String sql = "UPDATE Coches SET " + attr + " = ? WHERE id = ?";

		conexion = conectarMySQL();

		try {
			PreparedStatement ps = conexion.prepareStatement(sql);
			ps.setString(2, idCoche);
			switch (attr) {
			case "id":
			case "modelo":
			case "pais":
				ps.setString(1, newvalue);
				break;
			case "motor":
			case "cilindros":
			case "potencia":
			case "revxmin":
			case "velocidad":
				ps.setInt(1, Integer.parseInt(newvalue));
				break;
			case "consAt100km":
				ps.setDouble(1, Double.parseDouble(newvalue));
			default:
				break;
			}

			int result = ps.executeUpdate();
			if (result > 0)
				return Response.status(Response.Status.OK).entity(g.toJson("OK")).build();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
	}
	
	/**
	 * @apiNote ENDPOINT DELETE="/coches/{idcoche}" 
	 * 
	 * @param idCoche id del coche a eliminar
	 * @return un Response con el resultado de la operacion en String 
	 */
	@DELETE
	@Path("/coches/{idCoche}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deletecoche(@PathParam("idCoche") String idCoche) {

		// Gson g = new Gson();

		String sql = "DELETE FROM Coches WHERE id = ?";

		conexion = conectarMySQL();
		PreparedStatement ps;
		try {
			ps = conexion.prepareStatement(sql);
			ps.setString(1, idCoche);
			int deleteResult = ps.executeUpdate();
			ps.close();
			if (deleteResult > 0)
				return Response.status(Response.Status.OK).entity(g.toJson("OK")).build();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
	}

}
