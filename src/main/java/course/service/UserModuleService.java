package course.service;

import course.model.Module;
import course.model.User;
import course.model.UserModule;
import course.repository.UserModuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserModuleService {

    private final UserModuleRepository userModuleRepository;

    @Autowired
    public UserModuleService(UserModuleRepository userModuleRepository) {
        this.userModuleRepository = userModuleRepository;
    }

    /**
     * Oznacz dany moduł jako wykonany przez użytkownika.
     */
    public void markModuleAsDone(User user, Module module) {
        // Spróbuj znaleźć istniejący rekord w user_modules
        UserModule userModule = userModuleRepository
                .findByUserUserIdAndModuleModuleId(user.getUserId(), module.getModuleId());

        if (userModule == null) {
            // Jeśli nie istnieje, utwórz
            userModule = new UserModule();
            userModule.setUser(user);
            userModule.setModule(module);
        }

        userModule.setStatus("Wykonano");
        userModuleRepository.save(userModule);
    }

    public UserModule getOrCreateUserModule(User user, Module module) {
        UserModule userModule = userModuleRepository.findByUserUserIdAndModuleModuleId(user.getUserId(), module.getModuleId());
        if (userModule == null) {
            userModule = new UserModule();
            userModule.setUser(user);
            userModule.setModule(module);
            userModule.setStatus("Niewykonano");
            userModuleRepository.save(userModule);
        }
        return userModule;
    }

    /**
     * Sprawdź, czy wszystkie moduły w danym kursie są wykonane
     * (np. w prosty sposób, licząc w innym miejscu).
     * Ewentualnie można tu zaimplementować metodę, która sprawdza liczbę
     * modułów w kursie i liczbę "Wykonano" w user_modules.
     */
}