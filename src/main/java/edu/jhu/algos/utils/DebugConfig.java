package edu.jhu.algos.utils;

/**
 * Global debug configuration to enable or disable debug messages.
 * <p>
 * Set `DEBUG_MODE` to `true` to enable debug messages.
 * Set `DEBUG_MODE` to `false` to suppress all debug output.
 * </p>
 */
public class DebugConfig {
    private static boolean DEBUG_MODE = false; // Default: OFF

    /**
     * Enables debug mode.
     */
    public static void enableDebug() {
        DEBUG_MODE = true;
    }

    /**
     * Disables debug mode.
     */
    public static void disableDebug() {
        DEBUG_MODE = false;
    }

    /**
     * Prints a debug message if DEBUG_MODE is enabled.
     * @param message The message to print.
     */
    public static void log(String message) {
        if (DEBUG_MODE) {
            System.out.println("[DEBUG] " + message);
        }
    }

    /**
     * Returns whether debug mode is currently enabled.
     * @return true if debug mode is enabled, false otherwise.
     */
    public static boolean isDebugEnabled() {
        return DEBUG_MODE;
    }
}
