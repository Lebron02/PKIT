package course.service;

import course.model.User;
import course.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;

    @Autowired
    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Rejestracja nowego użytkownika.
     */
    public User registerUser(String email, String password,
                             String firstName, String lastName) {
        // Sprawdź czy użytkownik o podanym emailu już istnieje
        if (userRepository.findByEmail(email) != null) {
            throw new RuntimeException("Użytkownik o podanym e-mailu już istnieje: " + email);
        }

        // Utwórz nowy obiekt User
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setFirstName(firstName);
        user.setLastName(lastName);

        // Zapisz w bazie
        return userRepository.save(user);
    }

    /**
     * Logowanie użytkownika na podstawie emaila i hasła.
     */
    public User loginUser(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("Użytkownik nie istnieje: " + email);
        }

        // Porównaj hasło (w praktyce hashowanie)
        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("Niepoprawne hasło!");
        }

        return user; // zalogowany użytkownik
    }

    /**
     * Pobierz użytkownika po ID.
     */
    public User getUserById(int userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono użytkownika o ID = " + userId));
    }
}