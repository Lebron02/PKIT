package course.worker;

import course.model.Course;
import course.service.CourseService;
import course.service.UserCourseService;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

@Component
public class CourseSelectionWorker {

    private final UserCourseService userCourseService;
    private final CourseService courseService;

    @Autowired
    public CourseSelectionWorker(UserCourseService userCourseService, CourseService courseService) {
        this.userCourseService = userCourseService;
        this.courseService = courseService;
    }

    @JobWorker(type = "courseSelection", name = "CourseSelectionWorker")
    public Map<String, Object> handleCourseSelection(final JobClient client, final ActivatedJob job) {
        int userId = (int) job.getVariablesAsMap().get("userId");

        List<Course> ownedCourses = userCourseService.getOwnedCourses(userId);
        List<Course> availableCourses = courseService.getCoursesNotOwnedByUser(userId);

        System.out.println("\nTwoje posiadane kursy:");
        for (int i = 0; i < ownedCourses.size(); i++) {
            System.out.printf("[%d] %s (Posiadany)\n", i + 1, ownedCourses.get(i).getCourseName());
        }
        System.out.println("\nKursy dostępne do zakupu:");
        for (int i = 0; i < availableCourses.size(); i++) {
            Course course = availableCourses.get(i);
            System.out.printf("[%d] %s - %.2f PLN\n", ownedCourses.size() + i + 1, course.getCourseName(), course.getPrice());
        }

        Scanner scanner = new Scanner(System.in);
        int choice;
        while (true) {
            try {
                System.out.print("\nWybierz numer kursu: ");
                choice = Integer.parseInt(scanner.nextLine().trim());
                if (choice < 1 || choice > (ownedCourses.size() + availableCourses.size())) {
                    throw new IllegalArgumentException("Niepoprawny numer kursu.");
                }
                break;
            } catch (Exception e) {
                System.err.println("Błąd: " + e.getMessage());
            }
        }

        Map<String, Object> variables = new HashMap<>();
        if (choice <= ownedCourses.size()) {
            // Użytkownik wybrał kurs, który już posiada
            Course chosenCourse = ownedCourses.get(choice - 1);
            variables.put("userId", userId);
            variables.put("action", false);
            variables.put("courseId", chosenCourse.getCourseId());
            variables.put("courseName", chosenCourse.getCourseName());
            System.out.println("Wybrałeś posiadany kurs: " + chosenCourse.getCourseName());
        } else {
            // Użytkownik wybrał kurs do zakupu
            Course chosenCourse = availableCourses.get(choice - ownedCourses.size() - 1);
            variables.put("userId", userId);
            variables.put("action", true);
            variables.put("courseId", chosenCourse.getCourseId());
            variables.put("chosenCourse", chosenCourse.getCourseName());
            variables.put("coursePrice", chosenCourse.getPrice());
            System.out.println("Wybrałeś kurs do zakupu: " + chosenCourse.getCourseName());
            System.out.printf("Cena kursu: %.2f PLN\n", chosenCourse.getPrice());
        }

        return variables;
    }
}
