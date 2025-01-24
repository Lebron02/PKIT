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
public class LoginWorker {

    private final AuthService authService;

    @Autowired
    public LoginWorker(AuthService authService) {
        this.authService = authService;
    }

    @JobWorker(type = "userLogin", name = "LoginWorker")
    public Map<String, Object> loginUser(final JobClient client, final ActivatedJob job) {

        System.out.println("Rozpoczęto proces logowania...");

        Scanner scanner = new Scanner(System.in);

        System.out.print("Podaj email: ");
        String inputEmail = scanner.nextLine().trim();

        System.out.print("Podaj hasło: ");
        String inputPassword = scanner.nextLine().trim();

        boolean loginSuccessful = false;
        int userId = 0;

        try {
            User user = authService.loginUser(inputEmail, inputPassword);
            System.out.println("Logowanie powiodło się. Witaj, " + user.getFirstName() + "!");
            loginSuccessful = true;
            userId = user.getUserId();
        } catch (RuntimeException e) {
            System.err.println("Logowanie nie powiodło się: " + e.getMessage());
        }

        Map<String, Object> variables = new HashMap<>();
        variables.put("loginSuccessful", loginSuccessful);

        if (loginSuccessful && userId != 0) {
            variables.put("userId", userId);
        }
        if (!loginSuccessful) {
            System.err.println("Niepoprawne dane logowania.");
        }

        return variables;


    }
}