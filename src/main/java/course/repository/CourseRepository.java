package course.repository;

import course.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {

    @Query("SELECT c.price FROM Course c WHERE c.courseId = :courseId")
    Double findPriceByCourseId(int courseId);
}