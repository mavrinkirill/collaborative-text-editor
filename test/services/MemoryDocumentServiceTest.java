package services;

import command.CommandBase;
import command.CommandType;
import command.InsertCommand;
import exceptions.ApiValidationException;
import exceptions.NotFoundException;
import exceptions.document.DocumentUpdateException;
import helpers.ModelBuilder;
import interfaces.DocumentService;
import interfaces.NotificationService;
import interfaces.TransformationFactory;
import models.command.CommandDto;
import models.command.InsertCommandDto;
import models.document.DocumentDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import transformations.CommandTransformation;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MemoryDocumentServiceTest {

    private DocumentService service;

    @Mock
    private TransformationFactory transformationFactory;

    @Mock
    private CommandTransformation commandTransformation;

    @Mock
    private NotificationService notificationService;

    @BeforeEach
    public void beforeTest() {
        transformationFactory = mock(TransformationFactory.class);
        commandTransformation = mock(CommandTransformation.class);
        notificationService = mock(NotificationService.class);
        service = new MemoryDocumentService(transformationFactory, notificationService);
    }

    @AfterEach
    public void afterTest() {
        notificationService = null;
        transformationFactory = null;
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
    void applyCommandShouldThrowNotFoundException() {
        InsertCommandDto insertCommandDto = ModelBuilder.insertCommandDto(0, 0, "", 0);

        Throwable thrown = assertThrows(NotFoundException.class, () -> {
            service.applyCommand(1, insertCommandDto);
        });
    }

    @Test
    void applyCommandShouldThrowApiValidationException() {
        InsertCommandDto insertCommandDto = null;

        Throwable thrown = assertThrows(ApiValidationException.class, () -> {
            service.applyCommand(1, insertCommandDto);
        });
    }

    @Test
    void applyCommandShouldThrowApiValidationExceptionByType() {
        InsertCommandDto insertCommandDto = ModelBuilder.insertCommandDto(1, 0, "a", 1);
        insertCommandDto.type = null;

        service.create();

        Throwable thrown = assertThrows(ApiValidationException.class, () -> {
            service.applyCommand(1, insertCommandDto);
        });
    }

    @ParameterizedTest
    @CsvSource({ "1, 0", "0, 1"})
    void applyCommandShouldThrowDocumentUpdateException(long version, int position) {
        InsertCommandDto insertCommandDto = ModelBuilder.insertCommandDto(version, position, "a", 1);

        service.create();

        Throwable thrown = assertThrows(DocumentUpdateException.class, () -> {
            service.applyCommand(1, insertCommandDto);
        });
    }

    @Test
    void applyCommandShouldAppay() throws Exception {
        InsertCommandDto insertCommandDto = ModelBuilder.insertCommandDto(0, 0, "a", 1);

        service.create();
        service.applyCommand(1, insertCommandDto);

        DocumentDto document = service.get(1);
        assertEquals(1, document.Id);
        assertEquals(1, document.version);
        assertEquals("a", document.content);
    }

    @Test
    void applyCommandShouldApplayWithTransformation() throws Exception {
        InsertCommandDto insertCommandDtoFirst = ModelBuilder.insertCommandDto(0, 0, "a", 1);
        InsertCommandDto insertCommandDtoSecond = ModelBuilder.insertCommandDto(0, 0, "a", 1);

        InsertCommand insertCommand = new InsertCommand(0, 0, "b", 1);

        when(transformationFactory.getTransformation(any(CommandType.class), any(CommandType.class))).thenReturn(commandTransformation);
        when(commandTransformation.transform(any(CommandBase.class), any(CommandBase.class))).thenReturn(insertCommand);

        service.create();
        service.applyCommand(1, insertCommandDtoFirst);
        service.applyCommand(1, insertCommandDtoSecond);

        DocumentDto document = service.get(1);
        assertEquals(1, document.Id);
        assertEquals(2, document.version);
        assertEquals("ba", document.content);
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