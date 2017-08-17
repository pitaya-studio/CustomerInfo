package com.trekiz.admin.modules.sys.entity;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.DataEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Created by zzk on 2016/10/13.
 * T1区域表
 */
@Entity
@Table(name = "sys_district")
public class District extends DataEntity {
    private Long id;
    private String name;
    private Integer tourInOut;
    private String tourInOutName;
    private String destinationIds;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name="name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name="tourInOut")
    public Integer getTourInOut() {
        return tourInOut;
    }

    public void setTourInOut(Integer tourInOut) {
        this.tourInOut = tourInOut;
    }

    @Transient
    public String getTourInOutName() {
        if (Context.FREE_TRAVEL_FOREIGN.equals(tourInOut.toString())) {
            tourInOutName = Context.FREE_TRAVEL_FOREIGN_CHINA;
        } else {
            tourInOutName = Context.FREE_TRAVEL_INLAND_CHINA;
        }
        return tourInOutName;
    }

    public void setTourInOutName(String tourInOutName) {
        this.tourInOutName = tourInOutName;
    }

    @Transient
    public String getDestinationIds() {
        return destinationIds;
    }

    public void setDestinationIds(String destinationIds) {
        this.destinationIds = destinationIds;
    }
}
