package pl.polsl.krypczyk.apartmentsforrent.userservice.domain.authorization.exception;

public class AccountNotActiveException extends RuntimeException {
    private static final String ACCOUNT_NOT_ACTIVE_MESSAGE = "Account is not active";

    public AccountNotActiveException() {
        super(ACCOUNT_NOT_ACTIVE_MESSAGE);
    }
}
