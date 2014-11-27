package ru.fizteh.fivt.students.AlexeyZhuravlev.MultiFileHashMap;

import ru.fizteh.fivt.students.AlexeyZhuravlev.filemap.DataBase;
import ru.fizteh.fivt.students.AlexeyZhuravlev.filemap.RemoveCommand;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;

/**
 * @author AlexeyZhuravlev
 */
public class MultiRemoveCommand extends Command {
    private String key;

    protected void putArguments(String[] args) {
        key = args[1];
    }

    protected int numberOfArguments() {
        return 1;
    }

    @Override
    public void execute(DataBaseDir base) throws Exception {
        if (base.getUsing() == null) {
            System.out.println("no table");
        } else {
            int hashCode = Math.abs(key.hashCode());
            int dir = hashCode % 16;
            int file = hashCode / 16 % 16;
            RemoveCommand remove = new RemoveCommand(key);
            if (base.getUsing().databases[dir][file] == null) {
                System.out.println("not found");
            } else {
                DataBase db = base.getUsing().databases[dir][file];
                remove.execute(db);
                if (db.recordsNumber() == 0) {
                    File dbFile = new File(db.dbFileName);
                    try {
                        Files.delete(dbFile.toPath());
                    } catch (SecurityException | IOException e) {
                        throw new Exception("Access violation: cannon delete database file");
                    }
                    base.getUsing().databases[dir][file] = null;
                    int k = 0;
                    for (int j = 0; j < 16; j++) {
                        if (base.getUsing().databases[dir][j] == null) {
                            k++;
                        }
                    }
                    if (k == 16) {
                        try {
                            Files.delete(dbFile.getParentFile().toPath());
                        } catch (DirectoryNotEmptyException e) {
                            throw new Exception("Cannot remove table subdirectory. Redundant files");
                        } catch (SecurityException | IOException e) {
                            throw new Exception("Access violation: cannot delete database subdirectory");
                        }
                    }
                }
            }
        }
    }
}
