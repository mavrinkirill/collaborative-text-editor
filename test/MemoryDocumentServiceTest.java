import command.CommandBase;
import command.InsertCommand;
import exceptions.document.DocumentInvalidVersionException;
import exceptions.document.DocumentUpdateException;
import exceptions.ApiValidationException;
import exceptions.NotFoundException;
import interfaces.DocumentService;
import interfaces.NotificationService;
import interfaces.TransformationService;
import models.command.CommandDto;
import models.command.DeleteCommandDto;
import models.document.DocumentDto;
import models.command.InsertCommandDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import services.MemoryDocumentService;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MemoryDocumentServiceTest {
    private DocumentService service;

    @Mock
    private TransformationService transformationService;

    @Mock
    private NotificationService notificationService;

    @BeforeEach
    public void beforeTest() {
        transformationService = mock(TransformationService.class);
        notificationService = mock(NotificationService.class);
        service = new MemoryDocumentService(transformationService, notificationService);
    }

    @AfterEach
    public void afterTest() {
        notificationService = null;
        transformationService = null;
        service = null;
    }

    @Test
    void createShouldCreate() {
        DocumentDto document = service.create();

        assertEquals(1, document.Id);
        assertEquals(0, document.version);
        assertEquals("", document.content);
    }

    @Test
    void getShouldThrowNotFoundException() {
        Throwable thrown = assertThrows(NotFoundException.class, () -> {
            DocumentDto document = service.get(1);
        });
    }

    @Test
    void getShouldReturn() throws NotFoundException {
        service.create();

        DocumentDto document = service.get(1);

        assertEquals(1, document.Id);
        assertEquals(0, document.version);
        assertEquals("", document.content);
    }

    @Test
    void applyInsertCommandShouldThrowNotFoundException() {
        InsertCommandDto insertCommandDto = new InsertCommandDto();

        Throwable thrown = assertThrows(NotFoundException.class, () -> {
            service.applyCommand(1, insertCommandDto);
        });
    }

    @Test
    void applyInsertCommandShouldThrowApiValidationException() {
        InsertCommandDto insertCommandDto = null;

        Throwable thrown = assertThrows(ApiValidationException.class, () -> {
            service.applyCommand(1, insertCommandDto);
        });
    }

    @Test
    void applyInsertCommandShouldThrowDocumentInvalidVersionException() {
        InsertCommandDto insertCommandDto = ModelBuilder.insertCommandDto(1, 0, "a", 1);

        service.create();

        Throwable thrown = assertThrows(DocumentInvalidVersionException.class, () -> {
            service.applyCommand(1, insertCommandDto);
        });
    }

    @Test
    void applyInsertCommandShouldThrowDocumentUpdateException() {
        InsertCommandDto insertCommandDto = ModelBuilder.insertCommandDto(0, 1, "a", 1);

        service.create();

        Throwable thrown = assertThrows(DocumentUpdateException.class, () -> {
            service.applyCommand(1, insertCommandDto);
        });
    }

    @Test
    void applyInsertCommandShouldAppay() throws Exception {
        InsertCommandDto insertCommandDto = ModelBuilder.insertCommandDto(0, 0, "a", 1);

        service.create();
        service.applyCommand(1, insertCommandDto);

        DocumentDto document = service.get(1);
        assertEquals(1, document.Id);
        assertEquals(1, document.version);
        assertEquals("a", document.content);
    }

    @Test
    void applyInsertCommandShouldApplayWithTransformation() throws Exception {
        InsertCommandDto insertCommandDtoFirst = ModelBuilder.insertCommandDto(0, 0, "a", 1);
        InsertCommandDto insertCommandDtoSecond = ModelBuilder.insertCommandDto(0, 0, "a", 1);

        InsertCommand insertCommand = new InsertCommand(0, 0, "b", 1);

        when(transformationService.transform(any(CommandBase.class), any(CommandBase.class))).thenReturn(insertCommand);

        service.create();
        service.applyCommand(1, insertCommandDtoFirst);
        service.applyCommand(1, insertCommandDtoSecond);

        DocumentDto document = service.get(1);
        assertEquals(1, document.Id);
        assertEquals(2, document.version);
        assertEquals("ba", document.content);
    }

    @Test
    void applyDeleteCommandShouldThrowNotFoundException() {
        DeleteCommandDto deleteCommandDto = new DeleteCommandDto();

        Throwable thrown = assertThrows(NotFoundException.class, () -> {
            service.applyCommand(1, deleteCommandDto);
        });
    }

    @Test
    void applyDeleteCommandShouldThrowApiValidationException() {
        DeleteCommandDto deleteCommandDto = null;

        Throwable thrown = assertThrows(ApiValidationException.class, () -> {
            service.applyCommand(1, deleteCommandDto);
        });
    }

    @Test
    void applyDeleteCommandShouldThrowDocumentInvalidVersionException() {
        DeleteCommandDto deleteCommandDto = ModelBuilder.deleteCommandDto(1, 0, 1, 1);

        service.create();

        Throwable thrown = assertThrows(DocumentInvalidVersionException.class, () -> {
            service.applyCommand(1, deleteCommandDto);
        });
    }

    @Test
    void applyDeleteCommandShouldThrowDocumentUpdateException() {
        DeleteCommandDto deleteCommandDto = ModelBuilder.deleteCommandDto(0, -1, 1, 1);

        service.create();

        Throwable thrown = assertThrows(DocumentUpdateException.class, () -> {
            service.applyCommand(1, deleteCommandDto);
        });
    }

    @Test
    void getHistorShouldThrowNotFoundException() {
        Throwable thrown = assertThrows(NotFoundException.class, () -> {
            ArrayList<CommandDto> history = service.getHistory(1, 0);
        });
    }

    @Test
    void getHistoryShouldReturn() throws Exception {
        InsertCommandDto insertCommandDtoFirst = ModelBuilder.insertCommandDto(0, 0, "a", 1);
        InsertCommandDto insertCommandDtoSecond = ModelBuilder.insertCommandDto(1, 0, "a", 1);
        InsertCommandDto insertCommandDtoThird = ModelBuilder.insertCommandDto(2, 0, "a", 1);

        service.create();
        service.applyCommand(1, insertCommandDtoFirst);
        service.applyCommand(1, insertCommandDtoSecond);
        service.applyCommand(1, insertCommandDtoThird);

        ArrayList<CommandDto> history = service.getHistory(1, 0);

        assertEquals(3 , history.size());
    }
}