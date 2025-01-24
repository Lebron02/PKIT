package course.repository;

import course.model.Course;
import course.model.UserCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserCourseRepository extends JpaRepository<UserCourse, Integer> {

    /**
     * Zwraca listę encji Course, które użytkownik już posiada
     * (status = 'zakupiono').
     */
    @Query("""
        SELECT c
        FROM Course c
        WHERE c.courseId IN (
            SELECT uc.course.courseId
            FROM UserCourse uc
            WHERE uc.user.userId = :userId
              AND uc.status = 'zakupiono'
        )
    """)
    List<Course> findCoursesByUserId(int userId);

    /**
     * Zwraca listę encji Course, których użytkownik jeszcze nie posiada
     * (czyli wszystkie kursy, których nie ma w user_courses
     *  z przypisanym userId i statusem 'zakupiono').
     */
    @Query("""
        SELECT c FROM Course c
        WHERE c.courseId NOT IN (
          SELECT uc.course.courseId
          FROM UserCourse uc
          WHERE uc.user.userId = :userId
            AND uc.status = 'zakupiono'
        )
    """)
    List<Course> findCoursesNotOwnedByUser(int userId);
}