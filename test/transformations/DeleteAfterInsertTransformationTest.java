package transformations;

import command.CommandBase;
import command.CommandType;
import command.DeleteCommand;
import command.InsertCommand;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DeleteAfterInsertTransformationTest {

    private CommandTransformation transformation;

    @BeforeEach
    public void beforeTest() {
        transformation = new DeleteAfterInsertTransformation();
    }

    @AfterEach
    public void afterTest() {
        transformation = new DeleteAfterInsertTransformation();
    }

    @Test
    void getPreviousCommandType() {
        CommandType actual = transformation.getPreviousCommandType();

        assertEquals(CommandType.INSERT, actual);
    }

    @Test
    void getCurrentCommandType() {
        CommandType actual = transformation.getCurrentCommandType();

        assertEquals(CommandType.DELETE, actual);
    }

    @Test
    public void transformationShouldNotChangePosition() {
        CommandBase previous = new InsertCommand(0, 1, "a", 1);
        CommandBase current = new DeleteCommand(0, 0, 1, 1);

        DeleteCommand actual = (DeleteCommand) transformation.transformation(previous, current);

        assertEquals(0, actual.position);
        assertEquals(1, actual.count);
    }

    @ParameterizedTest
    @CsvSource({ "0, 0, 1", "0, 1, 2"})
    public void transformationShouldChangePosition(int previousPosition, int currentPosition, int expectedPosition) {
        CommandBase previous = new InsertCommand(0, previousPosition, "a", 1);
        CommandBase current = new DeleteCommand(0, currentPosition, 1, 1);

        DeleteCommand actual = (DeleteCommand) transformation.transformation(previous, current);

        assertEquals(expectedPosition, actual.position);
        assertEquals(1, actual.count);
    }
}