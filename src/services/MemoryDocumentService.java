package services;

import command.CommandBase;
import document.Document;
import document.MemoryDocument;
import exceptions.ApiValidationException;
import exceptions.NotFoundException;
import exceptions.document.DocumentUpdateException;
import interfaces.DocumentService;
import interfaces.NotificationService;
import interfaces.TransformationFactory;
import mapper.CommandConverter;
import mapper.Mapper;
import models.command.CommandBaseDto;
import models.command.CommandDto;
import models.document.DocumentDto;

import java.util.ArrayList;
import java.util.HashMap;

public class MemoryDocumentService implements DocumentService {
    private TransformationFactory transformationFactory;
    private NotificationService notificationService;

    private int id = 1;
    private HashMap<Integer, Document> documentStorage = new HashMap<>();

    public MemoryDocumentService(TransformationFactory transformationFactory, NotificationService notificationService){
        this.transformationFactory = transformationFactory;
        this.notificationService = notificationService;
    }

    @Override
    public synchronized DocumentDto create() {
        MemoryDocument document = new MemoryDocument(this.transformationFactory, this.notificationService);

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
    public void applyCommand(int documentId, CommandBaseDto commandDto) throws Exception {
        if(commandDto == null){
            throw new ApiValidationException("Invalid command");
        }

        CommandBase command = CommandConverter.Convert(commandDto);

        if(command == null){
            throw new ApiValidationException("Invalid command map");
        }

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
            CommandDto commandDto = CommandConverter.Convert(entity);
            if(commandDto != null){
                commandDtoList.add(commandDto);
            }
            else{
                // Throw exception or log about it. It depends on requirements
            }
        }

        return commandDtoList;
    }

    private void applyCommand(int documentId, CommandBase command) throws Exception{
        Document document = documentStorage.get(documentId);

        if(document == null){
            throw new NotFoundException("Document not found");
        }

        try{
            document.applyCommand(command);
        }
        catch (Exception e){
            throw new DocumentUpdateException(e);
        }
    }
}
