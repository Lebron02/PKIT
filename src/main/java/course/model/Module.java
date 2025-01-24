package course.model;

import javax.persistence.*;

@Entity
@Table(name = "modules", schema = "elearning")
public class Module {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int moduleId;  // kolumna module_id

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;  // Powiązanie z tabelą courses

    @Column(nullable = false)
    private String title;   // kolumna title

    @Column(name = "order_num", nullable = false)
    private int orderNum;   // kolumna order_num

    // gettery i settery
    public int getModuleId() {
        return moduleId;
    }

    public void setModuleId(int moduleId) {
        this.moduleId = moduleId;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(int orderNum) {
        this.orderNum = orderNum;
    }
}