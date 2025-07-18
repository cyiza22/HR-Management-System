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
            return Role.valueOf(value.toUpperCase().replace("ROLE_", ""));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid role value. Valid values are: EMPLOYEE, MANAGER, HR");
        }
    }

    @JsonValue
    public String toValue() {
        return this.name().toLowerCase();
    }

    public static boolean isValidRole(String role) {
        if (role == null) return false;
        try {
            Role.fromString(role);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}