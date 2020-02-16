package interfaces;

import exceptions.NotFoundException;
import models.command.CommandBaseDto;
import models.command.CommandDto;
import models.document.DocumentDto;

import java.util.ArrayList;

public interface DocumentService {
    DocumentDto create();

    DocumentDto get(int documentId) throws NotFoundException;

    void applyCommand(int documentId, CommandBaseDto commandDto) throws Exception;

    ArrayList<CommandDto> getHistory(int documentId, long version) throws Exception;
}
