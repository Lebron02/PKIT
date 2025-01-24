package course.worker;

import course.model.Module;
import course.model.User;
import course.model.UserModule;
import course.service.AuthService;
import course.service.ModuleService;
import course.service.UserModuleService;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

@Component
public class ModuleSelectionWorker {

    private final ModuleService moduleService;
    private final UserModuleService userModuleService;
    private final AuthService authService;

    @Autowired
    public ModuleSelectionWorker(ModuleService moduleService,
                                 UserModuleService userModuleService,
                                 AuthService authService) {
        this.moduleService = moduleService;
        this.userModuleService = userModuleService;
        this.authService = authService;
    }

    @JobWorker(type = "markCompletedTopics", name = "markCompletedTopicsWorker")
    public Map<String, Object> handleModuleSelection(final JobClient client, final ActivatedJob job) {
        Map<String, Object> vars = job.getVariablesAsMap();

        if (!vars.containsKey("courseId") || vars.get("courseId") == null) {
            throw new IllegalArgumentException("Brak wymaganej zmiennej 'courseId'.");
        }
        if (!vars.containsKey("userId") || vars.get("userId") == null) {
            throw new IllegalArgumentException("Brak wymaganej zmiennej 'userId'.");
        }

        int userId = (int) vars.get("userId");
        int courseId = (int) vars.get("courseId");

        User user = authService.getUserById(userId);
        List<Module> modules = moduleService.getModulesByCourseId(courseId);

        while (true) {
            List<UserModule> userModules = modules.stream()
                    .map(module -> userModuleService.getOrCreateUserModule(user, module))
                    .collect(Collectors.toList());

            System.out.println("\nLista modułów w kursie:");
            for (int i = 0; i < userModules.size(); i++) {
                UserModule um = userModules.get(i);
                System.out.printf("[%d] %s (kolejność: %d, status: %s)\n",
                        i + 1,
                        um.getModule().getTitle(),
                        um.getModule().getOrderNum(),
                        um.getStatus());
            }

            Scanner scanner = new Scanner(System.in);
            System.out.print("\nWybierz numer modułu do oznaczenia jako wykonany, '0' aby kontynuować bez kończenia lub 'x' aby zakończyć: ");
            String choice = scanner.nextLine().trim();

            if ("x".equalsIgnoreCase(choice)) {
                System.out.println("Zakończono proces na żądanie użytkownika.");
                Map<String, Object> variables = new HashMap<>();
                variables.put("userTired", true);
                return variables;
            }

            if ("0".equals(choice)) {
                boolean allCompleted = userModules.stream()
                        .allMatch(um -> "Wykonano".equals(um.getStatus()));

                if (allCompleted) {
                    System.out.println("Wszystkie moduły zostały wykonane. Generowanie certyfikatu...");
                    Map<String, Object> variables = new HashMap<>();
                    variables.put("userTired", false);
                    variables.put("userId", userId);
                    variables.put("courseId", courseId);
                    return variables;
                } else {
                    System.out.println("Najpierw wykonaj wszystkie moduły!");
                    client.newCompleteCommand(job.getKey())
                            .variables(Map.of("isTired", false))
                            .send()
                            .whenComplete((response, throwable) -> {
                                if (throwable != null) {
                                    System.err.println("Błąd podczas kończenia zadania: " + throwable.getMessage());
                                }
                            });
                }
            }


            try {
                int moduleChoice = Integer.parseInt(choice);

                if (moduleChoice > 0 && moduleChoice <= userModules.size()) {
                    UserModule chosenUserModule = userModules.get(moduleChoice - 1);

                    if ("Wykonano".equals(chosenUserModule.getStatus())) {
                        System.out.println("Moduł '" + chosenUserModule.getModule().getTitle() + "' został już wykonany. Wybierz inny moduł.");
                    } else {
                        userModuleService.markModuleAsDone(user, chosenUserModule.getModule());
                        System.out.println("Moduł '" + chosenUserModule.getModule().getTitle() + "' oznaczony jako wykonany.");
                    }
                } else {
                    System.out.println("Niepoprawny numer modułu. Spróbuj ponownie.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Niepoprawny wybór. Wpisz numer modułu, '0' lub 'x'.");
            }
        }
    }
}
