package transformations;

import command.CommandBase;
import command.CommandType;
import command.DeleteCommand;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DeleteAfterDeleteTransformationTest {

    private CommandTransformation transformation;

    @BeforeEach
    public void beforeTest() {
        transformation = new DeleteAfterDeleteTransformation();
    }

    @AfterEach
    public void afterTest() {
        transformation = new DeleteAfterDeleteTransformation();
    }

    @Test
    void getPreviousCommandType() {
        CommandType actual = transformation.getPreviousCommandType();

        assertEquals(CommandType.DELETE, actual);
    }

    @Test
    void getCurrentCommandType() {
        CommandType actual = transformation.getCurrentCommandType();

        assertEquals(CommandType.DELETE, actual);
    }

    @Test
    public void transformationShouldNotChangePosition() {
        CommandBase previous = new DeleteCommand(0, 2, 1, 1);
        CommandBase current = new DeleteCommand(0, 0, 1, 1);

        DeleteCommand actual = (DeleteCommand) transformation.transformation(previous, current);

        assertEquals(0, actual.position);
        assertEquals(1, actual.count);
    }

    @ParameterizedTest
    @CsvSource({ "0, 0, 2, 1, 0, 0", "0, 0, 1, 2, 0, 1", "10, 0, 1, 1, 0, 1", "10, 0, 1, 11, 0, 10" })
    public void transformationShouldChangePositionAndCount(int previousPosition, int currentPosition, int previousCount, int currentCount, int expectedPosition, int expectedCount) {
        CommandBase previous = new DeleteCommand(0, previousPosition, previousCount, 1);
        CommandBase current = new DeleteCommand(0, currentPosition, currentCount, 1);

        DeleteCommand actual = (DeleteCommand) transformation.transformation(previous, current);

        assertEquals(expectedPosition, actual.position);
        assertEquals(expectedCount, actual.count);
    }
}