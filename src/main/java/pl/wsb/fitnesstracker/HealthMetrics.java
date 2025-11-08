package pl.wsb.fitnesstracker.healthmetrics;

import jakarta.persistence.*;
import lombok.*;
import pl.wsb.fitnesstracker.user.User;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "HEALTH_METRICS")
public class HealthMetrics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = true)
    private Integer heightCm;

    @Column(nullable = true)
    private Double weightKg;

    @Column(nullable = true)
    private Double bmi;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false, unique = true)
    private User user;
}
