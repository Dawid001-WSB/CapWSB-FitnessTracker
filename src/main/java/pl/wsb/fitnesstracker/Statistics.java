package pl.wsb.fitnesstracker.statistics;

import jakarta.persistence.*;
import lombok.*;
import pl.wsb.fitnesstracker.user.User;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "STATISTICS")
public class Statistics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate day;

    @Column(nullable = false)
    private Integer workouts;

    @Column(nullable = false)
    private Integer totalMinutes;

    @Column(nullable = true)
    private Integer totalCalories;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;
}
