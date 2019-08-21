package logic;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import static DekelNoy3rd.Service.Methods.*;

public class Head {
    private Path m_Path;
    private Branch m_ActiveBranch;
    private BranchLoader m_BranchLoader;

    public Head(Path i_Path, Map<String,Commit> i_sha_1CommitMap) {
        m_Path = i_Path;
        m_BranchLoader = new BranchLoader(m_Path.getParent().getParent());
        CreateHeadTxtFile();
        m_ActiveBranch = restoreActiveBranch(i_sha_1CommitMap);
    }

    private void CreateHeadTxtFile() {
        File file = new File(m_Path.toString());
        if (!file.exists()){
            CreateTextFile(m_Path.toString(),"master");
            CreateTextFile(m_Path.getParent().toString() + "/master","");
        }
    }

    public Branch GetActiveBranch() {
        return m_ActiveBranch;
    }

    public void SetActiveBranch(Branch i_ActiveBranch) {
        m_ActiveBranch = i_ActiveBranch;
        DekelNoy3rd.Service.Methods.CreateTextFile(m_Path.toString(), i_ActiveBranch.GetName());
    }

    private Branch restoreActiveBranch(Map<String,Commit> i_sha_1CommitMap) {
        Branch activeBranch = null;
        try {

            String activeBranchName = new Scanner(new File(m_Path.toString())).nextLine();
            Path pathToActiveBranch = Paths.get(m_Path.getParent().toString() + "/" + activeBranchName).normalize();
            activeBranch = m_BranchLoader.loadBranch(pathToActiveBranch, i_sha_1CommitMap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();}
        return activeBranch;
    }

    public String ShowCommit() {
        return m_ActiveBranch.ShowCommit();
    }

    public boolean IsHeadBranch(String i_BranchName) {
        return m_ActiveBranch.GetName().equals(i_BranchName);
    }

    public void DeployHeadBranch() {
        m_ActiveBranch.Deploy();
    }

    public Commit GetActiveCommit() {
        return m_ActiveBranch.GetCommit();
    }
}
