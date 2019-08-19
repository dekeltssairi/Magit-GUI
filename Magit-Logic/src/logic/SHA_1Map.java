package logic;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class SHA_1Map {
    private Map<String, ZipFile> m_StringZipFilesMap;
    private Path m_PathToMapFile;

    public SHA_1Map(Path i_PathToMapFile) {
        m_StringZipFilesMap = new HashMap<>();
        m_PathToMapFile = i_PathToMapFile;
        fillMap();
    }

    public Map<String, ZipFile> GetStringZipFilesMap() {
        return m_StringZipFilesMap;
    }

    public Path GetPathToMapFile() {
        return m_PathToMapFile;
    }

    private void fillMap() {

        if (new File(m_PathToMapFile.toString()).exists()) {
            File mapFile = new File(m_PathToMapFile.toString());
            Scanner scanner = null;
            try {
                scanner = new Scanner(mapFile);

                String line;
                while (scanner.hasNextLine()) {
                    line = scanner.nextLine();
                    String[] output = line.split(",");
                    String SHA_1 = output[0];
                    Path pathToZip = Paths.get(output[1]);
                    UserName zipper = new UserName(output[2]);
                    Path pathToRepositoryFile = Paths.get(output[3]);
                    m_StringZipFilesMap.put(SHA_1, new ZipFile(pathToZip, zipper, pathToRepositoryFile));

                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
