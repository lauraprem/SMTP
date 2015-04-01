package util.MsgServer;
import org.apache.log4j.Logger;
public abstract class MsgServer {

	public static Logger logger = Logger.getLogger(MsgServer.class.getName());
	
	public static String msgError(String msg, String getMessage, String user) {
		String msgTemp = UserForm(user) + " Error" + " : " + msg;
		System.out.println(msgTemp);
		logger.error(msgTemp);
		return msgTemp;
	}

	public static String msgWarnning(String msg, String getMessage, String user) {
		String msgTemp = UserForm(user) + " Warnning" + " : " + msg;
		System.out.println(msgTemp);
		logger.warn(msgTemp);
		return msgTemp;
	}

	public static String msgInfo(String obj, String msg, String user) {
		String msgTemp = UserForm(user) + " " + obj + " : " + msg;
		msgTemp = msgTemp.replaceAll("\r\n", "");
		System.out.println(msgTemp);
		logger.info(msgTemp);
		return msgTemp;
	}

	public static String msgConnect(boolean connected, String user) {
		String msgTemp;
		if (connected == true) {
			msgTemp = "Connected : " + UserForm(user);
		} else {
			msgTemp = "Disconnected : " + UserForm(user);
		}
		System.out.println(msgTemp);

		return msgTemp;
	}

	private static String UserForm(String user) {
		if (isUserFormat(user)) {
			user = "[" + user + "]";
		}

		return user;
	}

	/**
	 * permet de savoir si le user est désigné par son nom ou son ip et son port
	 * 
	 * @param user
	 * @return true si nom false si adresse ip et port
	 */
	public static boolean isUserFormat(String user) {
		if (user != null && user != "") {
			return user.split(",localport=").length == 1;
		}
		return false;
	}
}
