package course.repository;

import course.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    // Własna metoda: znajdź użytkownika po e-mailu
    User findByEmail(String email);
}