package api.example.hrm_system.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Role {
    EMPLOYEE,
    MANAGER,
    HR;

    @JsonCreator
    public static Role fromString(String value) {
        if (value == null) return null;
        try {
            // Remove "ROLE_" prefix if present and convert to uppercase
            String cleanValue = value.toUpperCase().replace("ROLE_", "");
            return Role.valueOf(cleanValue);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid role value. Valid values are: EMPLOYEE, MANAGER, HR");
        }
    }

    @JsonValue
    public String toValue() {
        return this.name();
    }

    public static boolean isValidRole(String role) {
        if (role == null || role.trim().isEmpty()) return false;
        try {
            Role.fromString(role);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    // Check if role is valid for registration (only EMPLOYEE and MANAGER)
    public static boolean isValidRegistrationRole(String role) {
        if (!isValidRole(role)) return false;
        Role roleEnum = fromString(role);
        return roleEnum == EMPLOYEE || roleEnum == MANAGER;
    }
}