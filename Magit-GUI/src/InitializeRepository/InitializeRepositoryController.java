package InitializeRepository;

import MainApp.MainAppController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.nio.file.Path;
import java.nio.file.Paths;

public class InitializeRepositoryController {
    @FXML private TextField m_PathToRepository;
    @FXML private TextField m_RepositoryName;
    private MainAppController m_MainAppController;

    public void SetMainAppController (MainAppController i_MainAppController){
        m_MainAppController = i_MainAppController;
    }

    public void LoadBtnAction(ActionEvent i_Event){
        String repositoryPathStr = m_PathToRepository.getText();
        String repositoryName = m_RepositoryName.getText();
        Path repositoryPath = Paths.get(repositoryPathStr);
        if (!repositoryPath.toFile().exists()){
            m_MainAppController.initializeRepository(repositoryPath.toString(), repositoryName);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succescfully Initilized");
            alert.setHeaderText("Succescfully Initilized");
            alert.setContentText("Succescfully Initilized Repository in input path");
            alert.showAndWait();
            Stage stage = (Stage)((Button)i_Event.getSource()).getScene().getWindow();
            stage.close();
        }
        else{

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Illegal input");
            alert.setContentText("The Path is Already existed!");
            alert.showAndWait();
        }

    }
}
