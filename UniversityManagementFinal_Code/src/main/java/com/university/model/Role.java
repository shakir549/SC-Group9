package com.university.model;


public enum Role {
    STUDENT("Student"),
    SUPERVISOR("Supervisor"),
    ADMIN("Admin");
    
    private final String displayName;
    
    Role(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    @Override
    public String toString() {
        return displayName;
    }
    
    /**
     * Convert from display name to Role enum
     * @param displayName the display name of the role
     * @return the corresponding Role enum, or null if not found
     */
    public static Role fromString(String displayName) {
        for (Role role : Role.values()) {
            if (role.displayName.equalsIgnoreCase(displayName)) {
                return role;
            }
        }
        return null;
    }
}
