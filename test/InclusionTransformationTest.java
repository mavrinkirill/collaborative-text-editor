import command.DeleteCommand;
import command.InsertCommand;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import transformation.command.CommandTransformation;
import transformation.command.InclusionTransformation;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InclusionTransformationTest {
    private CommandTransformation service;

    @BeforeEach
    public void beforeTest() {
        service = new InclusionTransformation();
    }

    @AfterEach
    public void afterTest() {
        service = null;
    }

    @Test
    public void insertAfterInsertShouldNotChangePosition() {
        InsertCommand previous = new InsertCommand(0, 1, "a", 1);
        InsertCommand current = new InsertCommand(0, 0, "a", 1);

        InsertCommand actual = service.transformation(previous, current);

        assertEquals(0, actual.position);
        assertEquals(1, actual.inserted.length());
    }

    @ParameterizedTest
    @CsvSource({ "1, 1, 0", "1, 2, 0"})
    public void insertAfterInsertShouldNotChangePosition(int firstAuthorId, int secondAuthorId, int expectedPosition) {
        InsertCommand previous = new InsertCommand(0, 0, "a", firstAuthorId);
        InsertCommand current = new InsertCommand(0, 0, "a", secondAuthorId);

        InsertCommand actual = service.transformation(previous, current);

        assertEquals(expectedPosition, actual.position);
        assertEquals(1, actual.inserted.length());
    }

    @ParameterizedTest
    @CsvSource({ "0, 1, 1, 1, 2", "0, 0, 2, 1, 1"})
    public void insertAfterInsertShouldChangePosition(int firstPosition, int secondPosition, int firstAuthorId, int secondAuthorId, int expectedPosition) {
        InsertCommand previous = new InsertCommand(0, firstPosition, "a", firstAuthorId);
        InsertCommand current = new InsertCommand(0, secondPosition, "a", secondAuthorId);

        InsertCommand actual = service.transformation(previous, current);

        assertEquals(expectedPosition, actual.position);
        assertEquals(1, actual.inserted.length());
    }

    @ParameterizedTest
    @CsvSource({ "1, 0, 0", "0, 0, 0" })
    public void insertAfterDeleteShouldNotChangePosition(int firstPosition, int secondPosition, int expectedPosition) {
        DeleteCommand previous = new DeleteCommand(0, firstPosition, 1, 1);
        InsertCommand current = new InsertCommand(0, secondPosition, "a", 1);

        InsertCommand actual = service.transformation(previous, current);

        assertEquals(expectedPosition, actual.position);
        assertEquals(1, actual.inserted.length());
    }

    @Test
    public void insertAfterDeleteShouldChangePosition() {
        DeleteCommand previous = new DeleteCommand(0, 0, 1, 1);
        InsertCommand current = new InsertCommand(0, 1, "a", 1);

        InsertCommand actual = service.transformation(previous, current);

        assertEquals(0, actual.position);
        assertEquals(1, actual.inserted.length());
    }

    @Test
    public void deleteAfterInsertShouldNotChangePosition() {
        InsertCommand previous = new InsertCommand(0, 1, "a", 1);
        DeleteCommand current = new DeleteCommand(0, 0, 1, 1);

        DeleteCommand actual = service.transformation(previous, current);

        assertEquals(0, actual.position);
        assertEquals(1, actual.count);
    }

    @ParameterizedTest
    @CsvSource({ "0, 0, 1", "0, 1, 2"})
    public void deleteAfterInsertShouldChangePosition(int previousPosition, int currentPosition, int expectedPosition) {
        InsertCommand previous = new InsertCommand(0, previousPosition, "a", 1);
        DeleteCommand current = new DeleteCommand(0, currentPosition, 1, 1);

        DeleteCommand actual = service.transformation(previous, current);

        assertEquals(expectedPosition, actual.position);
        assertEquals(1, actual.count);
    }

    @Test
    public void deleteAfterDeleteShouldNotChangePosition() {
        DeleteCommand previous = new DeleteCommand(0, 2, 1, 1);
        DeleteCommand current = new DeleteCommand(0, 0, 1, 1);

        DeleteCommand actual = service.transformation(previous, current);

        assertEquals(0, actual.position);
        assertEquals(1, actual.count);
    }

    @ParameterizedTest
    @CsvSource({ "0, 0, 2, 1, 0, 0", "0, 0, 1, 2, 0, 1", "10, 0, 1, 1, 0, 1", "10, 0, 1, 11, 0, 10" })
    public void deleteAfterDeleteShouldChangePositionAndCount(int previousPosition, int currentPosition, int previousCount, int currentCount, int expectedPosition, int expectedCount) {
        DeleteCommand previous = new DeleteCommand(0, previousPosition, previousCount, 1);
        DeleteCommand current = new DeleteCommand(0, currentPosition, currentCount, 1);

        DeleteCommand actual = service.transformation(previous, current);

        assertEquals(expectedPosition, actual.position);
        assertEquals(expectedCount, actual.count);
    }
}