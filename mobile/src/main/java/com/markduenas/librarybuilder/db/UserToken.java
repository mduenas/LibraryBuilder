package com.markduenas.librarybuilder.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "usertoken")
public class UserToken {

    @DatabaseField(id = true)
    public String userid;
    @DatabaseField(canBeNull = true)
    public String cloudtoken;
    @DatabaseField(canBeNull = false)
    public long lastlogin;
}
