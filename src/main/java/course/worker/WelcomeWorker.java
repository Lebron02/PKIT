package course.worker;

import course.model.Course;
import course.service.CourseService;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

@Component
public class WelcomeWorker {

    private final CourseService courseService;

    @Autowired
    public WelcomeWorker(CourseService courseService) {
        this.courseService = courseService;
    }

    @JobWorker(type = "welcome")

    public Map<String, Object> handleWelcome(final ActivatedJob job) {

        System.out.println("\n\nWitaj w systemie kursów!");

        System.out.println("Dostępne kursy:");

        List<Course> courses = courseService.getAllCourses();
        for (Course course : courses) {
            System.out.printf("- [%d] %s (Cena: %.2f zł)\n", course.getCourseId(), course.getCourseName(), course.getPrice());
        }

        Scanner scanner = new Scanner(System.in);

        Boolean hasAccount = null;

        while (hasAccount == null) {

            System.out.println("\nCzy masz konto w systemie?");
            System.out.println("[1] Tak");
            System.out.println("[2] Nie");
            System.out.print("Twój wybór: ");
            int choice = Integer.parseInt(scanner.nextLine());

            if (choice == 1) {
                hasAccount = true;
            } else if (choice == 2) {
                hasAccount = false;
            } else {
                System.out.println("Niepoprawny wybór. Wpisz 1 lub 2.");
            }
        }

        return Map.of(
                "hasAccount", hasAccount
        );

    }
}