package pl.wsb.fitnesstracker.healthmetrics;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import pl.wsb.fitnesstracker.user.api.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "health_metrics")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class HealthMetrics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // przykładowe pola – testy ich nie sprawdzają, ale mogą się przydać w aplikacji
    @Column(name = "height", nullable = false)
    private double height;

    @Column(name = "weight", nullable = false)
    private double weight;

    @Column(name = "bmi", nullable = false)
    private double bmi;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public HealthMetrics(User user,
                         double height,
                         double weight,
                         double bmi,
                         LocalDateTime createdAt) {
        this.user = user;
        this.height = height;
        this.weight = weight;
        this.bmi = bmi;
        this.createdAt = createdAt;
    }
}
