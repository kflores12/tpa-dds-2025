package ar.edu.utn.frba.dds.server;

import ar.edu.utn.frba.dds.model.entities.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class GestorTokens {

  private static final String SECRET_KEY = "abc";
  private static final Algorithm ALGORITHM = Algorithm.HMAC256(SECRET_KEY);
  private static final JWTVerifier VERIFIER = JWT.require(ALGORITHM).build();


  public String createToken(User user) {
    return JWT.create()
        .withSubject(user.getUsername()) //
        .withClaim("role", user.getRole())
        .withClaim("userId", user.getId())
        .withIssuedAt(Instant.now())
        .withExpiresAt(Instant.now().plus(2, ChronoUnit.HOURS))
        .sign(ALGORITHM);
  }

  public DecodedJWT validateToken(String token) {
    if (token == null || token.isEmpty()) {
      return null;
    }
    try {
      return VERIFIER.verify(token);
    } catch (JWTVerificationException exception) {
      return null;
    }
  }
}