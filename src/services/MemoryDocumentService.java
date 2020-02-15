package services;

import command.CommandBase;
import command.DeleteCommand;
import command.InsertCommand;
import document.Document;
import document.MemoryDocument;
import exceptions.ApiValidationException;
import exceptions.NotFoundException;
import interfaces.DocumentService;
import interfaces.NotificationService;
import interfaces.TransformationService;
import mapper.Mapper;
import models.command.CommandDto;
import models.command.DeleteCommandDto;
import models.command.InsertCommandDto;
import models.document.DocumentDto;

import java.util.ArrayList;
import java.util.HashMap;

public class MemoryDocumentService implements DocumentService {
    private TransformationService transformationService;
    private NotificationService notificationService;

    private int id = 1;
    private HashMap<Integer, Document> documentStorage = new HashMap<>();

    public MemoryDocumentService(TransformationService transformationService, NotificationService notificationService){
        this.transformationService = transformationService;
        this.notificationService = notificationService;
    }

    @Override
    public synchronized DocumentDto create() {
        MemoryDocument document = new MemoryDocument(this.transformationService, this.notificationService);

        documentStorage.put(id, document);
        DocumentDto documentDto = Mapper.Map(document.getState());
        documentDto.Id = id;
        id++;

        return documentDto;
    }

    @Override
    public DocumentDto get(int documentId) throws NotFoundException {
        Document document = documentStorage.get(documentId);

        if(document == null){
            throw new NotFoundException("Document not found");
        }

        DocumentDto documentDto = Mapper.Map(document.getState());
        documentDto.Id = documentId;

        return documentDto;
    }

    @Override
    public void applyCommand(int documentId, InsertCommandDto commandDto) throws Exception {
        if(commandDto == null){
            throw new ApiValidationException("Invalid command");
        }

        CommandBase command = Mapper.Map(commandDto);

        applyCommand(documentId, command);
    }

    @Override
    public void applyCommand(int documentId, DeleteCommandDto commandDto) throws Exception {
        if(commandDto == null){
            throw new ApiValidationException("Invalid command");
        }

        CommandBase command = Mapper.Map(commandDto);

        applyCommand(documentId, command);
    }

    @Override
    public ArrayList<CommandDto> getHistory(int documentId, long version) throws Exception {
        Document document = documentStorage.get(documentId);

        if(document == null){
            throw new NotFoundException("Document not found");
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
        Document document = documentStorage.get(documentId);

        if(document == null){
            throw new NotFoundException("Document not found");
        }

        document.applyCommand(command);
    }
}
