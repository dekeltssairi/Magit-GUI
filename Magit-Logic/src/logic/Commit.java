package logic;

import java.io.*;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import static DekelNoy3rd.Service.Methods.*;

public class Commit {
    private String m_MainFolderSha_1;
    private Commit m_ParentCommit;
    private String m_Message;
    private Date m_Date;
    private UserName m_Committer;
    private String m_Sha1;
    private Path m_PathToMainFolder;

    public Commit(String i_MainFolderSha_1, Commit i_ParentCommit, String i_Message, Date i_Date, UserName i_Committer, Path i_PathToMainFolder) {
        m_MainFolderSha_1 = i_MainFolderSha_1;
        m_ParentCommit = i_ParentCommit;
        m_Message = i_Message;
        m_Date = i_Date;
        m_Committer = i_Committer;
        m_Sha1 = org.apache.commons.codec.digest.DigestUtils.sha1Hex(toString());
        m_PathToMainFolder = i_PathToMainFolder;
    }

    public String GetSha1() {
        return m_Sha1;
    }

    public String GetMainFolderSha_1() {
        return m_MainFolderSha_1;
    }

    public Path GetPathToMainFolder() {
        return m_PathToMainFolder;
    }

    public Commit GetParentCommit() {
        return m_ParentCommit;
    }

    public String GetMessage() {
        return m_Message;
    }

    @Override
    public String toString() {
        String parentCommitSha1;
        if (m_ParentCommit == null) {
            parentCommitSha1 = "There is no earlier version";
        } else {
            parentCommitSha1 = m_ParentCommit.GetSha1();
        }
        String dateStr = new SimpleDateFormat("dd.MM.yyyy-HH:mm:ss:SSS").format(m_Date);
        String commitString = String.format("%s%s%s%s%s%s%s%s%s", m_MainFolderSha_1, System.lineSeparator(),
                parentCommitSha1, System.lineSeparator(),
                m_Message, System.lineSeparator(),
                dateStr, System.lineSeparator(),
                m_Committer.toString());
        return commitString;
    }

    public String ShowCommitDetails() {
        String dateStr = new SimpleDateFormat("dd.MM.yyyy-HH:mm:ss:SSS").format(m_Date);
        String detailsOfcommit = m_Sha1 + System.lineSeparator() +
                m_Message + System.lineSeparator() +
                dateStr + System.lineSeparator() +
                m_Committer.toString() + System.lineSeparator();

        detailsOfcommit += "================================================" + System.lineSeparator();
        return detailsOfcommit;
    }

    public void DeployRec(Path i_pathToFile, String i_Sha1) {
        String pathToObjects = m_PathToMainFolder.toString() + "/.magit/Objects";
        String pathToZipFile = pathToObjects + "/" + i_Sha1 + ".zip";
        String zipContent = ReadFromZip(pathToZipFile);
        zipContent = fixString(zipContent);
        String[] linesOfZip = zipContent.split(System.lineSeparator());

        for(String str: linesOfZip) {
            String[] output = str.split(",");
            if (output[2].equals("Blob")) {
                //String pathToCreateFile = i_pathToFile.toString() + "\\" + output[0] + ".txt";
                String pathToZipBlobFile = pathToObjects + "/" + output[1] + ".zip";
                Unzip(pathToZipBlobFile, i_pathToFile.toString());
            } else {
                Path pathToFile = Paths.get(i_pathToFile + "/" + output[0]);
                new File(pathToFile.toString()).mkdir();
                DeployRec(pathToFile, output[1]);
            }
        }
    }
}
