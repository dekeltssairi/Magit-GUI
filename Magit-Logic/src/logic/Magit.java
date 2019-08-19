package logic;

import java.io.*;
import java.nio.file.*;
import java.util.*;;
import static DekelNoy3rd.Service.Methods.*;

public class Magit {
    private List<Branch> m_Branches;
    private Path m_Path;
    private SHA_1Map m_Objects;
    private Head m_Head;
    private Map<String, Commit> m_Commits;
    private BranchLoader m_BranchLoader;

    public Magit(String i_RepositoryName, Path i_Path) {
        m_Branches = new ArrayList<>();
        m_Path = i_Path;
        m_Objects = new SHA_1Map(Paths.get(i_Path.toString() + "/MAP"));
        m_BranchLoader = new BranchLoader(m_Path);
        m_Commits = new HashMap<>();
        m_Head = new Head(Paths.get(i_Path + "/Branches/HEAD"), m_Commits);
        m_Branches.add(m_Head.GetActiveBranch());
        CreateTextFile(i_Path.toString() + "/Name", i_RepositoryName);
        loadNonActiveBranches();
    }

    public List<Branch> GetBranches() {return m_Branches;}

    public Path GetPath() {
        return m_Path;
    }

    public Map<String, Commit> GetCommits() {
        return m_Commits;
    }

    public Branch GetActiveBranch() {return m_Head.GetActiveBranch();}

    public SHA_1Map GetObjects() {return m_Objects;}

    public Head GetHead() {
        return m_Head;
    }

    private void loadNonActiveBranches() {
        Path pathToBranchesFolder = Paths.get(m_Path + "/Branches");
        File[] branchesTextFiles = new File(pathToBranchesFolder.toString()).listFiles();
        for (int i = 0; i < branchesTextFiles.length; i++) {
            Path branchPath = Paths.get(branchesTextFiles[i].getAbsolutePath());
            boolean isActiveBranch = branchPath.equals(m_Head.GetActiveBranch().GetPath().normalize());
            boolean isHeadFile = (branchPath.getName(branchPath.getNameCount() - 1)).toString().equals("HEAD");
            if (!isActiveBranch && !isHeadFile)
                m_Branches.add(m_BranchLoader.loadBranch(branchPath,m_Commits));

        }
    }

    public String BranchesDetails(){
        String branchDetails = "";
        if (m_Head.GetActiveBranch().GetCommit() == null)
        {
            branchDetails = "There are no branches which point to any commit yet!";
        }
        else {
            branchDetails = "All branches details:" + System.lineSeparator();
            branchDetails += "=====================" + System.lineSeparator();
            for (Branch branch : m_Branches) {
                branchDetails += branch.toString();
                if (branch == m_Head.GetActiveBranch()) {
                    branchDetails += "This is the head branch" + System.lineSeparator();
                }
                branchDetails += "================================================" + System.lineSeparator();
            }
        }
        return branchDetails;
    }

    public Commit GetActiveCommit(){
        return m_Head.GetActiveCommit();
    }

    public boolean IsExistBranch (String i_BranchName){
        boolean isExistBranch = false;
        for (Branch branch : m_Branches) {
            if (branch.GetName().equals(i_BranchName)){
                isExistBranch = true;
            }
        }
        return isExistBranch;
    }

    public void CreateNewBranch(String i_BranchName){
        Commit commit = m_Head.GetActiveBranch().GetCommit();
        Path newBranchPath = Paths.get(m_Path.toString() + "/Branches/" + i_BranchName);
        Branch newBranch = new Branch(i_BranchName,commit,newBranchPath);
        m_Branches.add(newBranch);
    }

    public boolean IsHeadBranch(String i_BranchName) {
        return m_Head.IsHeadBranch(i_BranchName);
    }

    public String ShowCurrrentCommit() {
        return m_Head.ShowCommit();
    }

    public String ShowCurrrentCommitHistory() {

        Commit currentCommit = m_Head.GetActiveBranch().GetCommit();
        String sha_1MainFolder = currentCommit.GetMainFolderSha_1();
        Path pathToMainFolder = Paths.get(m_Path.getParent().toString()).normalize();
        return deployFolderAsStringBySha_1Rec(sha_1MainFolder, pathToMainFolder);
    }

    private String deployFolderAsStringBySha_1Rec(String i_Sha_1Folder, Path i_PathToFolder) {
        Path pathToZipContainFolder = m_Objects.GetStringZipFilesMap().get(i_Sha_1Folder).GetPath();
        String zipContent = ReadFromZip(pathToZipContainFolder.toString());
        zipContent = fixString(zipContent);
        String[] linesOfZip = zipContent.split(System.lineSeparator());
        String details = "";
        for(String str: linesOfZip) {
            String[] output = str.split(",");
            String subFileFullName = i_PathToFolder.toString() + "\\" + output[0];
            String subFileSha_1 = output[1];
            String subFileType = output[2];
            String subFileModifier = output[3];
            String SubFileDateModification = output[4];
            String  subFileDetails = "Full Name: " + subFileFullName + System.lineSeparator() +
                    "Type: " + subFileType + System.lineSeparator() +
                    "Sha_1: " + subFileSha_1 + System.lineSeparator() +
                    "Last Modifier: " + subFileModifier + System.lineSeparator() +
                    "Last modification: " + SubFileDateModification + System.lineSeparator() + System.lineSeparator();
            if (subFileType.equals("Folder")) {
                details +=  deployFolderAsStringBySha_1Rec(subFileSha_1, Paths.get(subFileFullName));
            }
            details+= subFileDetails;
        }
        return details;
    }

    public void DeleteBranch(String branchName) {
        Branch branchToRemove = null;

        for (Branch branch: m_Branches)
        {
            if (branch.GetName().equals(branchName)){
                branchToRemove = branch;
            }
        }
        branchToRemove.DeleteBranchTextFile();
        m_Branches.remove(branchToRemove);

    }

    public String BranchCommitHistory() {
        String branchCommitsHistory = "";
        Commit currentCommit = m_Head.GetActiveBranch().GetCommit();
        while(currentCommit != null){
            branchCommitsHistory += currentCommit.ShowCommitDetails();
            currentCommit = currentCommit.GetParentCommit();
        }
        if(branchCommitsHistory.equals("")){
            branchCommitsHistory = "This branch does not point for any committ";
        }
        return branchCommitsHistory;
    }

    public boolean HasBranchesExceptHead() {
        return m_Branches.size() > 1;
    }

    public void UpdateMapFile() {
        Map<String, ZipFile> i_stringZipFilesMap = m_Objects.GetStringZipFilesMap();
        String mapStr = "";
        boolean firstIteration = true;
        for (Map.Entry<String,ZipFile> entry : i_stringZipFilesMap.entrySet()) {
            if (!firstIteration){
                mapStr+= System.lineSeparator();
            }
            firstIteration = false;
            mapStr += entry.getKey() + ",";
            mapStr += entry.getValue().toString();
        }
        CreateTextFile(m_Objects.GetPathToMapFile().toString(),mapStr );
    }

    public void DeployHeadBranch() {
        m_Head.DeployHeadBranch();
    }

    public void SwitchHeadBranch(String i_BranchName) {
        Branch newActiveBranch = null;
        for (Branch branch : m_Branches){
            if(branch.GetName().equals(i_BranchName)){
                newActiveBranch = branch;
            }
        }
        m_Head.SetActiveBranch(newActiveBranch);
    }
}