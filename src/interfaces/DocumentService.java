package interfaces;

import exceptions.NotFoundException;
import models.command.CommandDto;
import models.command.DeleteCommandDto;
import models.document.DocumentDto;
import models.command.InsertCommandDto;

import java.util.ArrayList;

public interface DocumentService {
    DocumentDto create();

    DocumentDto get(int documentId) throws NotFoundException;

    void applyCommand(int documentId, InsertCommandDto commandDto) throws Exception;

    void applyCommand(int documentId, DeleteCommandDto commandDto) throws Exception;

    ArrayList<CommandDto> getHistory(int documentId, long version) throws Exception;
}
