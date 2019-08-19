package logic;

import java.nio.file.Path;
import java.util.*;

public class Delta {
    private Path m_RepositoryPath;
    private Set<Path> m_Added;
    private Set<Path> m_Deleted;
    private Set<Path> m_Unchanged;
    private Set<Path> m_ContentChangedSameNameSameLocation;
    private Set<PairPath> m_ContentUnchangedRenameSameLocation;
    private Set<PairPath> m_ContentUnchangedRenameReLocation;
    private Set<PairPath> m_ContentChangeSameNameRelocation;
    private Set<PairPath> m_ContentUnchangedSameNameRelocation;

    public Delta(Path i_RepositoryPath) {
        m_RepositoryPath = i_RepositoryPath;
        initilizaSets();
    }

    private void initilizaSets() {
        m_Added = new HashSet<Path>() {
            @Override
            public boolean contains(Object o) {
                boolean isContatins = false;
                for (Path path : m_Added) {
                    if (path.toString().equals(o.toString()))
                        isContatins = true;
                }
                return isContatins;
            }
        };

        m_Deleted = new HashSet<Path>() {
            @Override
            public boolean contains(Object o) {
                boolean isContatins = false;
                for (Path path : m_Deleted) {
                    if (path.toString().equals(o.toString()))
                        isContatins = true;
                }
                return isContatins;
            }
        };

        m_Unchanged = new HashSet<Path>() {
            @Override
            public boolean contains(Object o) {
                boolean isContatins = false;
                for (Path path : m_Unchanged) {
                    if (path.toString().equals(o.toString()))
                        isContatins = true;
                }
                return isContatins;
            }
        };

        m_ContentChangedSameNameSameLocation = new HashSet<Path>() {
            @Override
            public boolean contains(Object o) {
                boolean isContatins = false;
                for (Path path : m_ContentChangedSameNameSameLocation) {
                    if (path.toString().equals(o.toString()))
                        isContatins = true;
                }
                return isContatins;
            }
        };

        m_ContentUnchangedRenameSameLocation = new HashSet<PairPath>() {
            @Override
            public boolean contains(Object o) {
                boolean isContatins = false;
                for (PairPath pairPath : m_ContentUnchangedRenameSameLocation) {
                    if (pairPath.GetSource().toString().equals(o.toString()) || pairPath.GetDestination().toString().equals(o.toString())) {
                        isContatins = true;
                    }
                }
                return isContatins;
            }
        };

        m_ContentUnchangedRenameReLocation = new HashSet<PairPath>() {
            @Override
            public boolean contains(Object o) {
                boolean isContatins = false;
                for (PairPath pairPath : m_ContentUnchangedRenameReLocation) {
                    if (pairPath.GetSource().toString().equals(o.toString()) || pairPath.GetDestination().toString().equals(o.toString())) {
                        isContatins = true;
                    }

                }
                return isContatins;
            }
        };

        m_ContentChangeSameNameRelocation = new HashSet<PairPath>() {
            @Override
            public boolean contains(Object o) {
                boolean isContatins = false;
                for (PairPath pairPath : m_ContentChangeSameNameRelocation) {
                    if (pairPath.GetSource().toString().equals(o.toString()) || pairPath.GetDestination().toString().equals(o.toString())) {
                        isContatins = true;
                    }

                }
                return isContatins;
            }
        };



        m_ContentUnchangedSameNameRelocation = new HashSet<PairPath>() {
            @Override
            public boolean contains(Object o) {
                boolean isContatins = false;
                for (PairPath pairPath : m_ContentUnchangedSameNameRelocation) {
                    if (pairPath.GetSource().toString().equals(o.toString()) || pairPath.GetDestination().toString().equals(o.toString())) {
                        isContatins = true;
                    }
                }
                return isContatins;
            }
        };
    }

    public Set<Path> GetAdded() {
        return m_Added;
    }

    public Set<Path> GetDeleted() {
        return m_Deleted;
    }

    public Set<Path> GetUnchanged() {
        return m_Unchanged;
    }

    public Set<Path> GetContentChangedSameNameSameLocation() {
        return m_ContentChangedSameNameSameLocation;
    }

    public Set<PairPath> GetContentUnchangedRenameSameLocation() {
        return m_ContentUnchangedRenameSameLocation;
    }

    public Set<PairPath> GetContentUnchangedRenameReLocation() {
        return m_ContentUnchangedRenameReLocation;
    }

    public Set<PairPath> GetContentChangeSameNameRelocation() {
        return m_ContentChangeSameNameRelocation;
    }

    public Set<PairPath> GetContentUnchangedSameNameRelocation() {
        return m_ContentUnchangedSameNameRelocation;
    }

    public synchronized void SyncLists() {
        filterList(m_Added);
        filterList(m_Deleted);
    }

    private void filterList(Set<Path> i_Set) {

        for (Iterator<Path> iterator = i_Set.iterator(); iterator.hasNext();) {
            Path element = iterator.next();
            if (m_ContentChangedSameNameSameLocation.contains(element)) {
                iterator.remove();
            }

            if (m_ContentUnchangedRenameSameLocation.contains(element)) {
                iterator.remove();
            }

            if (m_Unchanged.contains(element)) {
                iterator.remove();
            }

            if(m_ContentChangeSameNameRelocation.contains(element)){
                iterator.remove();
            }

            if(m_ContentUnchangedRenameReLocation.contains(element)){
                iterator.remove();
            }
            if(m_ContentUnchangedSameNameRelocation.contains(element)){
                iterator.remove();
            }
        }
        if (i_Set.contains(m_RepositoryPath)){
            i_Set.remove(m_RepositoryPath);
        }
    }

    @Override
    public String toString() {
        String delta = "";
        if (m_Added.size() > 0) {
            delta += ("Those Files are new:") + System.lineSeparator();
            delta += "==================================" + System.lineSeparator();
            for (Path path : m_Added) {
                delta += "-" + path.toString() + System.lineSeparator();
            }
        }
        if (m_Deleted.size() > 0) {
            delta += "Those Files deleted" + System.lineSeparator();
            delta += "==================================" + System.lineSeparator();
            for (Path path : m_Deleted) {
                delta += "-" + path.toString() + System.lineSeparator();
            }
        }


//        delta += "those files unchanged" + System.lineSeparator();
//        delta += "==================================" + System.lineSeparator();
//        for (Path path : m_Unchanged) {
//            delta += "-" + path.toString() + System.lineSeparator();
//        }
        if (m_ContentUnchangedSameNameRelocation.size() > 0) {
            delta += "those files replaced" + System.lineSeparator();
            delta += "==================================" + System.lineSeparator();
            for (PairPath path : m_ContentUnchangedSameNameRelocation) {
                delta += "-" + path.toString() + System.lineSeparator();
            }
        }

        if (m_ContentChangedSameNameSameLocation.size() > 0) {
            delta += "those files changed" + System.lineSeparator();
            delta += "==================================" + System.lineSeparator();
            for (Path path : m_ContentChangedSameNameSameLocation) {
                delta += "-" + path.toString() + System.lineSeparator();
            }
        }

        if (m_ContentUnchangedRenameSameLocation.size() > 0) {
            delta += "those files reNamed" + System.lineSeparator();
            delta += "==================================" + System.lineSeparator();
            for (PairPath pairPath : m_ContentUnchangedRenameSameLocation) {
                delta += "-" + pairPath.toString() + System.lineSeparator();
            }
        }

        if (m_ContentUnchangedRenameReLocation.size() > 0) {
            delta += "those files relocated and rename" + System.lineSeparator();
            delta += "==================================" + System.lineSeparator();
            for (PairPath pairPath : m_ContentUnchangedRenameReLocation) {
                delta += "-" + pairPath.toString() + System.lineSeparator();
            }
        }

        if (m_ContentChangeSameNameRelocation.size() > 0) {
            delta += "those files changed and replaced" + System.lineSeparator();
            delta += "==================================" + System.lineSeparator();
            for (PairPath pairPath : m_ContentChangeSameNameRelocation) {
                delta += "-" + pairPath.toString() + System.lineSeparator();
            }
        }
        if (delta.equals(""))
            delta = "No diffrance in WC And current commit";
        return delta;
    }

    public void Clean() {
        m_ContentChangedSameNameSameLocation.clear();
        m_ContentChangeSameNameRelocation.clear();
        m_ContentUnchangedRenameReLocation.clear();
        m_ContentUnchangedRenameSameLocation.clear();
        m_ContentUnchangedSameNameRelocation.clear();
        m_Unchanged.clear();
        m_Added.clear();
        m_Deleted.clear();
    }
}