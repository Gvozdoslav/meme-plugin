public class MemeProcessingException extends RuntimeException{
    public MemeProcessingException() {
    }

    public MemeProcessingException(String message) {
        super(message);
    }

    public MemeProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
