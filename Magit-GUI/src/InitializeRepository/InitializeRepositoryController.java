package InitializeRepository;

import MainApp.MainAppController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class InitializeRepositoryController {
    @FXML private TextField m_PathToRepository;
    @FXML private TextField m_RepositoryName;
    private MainAppController m_MainAppController;

    public void LoadBtnAction(ActionEvent i_Event){
        // Validations
        // show errors
        m_MainAppController.initializeRepository(m_RepositoryName.getText(), m_PathToRepository.getText());
    }

    public void SetMainAppController (MainAppController i_MainAppController){
        m_MainAppController = i_MainAppController;
    }
}
