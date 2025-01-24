package course.worker;

import course.model.User;
import course.service.AuthService;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

@Component
public class RegistrationWorker {

    private final AuthService authService;

    @Autowired
    public RegistrationWorker(AuthService authService) {
        this.authService = authService;
    }

    @JobWorker(type = "userRegistration", name = "RegistrationWorker")
    public Map<String, Object> registerUser(final JobClient client, final ActivatedJob job) {

        System.out.println("Rozpoczęcie procesu rejestracji użytkownika...");
        Scanner scanner = new Scanner(System.in);

        System.out.print("Imię: ");
        String firstName = scanner.nextLine().trim();

        System.out.print("Nazwisko: ");
        String lastName = scanner.nextLine().trim();

        System.out.print("Email: ");
        String email = scanner.nextLine().trim();

        System.out.print("Hasło: ");
        String password = scanner.nextLine().trim();

        Map<String, Object> variables = new HashMap<>();

        if (firstName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            System.err.println("Niektóre wymagane pola są puste. Rejestracja nie powiodła się.");
            failTask(client, job, "Wymagane pola: firstName, email, password.");
            variables.put("dataValid", false);
            return variables;
        }

        User registeredUser;
        try {
            registeredUser = authService.registerUser(email, password, firstName, lastName);
        } catch (RuntimeException e) {
            System.err.println("Błąd podczas rejestracji: " + e.getMessage());
            failTask(client, job, e.getMessage());
            variables.put("dataValid", false);
            return variables;
        }

        variables.put("dataValid", true);
        System.out.println("Rejestracja zakończona sukcesem.");
        return variables;
    }

    private void failTask(JobClient client, ActivatedJob job, String errorMessage) {
        client.newFailCommand(job.getKey())
                .retries(job.getRetries() - 1)
                .errorMessage(errorMessage)
                .send()
                .join();
    }
}