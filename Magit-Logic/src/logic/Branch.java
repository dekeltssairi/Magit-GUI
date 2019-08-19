package logic;
import java.nio.file.*;
import static DekelNoy3rd.Service.Methods.*;

public class Branch {
    private String m_Name;
    private Commit m_Commit;
    private Path m_Path;

    public Branch(String i_newBranchName, Commit i_Commit, Path i_Path) {
        m_Name = i_newBranchName;
        m_Commit = i_Commit;
        m_Path = i_Path; // path to specific branch.txt
        // can be far better
        createBranchTextFile(); // this suppose to override
    }

    public void createBranchTextFile() {
        String pathStr = m_Path.normalize().toString();
        String content;
        if (m_Commit == null) {
            content = "";
        } else {
            content = m_Commit.GetSha1();
        }
        CreateTextFile(pathStr, content);
        /// we need to read and see if not empty
    }

    public Path GetPath() {
        return m_Path;
    }

    public String GetName() {
        return m_Name;
    }

    public Commit GetCommit() {
        return m_Commit;
    }

    public void SetCommit(Commit i_Commit) {
        m_Commit = i_Commit;
    }

    @Override
    public String toString() {
        String nameBranchPresentation = "Name branch:  " + m_Name + System.lineSeparator();
        String Sha1OfCommitBranchPointTo = "SHA_1 of commit Branch point to is: " + m_Commit.GetSha1() + System.lineSeparator();
        String messageOfTheCommit = "Message of the commit: " + m_Commit.GetMessage() + System.lineSeparator();
        String branchDetails = nameBranchPresentation + Sha1OfCommitBranchPointTo + messageOfTheCommit;

        return branchDetails;
    }

    public String ShowCommit() {
        return m_Commit.toString();
    }

    public void DeleteBranchTextFile(){
        Path path = m_Path;
        m_Path = null;
        DekelNoy3rd.Service.Methods.DeleteTextFile(path);
    }

    public void Deploy(){
        if (m_Commit != null) {
            m_Commit.DeployRec(m_Commit.GetPathToMainFolder(), m_Commit.GetMainFolderSha_1());
        }
    }
}