package document;

import command.CommandBase;
import command.CommandType;
import exceptions.document.DocumentUpdateException;
import interfaces.NotificationService;
import interfaces.TransformationFactory;
import transformations.CommandTransformation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


public class MemoryDocument implements Document {
    private TransformationFactory transformationFactory;
    private NotificationService notificationService;

    private ReadWriteLock lock = new ReentrantReadWriteLock();

    private HashMap<Long, CommandBase> commandStorage = new HashMap<>();

    private long version = 0;

    private String content = "";

    public MemoryDocument(TransformationFactory transformationFactory, NotificationService notificationService){
        this.transformationFactory = transformationFactory;
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
                throw new DocumentUpdateException("Document invalid version");
            }

            if(command.version < version){
                command = transformation(command);
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
                //Ignore or not. It depends on  requirements
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

    private CommandBase transformation(CommandBase command) throws Exception {
        for (long i = command.version; i < version; i++){
            CommandBase previousCommand = commandStorage.get(i);
            CommandType previousCommandType = previousCommand.getType();
            CommandType currentCommandType = command.getType();

            try{
                CommandTransformation commandTransformation = transformationFactory.getTransformation(previousCommandType, currentCommandType);
                command = commandTransformation.transformation(previousCommand, command);
            }
            catch (Exception e){
                throw new DocumentUpdateException(e);
            }
        }

        command.version = version;
        return command;
    }
}
