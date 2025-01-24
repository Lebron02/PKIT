package course.model;

import javax.persistence.*;

@Entity
@Table(name = "user_modules", schema = "elearning")
public class UserModule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userModuleId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;  // kolumna user_id

    @ManyToOne
    @JoinColumn(name = "module_id", nullable = false)
    private Module module; // kolumna module_id

    @Column(nullable = false)
    private String status; // np. "Wykonano" / "Niewykonano"

    // gettery i settery
    public Long getUserModuleId() {
        return userModuleId;
    }

    public void setUserModuleId(Long userModuleId) {
        this.userModuleId = userModuleId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}