package com.markduenas.librarybuilder.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "usersession")
public class UserSession {

    @DatabaseField(id = true)
    public String userid;
    @DatabaseField(canBeNull = false)
    public String jsessionid;
    @DatabaseField(canBeNull = false)
    public long lastlogin;
}
