package document;

import command.*;
import exceptions.document.DocumentUpdateException;
import exceptions.document.DocumentInvalidVersionException;
import interfaces.*;
import java.util.*;
import java.util.concurrent.locks.*;

public class MemoryDocument implements Document {
    private TransformationService transformationService;
    private NotificationService notificationService;

    private ReadWriteLock lock = new ReentrantReadWriteLock();

    private HashMap<Long, CommandBase> commandStorage = new HashMap<>();

    private long version = 0;

    private String content = "";

    public MemoryDocument(TransformationService transformationService, NotificationService notificationService){
        this.transformationService = transformationService;
        this.notificationService = notificationService;
    }

    @Override
    public DocumentState getState() {
        lock.readLock().lock();
        try {
            DocumentState state = new DocumentState();
            state.version = version;
            state.content = content;
            return state;
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void applyCommand(CommandBase command) throws Exception {
        lock.writeLock().lock();
        try {
            if(command.version > version){
                throw new DocumentInvalidVersionException();
            }

            if(command.version < version){
                command = transform(command);
            }

            try {
                content = command.apply(content);
                commandStorage.put(version, command);
                version++;
            }
            catch (Exception e){
                throw new DocumentUpdateException(e);
            }

            try{
                notificationService.notify(command);
            }
            catch (Exception e){
                //Ignore
                /*We can just log it and don't send it to top level*/
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public ArrayList<CommandBase> getHistory(long version) {
        ArrayList<CommandBase> history = new ArrayList<>();

        lock.readLock().lock();
        try {
            for (long i = version; i < this.version; i++){
                CommandBase command = commandStorage.get(i);
                history.add(command);
            }

            return history;
        } finally {
            lock.readLock().unlock();
        }
    }

    private CommandBase transform(CommandBase command) throws Exception {
        for (long i = command.version; i < version; i++){
            CommandBase prev = commandStorage.get(i);
            command = transformationService.transform(prev, command);
        }
        command.version = version;

        return command;
    }
}
