package pt.ulisboa.tecnico.mydrive.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import pt.ist.fenixframework.Atomic;
import pt.ulisboa.tecnico.mydrive.domain.User;
import pt.ulisboa.tecnico.mydrive.domain.MyDrive;
import pt.ulisboa.tecnico.mydrive.exception.UserDoesNotExistException;
import pt.ulisboa.tecnico.mydrive.exception.MyDriveException;

public abstract class MyDriveService {
    protected static final Logger log = LogManager.getRootLogger();

    @Atomic
    public final void execute() throws MyDriveException {
        dispatch();
    }

    static MyDrive getMyDrive() {
        return MyDrive.getInstance();
    }

    static User getUser(String userName) throws UserDoesNotExistException {
        User u = getMyDrive().getUserByUserName(userName);

        if (u == null)
            throw new UserDoesNotExistException(userName);

        return u;
    }

    protected abstract void dispatch() throws MyDriveException;
}
