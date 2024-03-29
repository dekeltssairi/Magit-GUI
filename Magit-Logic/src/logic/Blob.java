package logic;

import java.nio.file.Path;
import java.util.Date;

public class Blob extends RepositoryFile {
    private String m_Content;

    public Blob(String i_name, String i_Sha_1, String i_Committer, Date i_Date, Path i_Path, String i_Content) {
               super(i_name, i_Sha_1, i_Committer, i_Date, i_Path);
        this.m_Content = i_Content;
    }

    @Override
    public String toString() {
        return m_Content;
    }
}
