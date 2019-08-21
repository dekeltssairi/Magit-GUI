package logic;

import java.nio.file.Path;

public class ZipFile {
    private Path m_Path;
    private UserName m_Zipper;
    private Path m_RepositoryFilePath;

    public ZipFile(Path i_ZipPath, UserName i_Zipper, Path i_RepositoryFilePath) {
        m_Path = i_ZipPath;
        m_Zipper = i_Zipper;
        m_RepositoryFilePath = i_RepositoryFilePath;
    }

    public void SetRepositoryFilePath(Path i_RepositoryFilePath) {
        this.m_RepositoryFilePath = i_RepositoryFilePath;
    }

    public void SetZipper(UserName i_Zipper) {
        this.m_Zipper = i_Zipper;
    }
    public Path GetPath() {
        return m_Path;
    }

    public UserName GetZipper() {
        return m_Zipper;
    }

    public Path GetRepositoryFilePath() {
        return m_RepositoryFilePath;
    }

    @Override
    public String toString() {
        return String.format("%s,%s,%s", m_Path, m_Zipper,m_RepositoryFilePath);
    }
}
