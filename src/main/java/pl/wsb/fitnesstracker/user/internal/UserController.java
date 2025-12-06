package pl.wsb.fitnesstracker.user.internal;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.wsb.fitnesstracker.user.api.User;

import java.time.LocalDate;
import java.util.List;

/**
 * REST API do operacji CRUD na użytkownikach.
 * Ścieżki dopasowane do UserApiIntegrationTest.
 */
@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class UserController {

    // Spring wstrzyknie repozytorium dla encji User (tak jak w InitialDataLoader)
    private final JpaRepository<User, Long> userRepository;

    // GET /v1/users
    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // GET /v1/users/simple
    @GetMapping("/simple")
    public List<User> getAllSimpleUsers() {
        // test sprawdza tylko firstName/lastName – możemy zwrócić pełnych Userów
        return userRepository.findAll();
    }

    // GET /v1/users/{id}
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // GET /v1/users/email?email=...
    @GetMapping("/email")
    public List<User> getUsersByEmail(@RequestParam String email) {
        String lower = email.toLowerCase();
        return userRepository.findAll().stream()
                .filter(u -> u.getEmail() != null &&
                        u.getEmail().toLowerCase().contains(lower))
                .toList();
    }

    // GET /v1/users/older/{time}
    @GetMapping("/older/{time}")
    public List<User> getUsersOlderThan(@PathVariable("time") LocalDate time) {
        return userRepository.findAll().stream()
                .filter(u -> u.getBirthdate() != null &&
                        u.getBirthdate().isBefore(time))
                .toList();
    }

    // DELETE /v1/users/{userId}
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        userRepository.deleteById(userId);
        return ResponseEntity.noContent().build(); // 204
    }

    // POST /v1/users
    @PostMapping
    public ResponseEntity<Void> createUser(@RequestBody UserRequest request) {
        User user = new User(
                request.firstName(),
                request.lastName(),
                request.birthdate(),
                request.email()
        );
        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).build(); // 201
    }

    // PUT /v1/users/{userId}
    @PutMapping("/{userId}")
    public ResponseEntity<Void> updateUser(@PathVariable Long userId,
                                           @RequestBody UserRequest request) {
        // Usuwamy starego użytkownika (jeśli istnieje)
        userRepository.deleteById(userId);

        // Zapisujemy nowego z danymi z requestu
        User user = new User(
                request.firstName(),
                request.lastName(),
                request.birthdate(),
                request.email()
        );
        userRepository.save(user);

        return ResponseEntity.ok().build(); // 200
    }
}
