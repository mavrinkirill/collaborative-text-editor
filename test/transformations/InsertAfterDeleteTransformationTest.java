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

class InsertAfterDeleteTransformationTest {

    private CommandTransformation transformation;

    @BeforeEach
    public void beforeTest() {
        transformation = new InsertAfterDeleteTransformation();
    }

    @AfterEach
    public void afterTest() {
        transformation = new InsertAfterDeleteTransformation();
    }

    @Test
    void getPreviousCommandTypeShouldBeDelete() {
        CommandType actual = transformation.getPreviousCommandType();

        assertEquals(CommandType.DELETE, actual);
    }

    @Test
    void getCurrentCommandTypeShouldBeInsert() {
        CommandType actual = transformation.getCurrentCommandType();

        assertEquals(CommandType.INSERT, actual);
    }

    @ParameterizedTest
    @CsvSource({ "1, 0, 0", "0, 0, 0" })
    public void transformationShouldNotChangePosition(int firstPosition, int secondPosition, int expectedPosition) {
        CommandBase previous = new DeleteCommand(0, firstPosition, 1, 1);
        CommandBase current = new InsertCommand(0, secondPosition, "a", 1);

        InsertCommand actual = (InsertCommand) transformation.transformation(previous, current);

        assertEquals(expectedPosition, actual.position);
        assertEquals(1, actual.inserted.length());
    }

    @Test
    public void transformationShouldChangePosition() {
        DeleteCommand previous = new DeleteCommand(0, 0, 1, 1);
        InsertCommand current = new InsertCommand(0, 1, "a", 1);

        InsertCommand actual = (InsertCommand) transformation.transformation(previous, current);

        assertEquals(0, actual.position);
        assertEquals(1, actual.inserted.length());
    }
}