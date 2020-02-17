package services;

import command.CommandBase;
import interfaces.NotificationService;

public class ServerSentNotificationService implements NotificationService {
    @Override
    public void notify(int documentId, CommandBase command) throws Exception {
        /*
        * We can use Server-sent events and notify UI about changes. Like SignalR*/
    }
}
