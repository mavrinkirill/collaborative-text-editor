package document;

import command.CommandBase;
import command.CommandType;
import exceptions.document.DocumentUpdateException;
import interfaces.TransformationFactory;
import transformations.CommandTransformation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


public class MemoryDocument implements Document {
    private TransformationFactory transformationFactory;
    private ReadWriteLock lock = new ReentrantReadWriteLock();

    private HashMap<Long, CommandBase> commandStorage = new HashMap<>();

    private long version = 0;

    private String content = "";

    public MemoryDocument(TransformationFactory transformationFactory){
        this.transformationFactory = transformationFactory;
    }

    @Override
    public DocumentState getState() {
        lock.readLock().lock();
        try {
            return new DocumentState(version, content);
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
                command = commandTransformation.transform(previousCommand, command);
            }
            catch (Exception e){
                throw new DocumentUpdateException(e);
            }
        }

        command.version = version;
        return command;
    }
}
