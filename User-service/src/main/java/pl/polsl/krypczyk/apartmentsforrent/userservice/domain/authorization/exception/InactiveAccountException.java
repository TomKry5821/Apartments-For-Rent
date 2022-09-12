package pl.polsl.krypczyk.apartmentsforrent.userservice.domain.authorization.exception;

public class InactiveAccountException extends RuntimeException {
    private static final String INACTIVE_ACCOUNT_MESSAGE = "Account is not active";

    public InactiveAccountException() {
        super(INACTIVE_ACCOUNT_MESSAGE);
    }
}
