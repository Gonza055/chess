package logic;

public class Exception extends RuntimeException {

  private final int statusCode;
  private final String errorDetails;

  public Exception(String message, int statusCode) {
    super(message);
    this.statusCode = statusCode;
    this.errorDetails = null;
  }

  public Exception(String message, int statusCode, String errorDetails) {
    super(message);
    this.statusCode = statusCode;
    this.errorDetails = errorDetails;
  }

  public int getStatusCode() {
    return statusCode;
  }

  public String getErrorDetails() {
    return errorDetails;
  }

  @Override
  public String toString() {
    String basicMessage = "ResponseException: " + getMessage() + " (Status Code: " + statusCode + ")";
    return errorDetails != null ? basicMessage + " - Details: " + errorDetails : basicMessage;
  }
}