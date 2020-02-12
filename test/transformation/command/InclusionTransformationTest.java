package transformation.command;

import command.CommandBase;
import command.InsertCommand;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.Assert.assertEquals;

class InclusionTransformationTest {
    private CommandTransformation service;

    @BeforeEach
    public void beforeTest() {
        service = new InclusionTransformation();
    }

    @AfterEach
    void afterTest() {
        service = null;
    }

    @ParameterizedTest
    @CsvSource({ "1, 1, 0", "1, 2, 0", "2, 1, 4"})
    void insertAfterInsertTransformationShouldPrioritizeAuthorsWithSimilarPosition(int firstAuthorId, int secondAuthorId, int expected) {
        CommandBase previous = new InsertCommand(0, 0, "tets", firstAuthorId);
        CommandBase current = new InsertCommand(0, 0, "tets", secondAuthorId);

        CommandBase actual = service.insertAfterInsertTransformation(previous, current);

        assertEquals(expected, actual.position);
    }

    @Test
    void insertAfterInsertTransformationShouldPrioritizeAuthorsWithSimilarPosition2() {
        CommandBase previous = new InsertCommand(0, 4, "tets", 1);
        CommandBase current = new InsertCommand(0, 0, "tets", 1);

        CommandBase actual = service.insertAfterInsertTransformation(previous, current);

        assertEquals(0, actual.position);
    }
}