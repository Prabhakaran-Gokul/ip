package duke;

import java.time.format.TextStyle;
import java.util.Scanner;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Region;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;


/**
 * Duke is the main class that runs the whole program.
 *
 * @author Prabhakaran Gokul
 */
public class Duke extends Application {
    private Storage storage;
    private TaskList taskList;
    private Ui ui;

    private ScrollPane scrollPane;
    private VBox dialogContainer;
    private TextField userInput;
    private Button sendButton;
    private Scene scene;
    private Image user = new Image(this.getClass().getResourceAsStream("/images/user.jpg"));
    private Image duke = new Image(this.getClass().getResourceAsStream("/images/rooney.jpg"));

    /**
     * This constructor initialises the Ui, Storage Classes
     */
    public Duke() {
        this.ui = new Ui();
        this.storage = new Storage();
        try {
            this.taskList = new TaskList(storage.loadFile());
            this.ui.loadingSuccess();
        }
        catch (DukeException e) {
            this.ui.showError(e.getMessage());
            this.taskList = new TaskList();
            this.ui.loadingFailure();
        }
    }

    /**
     * This is the driver of the program.
     * Parser class in initialised here and user inputs are parsed to
     * perform the necessary actions. DukeExceptions are caught and handled here
     */
    public void run() {
        this.ui.welcomeMsg();
        Parser exec = new Parser(taskList);
        Scanner sc = new Scanner(System.in);
        String command;

        while (sc.hasNext()) {
            command = sc.nextLine();
            try {
                exec.executeCommand(command);
            } catch (DukeException e) {
                ui.showError(e.getMessage());
            }
            if (!exec.isAlive) {
                try {
                    this.storage.saveFile(this.taskList.list);
                    ui.byeMsg();
                    sc.close();
                    break;
                }
                catch (DukeException e) {
                    ui.showError(e.getMessage());
                    break;
                }

            }

        }
    }

//    public static void main(String[] args) {
//        new Duke().run();
//        Application.launch(args);
//    }

    @Override
    public void start(Stage stage) {
        // Step 1. Setting up the required components

        //The container for the content of the chat to scroll.
        scrollPane = new ScrollPane();
        dialogContainer = new VBox();
        scrollPane.setContent(dialogContainer);

        userInput = new TextField();
        sendButton = new Button("Send");

        AnchorPane mainLayout = new AnchorPane();
        mainLayout.getChildren().addAll(scrollPane, userInput, sendButton);

        scene = new Scene(mainLayout);

        stage.setScene(scene);
        stage.show();

        //Step 2. Formatting the window to look as expected
        stage.setTitle("Duke");
        stage.setResizable(false);
        stage.setMinHeight(600.0);
        stage.setMinWidth(400.0);

        mainLayout.setPrefSize(400.0, 600.0);

        scrollPane.setPrefSize(385, 535);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

        scrollPane.setVvalue(1.0);
        scrollPane.setFitToWidth(true);

        dialogContainer.setPrefHeight(Region.USE_COMPUTED_SIZE);

        userInput.setPrefWidth(325.0);

        sendButton.setPrefWidth(55.0);

        AnchorPane.setTopAnchor(scrollPane, 1.0);

        AnchorPane.setBottomAnchor(sendButton, 1.0);
        AnchorPane.setRightAnchor(sendButton, 1.0);

        AnchorPane.setLeftAnchor(userInput, 1.0);
        AnchorPane.setBottomAnchor(userInput, 1.0);

        //Step 3. Add functionality to handle user input.
        sendButton.setOnMouseClicked(event -> {
            handleUserInput();
        });

        userInput.setOnAction(event -> {
            handleUserInput();
        });

        //Scroll down to the end every time dialogContainer's height changes.
        dialogContainer.heightProperty().addListener(observable -> scrollPane.setVvalue(1.0));
    }

    /**
     * Iteration 1:
     * Creates a label with the specified text and adds it to the dialog container.
     * @param text String containing text to add
     * @return a label with the specified text that has word wrap enabled.
     */
    private Label getDialogLabel(String text) {
        Label textToAdd = new Label(text);
        textToAdd.setWrapText(true);

        return textToAdd;
    }

    /**
     * Iteration 2:
     * Creates two dialog boxes, one echoing user input and the other containing Duke's reply and then appends them to
     * the dialog container. Clears the user input after processing.
     */
    private void handleUserInput() {
        Label userText = new Label(userInput.getText());
        Label dukeText = new Label(getResponse(userInput.getText()));
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(userText, new ImageView(user)),
                DialogBox.getDukeDialog(dukeText, new ImageView(duke))
        );
        userInput.clear();
    }

    private String getResponse(String input) {
        return "Duke heard: " + input;
    }


}
