package transformations;

import command.CommandType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InsertAfterDeleteTransformationTest {

    private InsertAfterDeleteTransformation transformation;

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

    @Test
    void transformation() {
    }
}