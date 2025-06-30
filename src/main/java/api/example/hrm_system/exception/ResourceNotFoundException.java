package api.example.hrm_system.exception;

public class ResourceNotFoundException extends ApiException {
    public ResourceNotFoundException(String resource) {
        super(resource + " not found.");
    }
}