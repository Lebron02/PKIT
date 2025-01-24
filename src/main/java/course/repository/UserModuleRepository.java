package course.repository;

import course.model.UserModule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserModuleRepository extends JpaRepository<UserModule, Integer> {

    // Możesz tu dodać własne metody, np. sprawdzenie, czy użytkownik ukończył wszystkie moduły
    // lub pobranie modułu dla danego userId i moduleId, jeśli chcesz zaktualizować status.

    // Przykład: znaleźć UserModule po user_id i module_id
    UserModule findByUserUserIdAndModuleModuleId(int userId, int moduleId);
}