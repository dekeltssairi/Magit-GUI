package sample;

import javafx.event.*;
import javafx.fxml.*;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import logic.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    private MyAmazingGitEngine m_MyAmazingGitEngine;
    @FXML private Label m_UserName;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        m_MyAmazingGitEngine = new MyAmazingGitEngine();
        m_UserName.setText("Administrator");
    }

    @FXML private TextField m_PathToRepository;

    public void  initializeRepository(ActionEvent i_Event) {
        m_MyAmazingGitEngine.LoadExistRepository(m_PathToRepository.getText());
    }

    @FXML
    public void newWindow(ActionEvent i_Event) {
        try {
            //FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("InitializeRepository.fxml"));
           // fxmlLoader.setController(this);
            Parent root =  FXMLLoader.load(getClass().getResource("InitializeRepository.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void SetUserName(ActionEvent i_Event) throws IOException {
        Label secondLabel = new Label("Change UserName");
        TextField textField = new TextField();
        Button button = new Button("Change");

        VBox userNameWindow = new VBox();
        HBox userNameField = new HBox();
        userNameField.getChildren().addAll(secondLabel, textField);
        userNameField.setSpacing(10);
        userNameWindow.getChildren().addAll(userNameField, button);
        userNameWindow.setAlignment(Pos.CENTER);
        userNameField.setAlignment(Pos.CENTER);

        userNameWindow.setSpacing(10);
        Scene secondScene = new Scene(userNameWindow, 230, 100);
        Stage newWindow = new Stage();
        newWindow.setHeight(100);
        newWindow.setWidth(300);
        newWindow.setResizable(false);
        newWindow.setTitle("Set UserName");
        newWindow.setScene(secondScene);
        newWindow.initModality(Modality.APPLICATION_MODAL);
        newWindow.show();

        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                m_MyAmazingGitEngine.SetUsername(textField.getText());
                m_UserName.setText(textField.getText());
                newWindow.close();
            }
        });
    }
}

