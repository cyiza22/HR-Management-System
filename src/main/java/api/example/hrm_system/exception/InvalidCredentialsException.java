package api.example.hrm_system.exception;

public class InvalidCredentialsException extends ApiException {
    public InvalidCredentialsException() {
        super("Invalid email or password.");
    }
}