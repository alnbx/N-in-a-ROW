package engine;

public class SettingsFileException extends Exception {
    String message;

    SettingsFileException(String msg) {
        this.message = msg;
    }

    public String toString() {
        return this.message;
    }
}
