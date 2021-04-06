package ecommerce;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

public class SessionManager {
	
	// Scadenza (rappresentata in secondi): 60 minuti
	public static final int EXPIRE_TIME = 60 * 60;
	
	private HashMap<UUID, Long> sessions;
	
	public SessionManager() {
		this.sessions = new HashMap<UUID, Long>();
	}
	
	public String createSession() {
		UUID uuid = UUID.randomUUID();
		sessions.put(uuid, (new Date()).getTime());
		return uuid.toString();
	}
	
	public boolean isValidSession(String sessionUUID) {
		try {
			UUID uuid = UUID.fromString(sessionUUID);
			boolean expired = sessionExpired(uuid);
			if (expired) sessions.remove(uuid);
			return !expired;
		} catch (IllegalArgumentException e) {
			System.out.println(sessionUUID + " is not a valid UUID");
			return false;
		}
	}
	
	private boolean sessionExpired(UUID sessionUUID) {
		if (sessions.containsKey(sessionUUID)) {
			Instant sessionCreationTime = new Date(sessions.get(sessionUUID)).toInstant();
			Instant now = Instant.now();
			
			Instant expireInstant = sessionCreationTime.plusSeconds(EXPIRE_TIME);
			return expireInstant.isBefore(now);
		}
		return true;
	}
}
