package Functional;

import Authentication.SessionManager;
import PageControllers.NewAppointmentControllerCalendar;
import Model.Customer;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.base.NodeMatchers.*;

public class NewAppointmentControllerCalendarTest extends ApplicationTest {

    private NewAppointmentControllerCalendar controller;
    private Stage stage;

    @Override
    public void start(Stage stage) throws Exception {
        Customer customer = new Customer("Mario", "Rossi", "m.rossi@example.com", "securePass123", "1234567890");
        SessionManager.getInstance().setCurrentUser(customer);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/NewAppointmentCalendar.fxml"));
        Parent root = loader.load();
        controller = loader.getController();

        this.stage = stage;
        stage.setScene(new Scene(root));
        stage.setTitle("New Appointment Calendar");
        stage.show();

        WaitForAsyncUtils.waitForFxEvents();
        Thread.sleep(500);
    }

    private void clickAndWait(String nodeId, int times) {
        for (int i = 0; i < times; i++) {
            clickOn(nodeId);
            WaitForAsyncUtils.waitForFxEvents();
        }
    }

    @Test
    public void testCalendarLoadsCorrectMonthAndYear() {
        Label yearMonthLabel = lookup("#yearMonthLabel").query();
        String expected = YearMonth.now().format(DateTimeFormatter.ofPattern("yyyy MMMM", Locale.ENGLISH));
        assertEquals(expected, yearMonthLabel.getText());
    }

    @Test
    public void testNextAndPreviousMonthButtons() throws Exception {
        String initialLabel = lookup("#yearMonthLabel").queryLabeled().getText();

        clickAndWait("#nextButton", 5);

        String afterNext = lookup("#yearMonthLabel").queryLabeled().getText();
        assertNotEquals(initialLabel, afterNext);

        clickAndWait("#previousButton", 5);

        String afterPrevious = lookup("#yearMonthLabel").queryLabeled().getText();
        assertEquals(initialLabel, afterPrevious);
    }

    @Test
    public void testTodayButtonVisibilityAndFunction() throws Exception {
        clickAndWait("#nextButton", 5);

        verifyThat("#todayButton", isVisible());

        clickOn("#todayButton");
        WaitForAsyncUtils.waitForFxEvents();

        verifyThat("#previousButton", isDisabled());
    }

    @Test
    public void testClickOnDayCellNavigatesToSlotsView() throws Exception {
        StackPane dayCell = lookup(".day-cell").lookup((Predicate<Node>) node ->
                node instanceof StackPane && !node.isDisabled()).query();
        clickOn(dayCell);
        WaitForAsyncUtils.waitForFxEvents();
        assertEquals("Slots", stage.getTitle());
    }

    @Test
    public void testGoToAppointmentsView() {
        clickOn("#appointmentsButton");
        WaitForAsyncUtils.waitForFxEvents();
        assertEquals("Appointments", stage.getTitle());
    }

    @Test
    public void testGoToNewsView() {
        clickOn("#newsButton");
        WaitForAsyncUtils.waitForFxEvents();
        assertEquals("News", stage.getTitle());
    }

    @Test
    public void testGoToProfileView() {
        clickOn("#profileButton");
        WaitForAsyncUtils.waitForFxEvents();
        assertEquals("Profile", stage.getTitle());
    }
}