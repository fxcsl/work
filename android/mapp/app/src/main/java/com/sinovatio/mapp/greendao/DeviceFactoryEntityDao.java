package com.sinovatio.mapp.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.sinovatio.mapp.entity.DeviceFactoryEntity;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "DEVICE_FACTORY_ENTITY".
*/
public class DeviceFactoryEntityDao extends AbstractDao<DeviceFactoryEntity, Long> {

    public static final String TABLENAME = "DEVICE_FACTORY_ENTITY";

    /**
     * Properties of entity DeviceFactoryEntity.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property ThreeByteMac = new Property(1, String.class, "threeByteMac", false, "THREE_BYTE_MAC");
        public final static Property Factory = new Property(2, String.class, "factory", false, "FACTORY");
    }


    public DeviceFactoryEntityDao(DaoConfig config) {
        super(config);
    }
    
    public DeviceFactoryEntityDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, DeviceFactoryEntity entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String threeByteMac = entity.getThreeByteMac();
        if (threeByteMac != null) {
            stmt.bindString(2, threeByteMac);
        }
 
        String factory = entity.getFactory();
        if (factory != null) {
            stmt.bindString(3, factory);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, DeviceFactoryEntity entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String threeByteMac = entity.getThreeByteMac();
        if (threeByteMac != null) {
            stmt.bindString(2, threeByteMac);
        }
 
        String factory = entity.getFactory();
        if (factory != null) {
            stmt.bindString(3, factory);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public DeviceFactoryEntity readEntity(Cursor cursor, int offset) {
        DeviceFactoryEntity entity = new DeviceFactoryEntity( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // threeByteMac
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2) // factory
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, DeviceFactoryEntity entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setThreeByteMac(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setFactory(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(DeviceFactoryEntity entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(DeviceFactoryEntity entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(DeviceFactoryEntity entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
