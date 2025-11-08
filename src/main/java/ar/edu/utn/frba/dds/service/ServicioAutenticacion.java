package ar.edu.utn.frba.dds.service;

import ar.edu.utn.frba.dds.model.entities.User;
import ar.edu.utn.frba.dds.repositories.RepositorioUsuarios;
import ar.edu.utn.frba.dds.server.AppRole;
import java.util.Optional;
import org.mindrot.jbcrypt.BCrypt;


public class ServicioAutenticacion {

  private final RepositorioUsuarios repoUsuarios = RepositorioUsuarios.getInstance();


  public boolean registerUser(String username, String password, AppRole role) {

    if (repoUsuarios.findByUsername(username).isPresent()) {
      return false;
    }

    String passwordHash = BCrypt.hashpw(password, BCrypt.gensalt());
    User newUser = new User(username, passwordHash, role);
    repoUsuarios.save(newUser);

    return true;
  }

  public Optional<User> validateLogin(String username, String password) {

    Optional<User> userOpt = repoUsuarios.findByUsername(username);

    if (userOpt.isPresent()) {
      User user = userOpt.get();
      if (BCrypt.checkpw(password, user.getPasswordHash())) {
        return Optional.of(user);
      }
    }

    return Optional.empty();
  }
}