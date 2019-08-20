package logic;

import java.nio.file.Path;
import java.util.Date;

public class RepositoryFile {
    private String m_Name;
    private String m_Sha_1;
    private String m_Committer;
    private Date m_Date;
    private Path m_Path;

    public RepositoryFile(String i_Name, String i_Sha_1, String i_Committer, Date i_Date, Path i_Path) {
        m_Name = i_Name;
        m_Sha_1 = i_Sha_1;
        m_Committer = i_Committer;
        m_Date = i_Date;
        m_Path = i_Path;
    }

    public String GetSha_1() {
        return m_Sha_1;
    }

    public String GetName() {
        return m_Name;
    }

    public Path GetPath() {
        return m_Path;
    }

    public String GetCommitter() {
        return m_Committer;
    }

    public Date GetDate() {
        return m_Date;
    }

    public void SetSha_1(String i_Sha1) {
        m_Sha_1 = i_Sha1;
    }
}
