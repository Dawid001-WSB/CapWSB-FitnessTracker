package pl.wsb.fitnesstracker.user.internal;

import java.time.LocalDate;

/**
 * DTO używane w żądaniach POST/PUT do tworzenia i aktualizacji użytkowników.
 */
public record UserRequest(
        String firstName,
        String lastName,
        LocalDate birthdate,
        String email
) {
}
