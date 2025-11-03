package ar.edu.utn.frba.dds.repositories;

import ar.edu.utn.frba.dds.model.entities.User;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import java.util.List;
import java.util.Optional;
import javax.persistence.NoResultException;

public class RepositorioUsuarios implements WithSimplePersistenceUnit {

  static RepositorioUsuarios instance = new RepositorioUsuarios();

  public static RepositorioUsuarios getInstance() {
    return instance;
  }

  public RepositorioUsuarios() {
  }

  public void save(User user) {
    entityManager().persist(user);
  }

  public Optional<User> findByUsername(String username) {
    try {
      User user = (User) entityManager()
          .createQuery("FROM User u WHERE u.username = :username")
          .setParameter("username", username)
          .getSingleResult();
      return Optional.of(user);
    } catch (NoResultException e) {
      return Optional.empty();
    }
  }

  public Optional<User> findById(Long id) {
    User user = entityManager().find(User.class, id);
    return Optional.ofNullable(user);
  }

  public List<User> obtenerTodos() {
    return entityManager()
        .createQuery("from User", User.class).getResultList();
  }
}