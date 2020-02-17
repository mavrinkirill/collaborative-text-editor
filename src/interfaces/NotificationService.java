package interfaces;

import command.CommandBase;

public interface NotificationService {
    void notify(int documentId, CommandBase command) throws Exception;
}
