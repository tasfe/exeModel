package model;

import org.exemodel.annotation.CacheOrder;
import org.exemodel.annotation.Cacheable;
import org.exemodel.annotation.PartitionId;
import org.exemodel.orm.ExecutableModel;

import javax.persistence.Id;

/**
 * Created by zp on 16/9/12.
 */
@Cacheable(key = "trl")
public class Role extends ExecutableModel{
    private int id;
    private int userId;
    private String title;
    private String details;
    private String permissions;


    @Id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    @PartitionId
    @CacheOrder(value = 0)
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @CacheOrder(value = 1)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    @CacheOrder(value = 2)
    public String getPermissions() {
        return permissions;
    }

    public void setPermissions(String permissions) {
        this.permissions = permissions;
    }
}
