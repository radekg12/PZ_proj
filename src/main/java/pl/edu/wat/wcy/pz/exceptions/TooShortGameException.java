package pl.edu.wat.wcy.pz.exceptions;

public class TooShortGameException extends IllegalArgumentException {
    public TooShortGameException() {
    }

    public TooShortGameException(String s) {
        super(s);
    }

    public TooShortGameException(String message, Throwable cause) {
        super(message, cause);
    }

    public TooShortGameException(Throwable cause) {
        super(cause);
    }


}
