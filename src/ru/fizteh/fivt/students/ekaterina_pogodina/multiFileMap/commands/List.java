package ru.fizteh.fivt.students.ekaterina_pogodina.multiFileMap.commands;

import ru.fizteh.fivt.students.ekaterina_pogodina.basicclasses.MyException;
import ru.fizteh.fivt.students.ekaterina_pogodina.multiFileMap.TableManager;

public class List extends Command {
    @Override
    public void execute(String[] args, TableManager table) throws Exception {
        if (table.currentTable == null) {
            MyException.exception("no table");
        }
        System.out.println(String.join("; ", table.usingTable.keys.keySet()));
    }

    @Override
    public void checkArgs(String[] args, TableManager table) throws Exception {
        if (args.length > 1) {
            table.manyArgs(args[1]);
        }
        execute(args, table);
    }
}
