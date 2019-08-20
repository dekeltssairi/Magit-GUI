package logic;

import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.*;

public class Folder extends RepositoryFile {

    private List <RepositoryFile> m_RepositoryFiles;

    public Folder(String i_name, String i_Sha_1, String i_Committer, Date i_Date, Path i_Path, List<RepositoryFile> i_RepositoryFiles) {
        super(i_name, i_Sha_1, i_Committer, i_Date, i_Path);
        this.m_RepositoryFiles = i_RepositoryFiles;
    }

    @Override
    public String toString() {
        String describtionOfSubFiles = "";
        for(int i = 0; i < m_RepositoryFiles.size(); i++) {
            String subFileName;
            subFileName = m_RepositoryFiles.get(i).GetName();
            String subFileSha_1 = m_RepositoryFiles.get(i).GetSha_1();
            String subFileType = m_RepositoryFiles.get(i).getClass().getSimpleName();
            String subFileCommitter = m_RepositoryFiles.get(i).GetCommitter();
            String subFileDate = new SimpleDateFormat("dd.MM.yyyy-HH:mm:ss:SSS").format(m_RepositoryFiles.get(i).GetDate());
            describtionOfSubFiles += String.format("%s,%s,%s,%s, %s",subFileName,subFileSha_1,subFileType,subFileCommitter,subFileDate);
            if (i < m_RepositoryFiles.size() - 1){
                describtionOfSubFiles += System.lineSeparator();
            }
        }
        return describtionOfSubFiles;
    }

    public List<RepositoryFile> GetRepositoryFiles() {
        return m_RepositoryFiles;
    }
}