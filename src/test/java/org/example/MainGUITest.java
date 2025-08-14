package org.example;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

import java.io.*;
import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MainGUITest {

    private Student student;

    @BeforeEach
    void setup() {
        student = new Student("Alice", "S101", CourseType.MAJOR, "alice@mail.com", "0123456789");
    }

    // -------------------------
    // ORIGINAL TESTS
    // -------------------------

    @Test
    void testCourseTypeEnum() {
        assertEquals("MAJOR", CourseType.MAJOR.name());
        assertEquals(3, CourseType.values().length);
    }

    @Test
    void testStudentGetters() {
        assertEquals("Alice", student.getStudentName());
        assertEquals("S101", student.getStudentId());
        assertEquals("alice@mail.com", student.getStudentEmail());
        assertEquals("0123456789", student.getphoneNumber());
        assertEquals(CourseType.MAJOR, student.getCourseType());
    }

    @Test
    void testStudentToStringContainsName() {
        String output = student.toString();
        assertTrue(output.contains("Alice"));
        assertTrue(output.contains("MAJOR"));
    }

    @Test
    void testSignupSaveAndLoadUsers() throws Exception {
        HashMap<String, String> users = new HashMap<>();
        users.put("testUser", "1234");

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("users.dat"))) {
            oos.writeObject(users);
        }

        HashMap<String, String> loadedUsers;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("users.dat"))) {
            loadedUsers = (HashMap<String, String>) ois.readObject();
        }

        assertTrue(loadedUsers.containsKey("testUser"));
        assertEquals("1234", loadedUsers.get("testUser"));
    }

    @Test
    void testLoginLoadUsersEmpty() throws Exception {
        File file = new File("users.dat");
        if (file.exists()) file.delete();

        HashMap<String, String> users;
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("users.dat"));
            users = (HashMap<String, String>) ois.readObject();
        } catch (Exception e) {
            users = new HashMap<>();
        }

        assertTrue(users.isEmpty());
    }

    @Test
    void testSaveStudentList() throws Exception {
        List<Student> list = new ArrayList<>();
        list.add(student);

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("hello.txt"))) {
            out.writeObject(list);
        }

        assertTrue(new File("hello.txt").exists());
    }

    @Test
    void testLoadStudentList() throws Exception {
        List<Student> list = new ArrayList<>();
        list.add(student);

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("hello.txt"))) {
            out.writeObject(list);
        }

        List<Student> loadedList;
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("hello.txt"))) {
            loadedList = (List<Student>) in.readObject();
        }

        assertEquals(1, loadedList.size());
        assertEquals("Alice", loadedList.get(0).getStudentName());
    }

    @Test
    void testMultipleStudentsSerialization() throws Exception {
        List<Student> list = Arrays.asList(
                new Student("Bob", "S102", CourseType.NON_MAJOR, "bob@mail.com", "01999"),
                new Student("Carol", "S103", CourseType.OPTIONAL, "carol@mail.com", "01888")
        );

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("hello.txt"))) {
            out.writeObject(list);
        }

        List<Student> loadedList;
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("hello.txt"))) {
            loadedList = (List<Student>) in.readObject();
        }

        assertEquals(2, loadedList.size());
        assertEquals("Bob", loadedList.get(0).getStudentName());
    }

    @Test
    void testStudentEqualityManually() {
        Student s2 = new Student("Alice", "S101", CourseType.MAJOR, "alice@mail.com", "0123456789");
        assertNotSame(student, s2);
        assertEquals(student.getStudentId(), s2.getStudentId());
    }

    @Test
    void testStudentHasCorrectCourseType() {
        assertEquals(CourseType.MAJOR, student.getCourseType());
    }

    @Test
    void testEmptyFieldSignupLogic() {
        String username = "";
        String password = "";
        assertTrue(username.isEmpty() || password.isEmpty());
    }

    @Test
    void testAddUserToMap() {
        HashMap<String, String> users = new HashMap<>();
        users.put("newUser", "pass");
        assertTrue(users.containsKey("newUser"));
    }

    @Test
    void testDeleteStudentFromList() {
        List<Student> list = new ArrayList<>();
        list.add(student);
        list.removeIf(s -> s.getStudentId().equals("S101"));
        assertTrue(list.isEmpty());
    }

    @Test
    void testSearchStudentById() {
        List<Student> list = new ArrayList<>();
        list.add(student);
        boolean found = list.stream().anyMatch(s -> s.getStudentId().equals("S101"));
        assertTrue(found);
    }

    @Test
    void testFileExistsAfterSave() throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("hello.txt"))) {
            out.writeObject(new ArrayList<Student>());
        }
        assertTrue(new File("hello.txt").exists());
    }

    // -------------------------
    // PARAMETERIZED TESTS
    // -------------------------

    @ParameterizedTest
    @ValueSource(strings = {"MAJOR", "NON_MAJOR", "OPTIONAL"})
    void testCourseTypeEnumParameterized(String typeName) {
        assertTrue(Arrays.stream(CourseType.values())
                .anyMatch(c -> c.name().equals(typeName)));
    }

    @ParameterizedTest
    @CsvSource({
            "'', ''",
            "'Alice', ''",
            "'', '1234'",
            "'Alice', '1234'"
    })
    void testSignupEmptyFieldsParameterized(String username, String password) {
        boolean isEmpty = username.isEmpty() || password.isEmpty();
        if (username.isEmpty() || password.isEmpty()) {
            assertTrue(isEmpty);
        } else {
            assertFalse(isEmpty);
        }
    }

    static Stream<Arguments> studentProvider() {
        return Stream.of(
                Arguments.of("Alice", "S101", CourseType.MAJOR),
                Arguments.of("Bob", "S102", CourseType.NON_MAJOR),
                Arguments.of("Carol", "S103", CourseType.OPTIONAL)
        );
    }

    @ParameterizedTest
    @MethodSource("studentProvider")
    void testStudentCreationParameterized(String name, String id, CourseType type) {
        Student s = new Student(name, id, type, name.toLowerCase() + "@mail.com", "01234");
        assertEquals(name, s.getStudentName());
        assertEquals(id, s.getStudentId());
        assertEquals(type, s.getCourseType());
    }

    // -------------------------
    // MOCKITO TESTS
    // -------------------------

    @Test
    void testMockStudentBehavior() {
        // Create a mock Student
        Student mockStudent = Mockito.mock(Student.class);

        // Define behavior
        when(mockStudent.getStudentName()).thenReturn("MockedName");
        when(mockStudent.getStudentId()).thenReturn("M101");

        // Verify mock behavior
        assertEquals("MockedName", mockStudent.getStudentName());
        assertEquals("M101", mockStudent.getStudentId());

        // Verify method call count
        verify(mockStudent, times(1)).getStudentName();
        verify(mockStudent, times(1)).getStudentId();
    }

    @Test
    void testMockServiceInteraction() {
        // Create a mock service (simulate a save operation)
        SignupService mockService = mock(SignupService.class);

        // Call the method
        mockService.saveUser("Alice", "1234");

        // Verify interaction
        verify(mockService, times(1)).saveUser("Alice", "1234");
    }

    // Dummy interface for demonstration
    interface SignupService {
        void saveUser(String username, String password);
    }
}
