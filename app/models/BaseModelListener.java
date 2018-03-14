package models;

import javax.persistence.PreUpdate;

public class BaseModelListener {
    @PreUpdate
    public static void preUpdate(BaseModel baseModel) {
        baseModel.lastModifyTime = System.currentTimeMillis();
    }
}
