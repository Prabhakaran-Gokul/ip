import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StorageTest {
    @Test
    public void taskToString_correctInputs_success() {
        Deadline d = new Deadline("return book", "Sunday");
        d.isDone = true;
        assertEquals("D, 1, return book, Sunday", new Storage().taskToString(d));
    }

    @Test
    public void stringToTask_correctInputs_success() throws Exception{
        ToDo t = new ToDo("make food");
        assertEquals(t.taskDescription, new Storage().stringToTask("T, 0, make food").taskDescription);
    }
}