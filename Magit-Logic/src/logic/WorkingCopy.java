package logic;

import java.io.*;
import java.nio.file.*;
import  java.util.*;
import static org.apache.commons.codec.digest.DigestUtils.*;
import static DekelNoy3rd.Service.Methods.*;

public class WorkingCopy {

    private Path m_Path;
    private RepositoryFile m_MainFolder;

    public WorkingCopy(Path i_Path) {
       m_Path = i_Path;
    }

    public Path GetPath() {
        return m_Path;
    }

    public void GenerateSystemFiles(UserName i_UserName, SHA_1Map i_StringZipFilesMap) {
        File mainFolder = new File(m_Path.toString());
        DeleteEmptyDirectoriesWithoutSpecificFolder(m_Path, ".magit");
        if (mainFolder.listFiles().length > 1) {
            m_MainFolder = createSystemFilesRec(mainFolder, m_Path, i_UserName, i_StringZipFilesMap.GetStringZipFilesMap());
        }
    }

    public RepositoryFile GetMainFolder() {return m_MainFolder;}

    private RepositoryFile createSystemFilesRec(File myFile, Path i_Path, UserName i_UserName, Map<String, ZipFile> i_StringZipFilesMap) {
        RepositoryFile someFile = null;

        if (myFile.exists() == false) {
            someFile = null;
        }
        String fileName = myFile.getName();
        String sha_1;
        String committer = i_UserName.GetName();
        Date lastModifyDate;

        if (myFile.isFile()) {
            fileName = myFile.getName();
            String content = ReadContentOfTextFile(myFile.getPath());
            content = fixString(content);
            sha_1 = sha1Hex(content);
            if(i_StringZipFilesMap.containsKey(sha_1)){
                ZipFile zipFile = i_StringZipFilesMap.get(sha_1);
                if (zipFile.GetRepositoryFilePath().toString().equals(i_Path.toString())){
                    committer = zipFile.GetZipper().GetName();
                }
                else{
                    zipFile.SetZipper(i_UserName);
                    zipFile.SetRepositoryFilePath(i_Path);
                }
            }
            someFile = new Blob(fileName, sha_1, committer, new Date(myFile.lastModified()),i_Path, content);
        }
        else {
            File[] subFilesArray = myFile.listFiles();
            Arrays.sort(subFilesArray);
            List<File> subFilesList = new ArrayList<File>(Arrays.asList(subFilesArray));
            removeMagitFolderFromList(subFilesList);
            List<RepositoryFile> repositoryFilesInDirectory = new ArrayList<>(subFilesList.size());
            String chainOfAllInnerSha_1 = "";
            for (int i = 0; i < subFilesList.size(); i++) {
                Path pathToSubFile = subFilesList.get(i).toPath();
                RepositoryFile subFile = createSystemFilesRec(subFilesList.get(i),pathToSubFile, i_UserName, i_StringZipFilesMap);
                repositoryFilesInDirectory.add(subFile);
                chainOfAllInnerSha_1 += subFile.GetSha_1();
            }
            if (repositoryFilesInDirectory.size() > 0) {
                lastModifyDate = getLastModifyDateOfFolder(repositoryFilesInDirectory);
            }
            else {
                lastModifyDate = new Date(myFile.lastModified());
            }
            sha_1 = sha1Hex(chainOfAllInnerSha_1);
            if(i_StringZipFilesMap.containsKey(sha_1)){
                ZipFile zipFile = i_StringZipFilesMap.get(sha_1);
                if (zipFile.GetRepositoryFilePath().toString().equals(i_Path.toString())){
                    committer = zipFile.GetZipper().GetName();
                }
                else{
                    zipFile.SetZipper(i_UserName);
                    zipFile.SetRepositoryFilePath(i_Path);
                }
            }
            someFile = new Folder(fileName, sha_1, committer, lastModifyDate,i_Path, repositoryFilesInDirectory);
        }
        return someFile;
    }

    private Date getLastModifyDateOfFolder(List<RepositoryFile> repositoryFilesInDirectory) {
        Date lastDate = repositoryFilesInDirectory.get(0).GetDate();
        for (int i = 1; i < repositoryFilesInDirectory.size(); i++){
            Date date = repositoryFilesInDirectory.get(i).GetDate();
            if (lastDate.compareTo(date) < 0){
                lastDate = date;
            }
        }
        return lastDate;
    }

    private void removeMagitFolderFromList(List<File> i_ListOfFiles){
        for (int i = 0; i < i_ListOfFiles.size(); i++){
            if(i_ListOfFiles.get(i).getName().equals(".magit")){
                i_ListOfFiles.remove(i);
            }
        }
    }

    public void Clean() {
        File[] mainFolderSubFiles = new File (m_Path.toString()).listFiles();
        List<File> subFilesList = new ArrayList<File>(Arrays.asList(mainFolderSubFiles));
        removeMagitFolderFromList(subFilesList);
        for(File file : subFilesList){
            DeleteDirectory(file);
        }
    }

    public void GenerateSystemFiles(List<RepositoryFile> i_RepositoryFilesList, Path i_PathToFolder) {
        File mainFolder = new File(i_PathToFolder.toString());
        generateSystemFilesRec(mainFolder, i_RepositoryFilesList);
    }

    private void generateSystemFilesRec(File i_Folder, List<RepositoryFile> i_RepositoryFilesList) {
        for(RepositoryFile repositoryFile : i_RepositoryFilesList){
            Path pathOfParentFolder = repositoryFile.GetPath().getParent();
            if(pathOfParentFolder.toString().equals(i_Folder.getAbsolutePath())){

                if(repositoryFile instanceof Blob){
                    Path pathToObjects = Paths.get(m_Path.toString() + "/.magit/Objects");
                    String pathToZip = pathToObjects.toString() + "/" + repositoryFile.GetSha_1() + ".zip";
                    String blobContent = ReadFromZip(pathToZip);
                    CreateTextFile(repositoryFile.GetPath().toString(), blobContent);
                }

                else{
                    File folder = new File(repositoryFile.GetPath().toString());
                    folder.mkdir();
                    generateSystemFilesRec(folder, i_RepositoryFilesList);
                }
            }
        }
    }

    public boolean HasFilesInWC() {
        return new File(m_Path.toString()).listFiles().length > 1;
    }
}