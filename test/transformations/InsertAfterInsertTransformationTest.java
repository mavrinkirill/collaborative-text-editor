package transformations;

import command.CommandBase;
import command.CommandType;
import command.InsertCommand;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.jupiter.api.Assertions.assertEquals;

class InsertAfterInsertTransformationTest {

    private CommandTransformation transformation;

    @BeforeEach
    public void beforeTest() {
        transformation = new InsertAfterInsertTransformation();
    }

    @AfterEach
    public void afterTest() {
        transformation = new InsertAfterInsertTransformation();
    }

    @Test
    void getPreviousCommandTypeShouldBeInsert() {
        CommandType actual = transformation.getPreviousCommandType();

        assertEquals(CommandType.INSERT, actual);
    }

    @Test
    void getCurrentCommandTypeShouldBeInsert() {
        CommandType actual = transformation.getCurrentCommandType();

        assertEquals(CommandType.INSERT, actual);
    }

    @Test
    public void transformationShouldNotChangePosition() {
        CommandBase previous = new InsertCommand(0, 1, "a", 1);
        CommandBase current = new InsertCommand(0, 0, "a", 1);

        CommandBase actual = transformation.transformation(previous, current);

        assertEquals(0, actual.position);
        assertThat(actual, instanceOf(InsertCommand.class));
    }

    @ParameterizedTest
    @CsvSource({ "1, 1, 0", "1, 2, 0"})
    void transformationShouldNotChangePosition(int firstAuthorId, int secondAuthorId, int expectedPosition) {
        CommandBase previous = new InsertCommand(0, 0, "a", firstAuthorId);
        CommandBase current = new InsertCommand(0, 0, "a", secondAuthorId);

        InsertCommand actual = (InsertCommand) transformation.transformation(previous, current);

        assertEquals(expectedPosition, actual.position);
        assertEquals(1, actual.inserted.length());
    }

    @ParameterizedTest
    @CsvSource({ "0, 1, 1, 1, 2", "0, 0, 2, 1, 1"})
    public void transformationShouldChangePosition(int firstPosition, int secondPosition, int firstAuthorId, int secondAuthorId, int expectedPosition) {
        CommandBase previous = new InsertCommand(0, firstPosition, "a", firstAuthorId);
        CommandBase current = new InsertCommand(0, secondPosition, "a", secondAuthorId);

        InsertCommand actual = (InsertCommand) transformation.transformation(previous, current);

        assertEquals(expectedPosition, actual.position);
        assertEquals(1, actual.inserted.length());
    }
}