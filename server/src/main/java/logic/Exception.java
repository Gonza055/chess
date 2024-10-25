package logic;

public class Exception extends java.lang.Exception {

    private String detailedMsg;
    private int errorCode;

    public Exception(String message, int statusCode) {
        super(buildExceptionMsg(message, statusCode));
        initException(message, statusCode);
    }

    private void initException(String message, int code) {
        if (message != null && !message.isEmpty()) {
            this.detailedMsg= message;
        } else {
            this.detailedMsg= "Unknown error occurred.";
        }

        this.errorCode = validateStatusCode(code);
    }

    private int validateStatusCode(int statusCode) {
        if (statusCode >= 100 && statusCode < 600) {
            return statusCode;
        } else {
            return 500;
        }
    }

    private static String buildExceptionMsg(String message, int statusCode) {
        StringBuilder builder = new StringBuilder();
        if (message != null && !message.isEmpty()) {
            builder.append("Error: ").append(message);
        } else {
            builder.append("An error occurred.");
        }
        builder.append(" (Status Code: ").append(statusCode).append(")");
        return builder.toString();
    }

    public int statusCode() {
        return this.errorCode;
    }

    public String getDetailedMsg() {
        return this.detailedMsg;
    }

    @Override
    public String getMessage() {
        return "Exception: " + detailedMsg + " | HTTP Status Code: " + errorCode;
    }
}
