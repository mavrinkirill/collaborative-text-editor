package services;

import command.CommandBase;
import command.DeleteCommand;
import command.InsertCommand;
import document.ConcurrentDocument;
import document.MemoryDocument;
import interfaces.DocumentService;
import interfaces.TransformationService;
import mapper.Mapper;
import models.dto.CommandDto;
import models.dto.DeleteCommandDto;
import models.dto.DocumentDto;
import models.dto.InsertCommandDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ConcurrentDocumentService implements DocumentService {
    private TransformationService transformationService;

    private ReadWriteLock lock = new ReentrantReadWriteLock();

    private static int id = 1;

    private static HashMap<Integer, ConcurrentDocument> documentStorage = new HashMap<>();

    public ConcurrentDocumentService(TransformationService transformationService){
        this.transformationService = transformationService;

    }

    @Override
    public DocumentDto create() {
        lock.writeLock().lock();
        try {
            MemoryDocument document = new MemoryDocument(this.transformationService);
            documentStorage.put(id, document);
            DocumentDto documentDto = Mapper.Map(document.getState());
            documentDto.Id = id;
            id++;
            return documentDto;
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public DocumentDto get(int documentId) throws Exception {
        ConcurrentDocument document = documentStorage.get(documentId);

        if(document == null){
            throw new ClassNotFoundException("Document not found");
        }

        DocumentDto documentDto = Mapper.Map(document.getState());
        documentDto.Id = id;

        return documentDto;
    }

    @Override
    public void applyCommand(int documentId, InsertCommandDto commandDto) throws Exception {
        if(commandDto == null){
            throw new NullPointerException("Invalid command");
        }

        CommandBase command = Mapper.Map(commandDto);

        applyCommand(documentId, command);
    }

    @Override
    public void applyCommand(int documentId, DeleteCommandDto commandDto) throws Exception {
        if(commandDto == null){
            throw new NullPointerException("Invalid command");
        }

        CommandBase command = Mapper.Map(commandDto);

        applyCommand(documentId, command);
    }

    @Override
    public ArrayList<CommandDto> getHistory(int documentId, long version) throws Exception {
        ConcurrentDocument document = documentStorage.get(documentId);

        if(document == null){
            throw new ClassNotFoundException("Document not found");
        }

        ArrayList<CommandDto> commandDtoList = new ArrayList<>();

        ArrayList<CommandBase> history = document.getHistory(version);

        for (CommandBase entity:history) {
            CommandDto commandDto = Mapper.Map(entity);
            switch (entity.getType()){
                case INSERT:
                    commandDto.inserted = ((InsertCommand) entity).inserted;
                    break;
                case DELETE:
                    commandDto.count = ((DeleteCommand) entity).count;
                    break;
            }
            commandDtoList.add(commandDto);
        }

        return commandDtoList;
    }

    private void applyCommand(int documentId, CommandBase command) throws Exception{
        ConcurrentDocument document = documentStorage.get(documentId);

        if(document == null){
            throw new ClassNotFoundException("Document not found");
        }

        document.command(command);
    }
}
