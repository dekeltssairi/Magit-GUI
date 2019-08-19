package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.Window;
import logic.*;
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
    
    public void SetUserName(ActionEvent i_Event){
        Label secondLabel = new Label("Change UserName");
        TextField textField = new TextField();
        Button button = new Button("Change");
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                m_MyAmazingGitEngine.SetUsername(textField.getText());
                m_UserName.setText(textField.getText());
            }
        });
        HBox secondaryLayout = new HBox();
        secondaryLayout.getChildren().addAll(secondLabel, textField, button);

        Scene secondScene = new Scene(secondaryLayout, 230, 100);

        // New window (Stage)
        Stage newWindow = new Stage();
        newWindow.setTitle("Set UserName");
        newWindow.setScene(secondScene);
        newWindow.show();
    }
}

