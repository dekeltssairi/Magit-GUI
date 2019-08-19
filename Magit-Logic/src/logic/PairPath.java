package logic;

import java.nio.file.Path;

public class PairPath {
    private Path m_Source;
    private Path m_Destination;

    public Path GetSource() {
        return m_Source;
    }

    public PairPath(Path i_Source, Path i_Destination) {
        m_Source = i_Source;
        m_Destination = i_Destination;
    }

    @Override
    public String toString() {
        String relocation = "relocated from: " + m_Source + " to " + m_Destination;
        return relocation;
    }

    public Path GetDestination() {
        return m_Destination;
    }
}