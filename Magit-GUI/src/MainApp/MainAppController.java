package MainApp;

import InitializeRepository.InitializeRepositoryController;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.stage.*;
import logic.*;
import java.net.URL;
import java.util.ResourceBundle;

public class MainAppController implements Initializable {

    private MyAmazingGitEngine m_MyAmazingGitEngine;
    @FXML private Label m_UserName;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        m_MyAmazingGitEngine = new MyAmazingGitEngine();
        m_UserName.setText("Administrator");
    }

    @FXML private TextField m_PathToRepository;

    public void  initializeRepository(String i_RepositoryName, String i_RepositoryPathStr) {
        m_MyAmazingGitEngine.CreateRepositoryFromPath(i_RepositoryName,i_RepositoryPathStr);
        // show WC on FXML
    }

    @FXML
    public void InitializeRepositoryWindow(ActionEvent i_Event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/InitializeRepository/InitializeRepository.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            InitializeRepositoryController IRController =(InitializeRepositoryController)fxmlLoader.getController();
            IRController.SetMainAppController(this);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void SetUserName(ActionEvent i_Event){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/SetUserName/SetUserName.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Set username");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}