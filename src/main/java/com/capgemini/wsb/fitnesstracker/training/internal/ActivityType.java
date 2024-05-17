package com.capgemini.wsb.fitnesstracker.training.internal;

/**
 * Enum representing different types of physical activities.
 */
public enum ActivityType {

    /**
     * Represents the activity of running.
     */
    RUNNING("Running"),

    /**
     * Represents the activity of cycling.
     */
    CYCLING("Cycling"),

    /**
     * Represents the activity of walking.
     */
    WALKING("Walking"),

    /**
     * Represents the activity of swimming.
     */
    SWIMMING("Swimming"),

    /**
     * Represents the activity of playing tennis.
     */
    TENNIS("Tenis");

    private final String displayName;

    /**
     * Constructs an ActivityType with a display name.
     *
     * @param displayName the display name of the activity
     */
    ActivityType(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Gets the display name of the activity.
     *
     * @return the display name
     */
    public String getDisplayName() {
        return displayName;
    }

}
