package com.example.myapplication.data;

/** A generic class that holds a result success w/ data or an error exception. */
public class Result<T> {
  // Hide the private constructor to limit subclass types (Success, Error)
  private Result() {}

  @Override
  public String toString() {
    if (this instanceof Result.Success) {
      Success success = (Success) this;
      return "Success[data=" + success.getData().toString() + "]";
    } else if (this instanceof Result.Error) {
      Error error = (Error) this;
      return "Error[exception=" + error.getError().toString() + "]";
    }
    return "";
  }

  // success sub-class
  public static final class Success<T> extends Result {
    private final T data;

    public Success(T data) {
      this.data = data;
    }

    public T getData() {
      return this.data;
    }
  }

  // error sub-class
  public static final class Error extends Result {
    private final Exception error;

    public Error(Exception error) {
      this.error = error;
    }

    public Exception getError() {
      return this.error;
    }
  }
}
