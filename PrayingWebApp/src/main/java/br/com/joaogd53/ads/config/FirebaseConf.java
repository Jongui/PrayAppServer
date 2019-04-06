package br.com.joaogd53.ads.config;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;

import br.com.joaogd53.ads.exceptions.FirebaseTokenException;

public class FirebaseConf {

	private static FirebaseConf firebaseConf;
	private FirebaseApp defaultApp;

	private FirebaseConf(ApplicationContext context) {
		Resource resource = context.getResource(
				"classpath:br/com/joaogd53/ads/config/prayingapp-76292-firebase-adminsdk-wun3x-0ec211382d.json");
		try {
			InputStream serviceAccount = resource.getInputStream();
			FirebaseOptions options = new FirebaseOptions.Builder()
					.setCredentials(GoogleCredentials.fromStream(serviceAccount))
					.setDatabaseUrl("https://prayingapp-76292.firebaseio.com").build();

			this.defaultApp = FirebaseApp.initializeApp(options);
			System.out.println("Default App:\t" + this.defaultApp.getName());
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public static FirebaseConf getInstance(ApplicationContext context) {
		if (firebaseConf == null)
			firebaseConf = new FirebaseConf(context);
		return firebaseConf;

	}

	public FirebaseAuth getDefaultAuth() {
		return FirebaseAuth.getInstance();
	}

	public void validateToken(String token) throws FirebaseTokenException {
		FirebaseToken decodedToken;
		try {
			String[] idToken = token.split(" ");
			decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken[1]);
			String uid = decodedToken.getUid();
			System.out.println("uid:\t" + uid);
		} catch (FirebaseAuthException | IndexOutOfBoundsException e) {
			throw new FirebaseTokenException("User or email not valid");
		}

	}

}
