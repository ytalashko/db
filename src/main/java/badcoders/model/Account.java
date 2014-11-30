package badcoders.model;

import badcoders.logic.util.Utils;

/**
 * An Account represents all information about a user.
 */
public final class Account {

    public final long id;
    public final String login;
    public final boolean isAdmin;

    public Account(long id, String login, boolean isAdmin) {
        Utils.checkArgument(login != null, "Account must have login");
        this.id = id;
        this.login = login;
        this.isAdmin = isAdmin;
    }

    public Account(String login, boolean isAdmin) {
        this(0, login, isAdmin);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Account account = (Account) o;

        return isAdmin == account.isAdmin && login.equals(account.login);

    }

    @Override
    public int hashCode() {
        int result = login.hashCode();
        result = 31 * result + (isAdmin ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Account{" +
                "login='" + login + '\'' +
                ", isAdmin=" + isAdmin +
                '}';
    }
}
