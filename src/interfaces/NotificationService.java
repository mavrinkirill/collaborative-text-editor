package interfaces;

import command.CommandBase;

public interface NotificationService {
    void notify(CommandBase command) throws Exception;
}
