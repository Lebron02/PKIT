package course.worker;

import course.service.CourseService;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

@Component
public class DiscountCalculationWorker {

    private final CourseService courseService;

    @Autowired
    public DiscountCalculationWorker(CourseService courseService) {
        this.courseService = courseService;
    }

    @JobWorker(type = "discountCalculation", name = "DiscountCalculationWorker")
    public Map<String, Object> handleDiscountCalculation(final JobClient client, final ActivatedJob job) {

        Map<String, Object> vars = job.getVariablesAsMap();

        int userId = (int) vars.getOrDefault("userId", -1);
        int courseId = (int) vars.getOrDefault("courseId", -1);
        Double coursePrice = (Double) vars.getOrDefault("coursePrice", 0.0);

        if (coursePrice == 0.0 && courseId != -1) {
            coursePrice = courseService.getCoursePriceById(courseId);

        }

        if (userId == -1 || courseId == -1 || coursePrice == null) {
            throw new IllegalArgumentException("Brak wymaganych danych (userId, courseId, coursePrice).");
        }

        Scanner scanner = new Scanner(System.in);

        System.out.println("\nWybierz swój status:");
        System.out.println("[1] Student");
        System.out.println("[2] Żołnierz");
        System.out.println("[3] Brak");

        int category = 0;
        while (category < 1 || category > 3) {
            System.out.print("Wybierz opcję (1-3): ");
            category = Integer.parseInt(scanner.nextLine());
            if (category < 1 || category > 3) {
                System.out.println("Nieprawidłowa opcja. Spróbuj ponownie.");
            }
        }

        String categoryType;
        switch (category) {
            case 1:
                categoryType = "Student";
                break;
            case 2:
                categoryType = "Żołnierz";
                break;
            case 3:
                categoryType = "Brak";
                break;
            default:
                throw new IllegalStateException("Nieoczekiwany błąd podczas wyboru kategorii.");
        }

        Map<String, Object> outputVariables = new HashMap<>();
        outputVariables.put("userId", userId);
        outputVariables.put("courseId", courseId);
        outputVariables.put("OriginalPrice", coursePrice);
        outputVariables.put("discount", categoryType);

        return outputVariables;
    }
}