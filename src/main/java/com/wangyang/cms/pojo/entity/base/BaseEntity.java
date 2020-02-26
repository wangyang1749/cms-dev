package com.wangyang.cms.pojo.entity.base;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;
@MappedSuperclass
public class BaseEntity {
    @Column(name = "create_time", columnDefinition = "timestamp default CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @Column(name = "update_time", columnDefinition = "timestamp default CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
