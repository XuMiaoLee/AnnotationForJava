package table;

import annotation.DBTable;
import annotation.PrimaryKey;

@DBTable(tableName = "tb_user")
public class UserTable
{
    @PrimaryKey
    private int id;
}
