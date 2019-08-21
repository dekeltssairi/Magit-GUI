package logic;

import java.io.*;
import java.nio.file.*;
import java.text.*;
import java.util.*;
import static DekelNoy3rd.Service.Methods.*;


public class Repository {
    private String m_Name;
    private Path m_Path;
    private WorkingCopy m_WC;
    private Magit m_Magit;
    private Delta m_Delta;

    public Repository(String i_Name, String i_PathStr, UserName i_UserName){
        m_Name = i_Name;
        m_Path = Paths.get(i_PathStr);
        m_Magit = new Magit(i_Name ,Paths.get(m_Path.toString() + "/.magit"));
        m_WC = new WorkingCopy(m_Path);
        m_Delta = new Delta(m_Path);
    }

    public Magit GetMagit() {return m_Magit;}

    public WorkingCopy GetWC() {return m_WC; }

    public String GetMainFolderSha_1OfActiveCommit() {
        return m_Magit.GetActiveCommit().GetMainFolderSha_1();
    }

    public void Commit(String i_CommitMessage, UserName i_UserName) {
        createZipFilesFromCurrentWc(m_WC.GetMainFolder());
        UpdateMapFile();
        Commit newCommit = createTheCommit(i_CommitMessage, i_UserName, m_WC.GetPath());
        m_Magit.GetCommits().put(newCommit.GetSha1(), newCommit);
        UpdateActiveBranch(newCommit);
    }

    public void UpdateMapFile() {
        m_Magit.UpdateMapFile();
    }

    public boolean IsSomethingToCommit(UserName i_UserName){
        m_WC.GenerateSystemFiles(i_UserName, m_Magit.GetObjects());
        boolean isThereIsActiveCommit =  m_Magit.GetActiveBranch().GetCommit() != null;
        boolean isSomethingToCommit;
        if (isThereIsActiveCommit) {
            String wcSha_1 = m_WC.GetMainFolder().GetSha_1();
            String activeCommitSha_1 = GetMainFolderSha_1OfActiveCommit();
            isSomethingToCommit = !wcSha_1.equals(activeCommitSha_1);
        }
        else {
            isSomethingToCommit = true;
        }
        return isSomethingToCommit;
    }

    private void UpdateActiveBranch(Commit i_NewCommit) {
        m_Magit.GetHead().GetActiveBranch().SetCommit(i_NewCommit);
        createTheTextFileCommitInComittedArea(i_NewCommit);
        m_Magit.GetActiveBranch().createBranchTextFile();
    }

    private void createZipFilesFromCurrentWc(RepositoryFile i_RepositoryFile) {
        createZipFilesFromCurrentWcRec(i_RepositoryFile);
    }

    private Commit createTheCommit(String i_CommitMessage, UserName i_UserName, Path i_PathToWc) {
        Commit newCommit = createTheObjectCommitInMagitObjects(i_CommitMessage,i_UserName, i_PathToWc);
        return newCommit;

    }

    private void createTheTextFileCommitInComittedArea(Commit i_Commit) {
        String pathToNewCommitInCommitted = m_Path.toString() +"/.magit/" +i_Commit.GetSha1();
        String contentFile = i_Commit.toString();
        CreateTextFile(pathToNewCommitInCommitted, contentFile);
    }

    private Commit createTheObjectCommitInMagitObjects(String i_CommitMessage, UserName i_UserName, Path i_PathToWc) {
        String sha_1MainFolder = m_WC.GetMainFolder().GetSha_1();
        Commit parentCommit = m_Magit.GetActiveBranch().GetCommit();
        Date commitDate = new Date();
        Commit newCommit = new Commit(sha_1MainFolder, parentCommit, i_CommitMessage, commitDate, i_UserName, i_PathToWc);
        return newCommit;
    }

    private void createZipFilesFromCurrentWcRec(RepositoryFile i_RepositoryFile) {
        if (i_RepositoryFile != null){
            String sha_1_RepositoryFile = i_RepositoryFile.GetSha_1();
            if (!m_Magit.GetObjects().GetStringZipFilesMap().containsKey(sha_1_RepositoryFile)){
                if (i_RepositoryFile instanceof Folder){
                    Folder folder = ((Folder) i_RepositoryFile);
                    int numOfFilesInFolder = folder.GetRepositoryFiles().size();
                    List <RepositoryFile> innerRepositoryFiles = folder.GetRepositoryFiles();

                    for (int i = 0; i < numOfFilesInFolder; i++){
                        createZipFilesFromCurrentWcRec(innerRepositoryFiles.get(i));
                    }
                }
                CreateZipFromRepositoryFileInObjectsFolder(i_RepositoryFile);
                AddZipToMap(i_RepositoryFile);
            }
        }
    }

    public void AddZipToMap(RepositoryFile i_RepositoryFile) {
        Path zipPath = Paths.get(m_Magit.GetPath() + "/Objects/" + i_RepositoryFile.GetSha_1() + ".zip");
        ZipFile zipFile = new ZipFile(zipPath,new UserName(i_RepositoryFile.GetCommitter()),i_RepositoryFile.GetPath());
        m_Magit.GetObjects().GetStringZipFilesMap().put(i_RepositoryFile.GetSha_1(), zipFile);
    }

    private void CreateZipFromRepositoryFileInObjectsFolder(RepositoryFile i_RepositoryFile) {
        CreateTemporaryTxtFileInObjectsFromRepositoryFile(i_RepositoryFile);
        CreateZipFromTemporaryTextFile(i_RepositoryFile);
        new File(m_Magit.GetPath().toString() + "/Objects/" + i_RepositoryFile.GetName()).delete();

    }

    private void CreateZipFromTemporaryTextFile(RepositoryFile i_RepositoryFile) {
        String zipName = i_RepositoryFile.GetSha_1() + ".zip";
        String destination =  m_Path.toString()+"/.magit/Objects/" + zipName;
        String source = m_Path.toString()+"/.magit/Objects/" + i_RepositoryFile.GetName();
        Zip(Paths.get(source), Paths.get(destination));
    }

    private void CreateTemporaryTxtFileInObjectsFromRepositoryFile(RepositoryFile i_RepositoryFile) {
        String destenationPath = m_Magit.GetPath().toString() + "/Objects/" + i_RepositoryFile.GetName();
        String contentFile = i_RepositoryFile.toString();
        CreateTextFile(destenationPath, contentFile);
    }

    public String GetBranchDetails(){
        return m_Magit.BranchesDetails();
    }

    public boolean IsExistBranch(String i_BranchName){
        return m_Magit.IsExistBranch(i_BranchName);
    }

    public void CreateNewBranch(String i_BranchName){
        m_Magit.CreateNewBranch(i_BranchName);
    }

    public String ShowCurrentCommit() {
        return m_Magit.ShowCurrrentCommit();
    }

    public String ShowCurrentCommitHistory() {
        return m_Magit.ShowCurrrentCommitHistory();
    }

    public boolean IsHeadBranch(String i_BranchName) {
        return m_Magit.IsHeadBranch(i_BranchName);
    }

    public void DeleteBranch(String i_BranchName) {
            m_Magit.DeleteBranch(i_BranchName);
    }

    public String GetBranchCommittsHistory() {
        return m_Magit.BranchCommitHistory();
    }

    public void SwitchHeadBranchAndDeployIt(String i_BranchName) {
        m_Magit.SwitchHeadBranch(i_BranchName);
        if (m_Magit.GetActiveCommit() != null) {
            cleanWC();
            m_Magit.DeployHeadBranch();
            m_WC.GetMainFolder().SetSha_1(m_Magit.GetActiveBranch().GetCommit().GetMainFolderSha_1());
        }
    }

    private void cleanWC() {
        m_WC.Clean();
    }

    public String ShowWorkingCopyStatus(UserName i_UserName) {
        m_WC.GenerateSystemFiles(i_UserName, m_Magit.GetObjects());
        m_Delta.Clean();
        List <RepositoryFile> repositoryFileList = DeployCommitToList();

        calculateDelta(repositoryFileList, m_WC.GetMainFolder());
        m_Delta.SyncLists();

        String workingCopyStatus = "Name of Repository: " + m_Name + System.lineSeparator();
        workingCopyStatus += "Path: " + m_Path.toString() + System.lineSeparator();
        workingCopyStatus += "Current UserName " + i_UserName.toString() + System.lineSeparator();
        workingCopyStatus += m_Delta.toString();
        return workingCopyStatus;
    }

    public List<RepositoryFile> DeployCommitToList() {
        String Sha_1CommitMainFolder = m_Magit.GetActiveCommit().GetMainFolderSha_1();
        List<RepositoryFile> repositoryFiles = new ArrayList<>();
        createRepositoryFileListOfCommitZipRec(Sha_1CommitMainFolder, m_Path, repositoryFiles);
        return repositoryFiles;
    }

    private RepositoryFile createRepositoryFileListOfCommitZipRec(String i_Sha_1Folder, Path i_PathToFile, List<RepositoryFile> i_RepositoryFiles) {
        RepositoryFile file = null;
        String pathToZip = m_Path.toString() + "/.magit/Objects/" + i_Sha_1Folder + ".zip";
        String zipContent = ReadFromZip(pathToZip);
        List<RepositoryFile> subFilesInFolder = new ArrayList<>();
        String[] linesOfZip = zipContent.split("\n");
        for(String str: linesOfZip){
            String[] output = str.split(",");
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy-HH:mm:ss:SSS");
            Date date = null;
            try {
                date = dateFormat.parse(output[4]);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Path pathToFile = Paths.get(i_PathToFile.toString() + "/" + output[0]);

            if(output[2].equals("Folder")) {
                RepositoryFile subFile = createRepositoryFileListOfCommitZipRec(output[1], pathToFile, i_RepositoryFiles);
                subFilesInFolder.add(subFile);
                file = new Folder(output[0], output[1], output[3], date, pathToFile, subFilesInFolder);
                i_RepositoryFiles.add(file);
            }
            else{
                String content = ReadFromZip(m_Path.toString() + "/.magit/Objects/" + output[1] + ".zip");
                file = new Blob(output[0], output[1], output[3], date, pathToFile, content);
                i_RepositoryFiles.add(file);
            }
        }
        return file;
    }

    private void calculateDelta(List<RepositoryFile> i_RepositoryFiles, RepositoryFile i_RepositroyFile) {
        for (RepositoryFile repositoryFile : i_RepositoryFiles){
            showWcStatusRec(repositoryFile, i_RepositroyFile);
        }
    }

    private void showWcStatusRec(RepositoryFile i_CommitRepositoryFile, RepositoryFile i_RepositoryFile) {
        boolean isSameContent =   i_RepositoryFile.GetSha_1().equals(i_CommitRepositoryFile.GetSha_1());
        boolean isSamePath = i_CommitRepositoryFile.GetPath().toString().equals((i_RepositoryFile.GetPath().toString()));
        boolean isSameName = (i_RepositoryFile.GetName().equals(i_CommitRepositoryFile.GetName()));
        boolean isSamePathExceptName =  DekelNoy3rd.Service.Methods.IsSamePathWithoutName(i_RepositoryFile.GetPath(),i_CommitRepositoryFile.GetPath());

        File mainFolderFile = new File (i_RepositoryFile.GetPath().toString());

        if(!mainFolderFile.exists()){
            return;
        }

        if(isSamePath && isSameContent){
            m_Delta.GetUnchanged().add(i_RepositoryFile.GetPath());
        }
        if (isSamePath && !isSameContent){
            m_Delta.GetContentChangedSameNameSameLocation().add(i_RepositoryFile.GetPath());
        }
        if(!isSamePath && isSamePathExceptName && isSameContent){
            m_Delta.GetContentUnchangedRenameSameLocation().add(new PairPath(i_CommitRepositoryFile.GetPath(), i_RepositoryFile.GetPath()));
        }
        if(!isSamePath && isSamePathExceptName && !isSameContent) {
            m_Delta.GetAdded().add(i_RepositoryFile.GetPath());
            m_Delta.GetDeleted().add(i_CommitRepositoryFile.GetPath());
        }
        if(!isSamePath && !isSamePathExceptName && isSameContent && !isSameName){
            m_Delta.GetContentUnchangedRenameReLocation().add(new PairPath(i_CommitRepositoryFile.GetPath(), i_RepositoryFile.GetPath()));
        }
        if(!isSamePath && !isSamePathExceptName && !isSameContent) {
            m_Delta.GetAdded().add(i_RepositoryFile.GetPath());
            m_Delta.GetDeleted().add(i_CommitRepositoryFile.GetPath());
        }
        if(!isSamePath && isSameName && isSameContent){
            m_Delta.GetContentUnchangedSameNameRelocation().add(new PairPath(i_CommitRepositoryFile.GetPath(), i_RepositoryFile.GetPath()));
        }
        if(!isSamePath && isSameName && !isSameContent){
            m_Delta.GetContentChangeSameNameRelocation().add(new PairPath(i_CommitRepositoryFile.GetPath(), i_RepositoryFile.GetPath()));
        }

        if(i_RepositoryFile instanceof Folder){
            Folder folder = ((Folder) i_RepositoryFile);
            int numOfFilesInFolder = folder.GetRepositoryFiles().size();
            List <RepositoryFile> innerRepositoryFiles = folder.GetRepositoryFiles();

            for (int i = 0; i < numOfFilesInFolder; i++){
                showWcStatusRec(i_CommitRepositoryFile, innerRepositoryFiles.get(i));
            }
        }
    }

    public Path GetPath() {
        return m_Path;
    }

    public boolean HasBranchesExceptHead() {
        return m_Magit.HasBranchesExceptHead();
    }

    public boolean HasFilesInWC() {
        return m_WC.HasFilesInWC();
    }
}