package interfaces;

import models.dto.*;

import java.util.ArrayList;

public interface DocumentService {
    DocumentDto create();

    DocumentDto get(int documentId) throws Exception;

    void applyCommand(int documentId, InsertCommandDto commandDto) throws Exception;

    void applyCommand(int documentId, DeleteCommandDto commandDto) throws Exception;

    ArrayList<CommandDto> getHistory(int documentId, long version) throws Exception;
}
