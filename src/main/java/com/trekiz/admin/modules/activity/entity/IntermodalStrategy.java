package com.trekiz.admin.modules.activity.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.trekiz.admin.modules.airticket.entity.ActivityAirTicket;
import com.trekiz.admin.modules.sys.entity.Currency;

/**
 * Created by ZhengZiyu on 2014/9/2.
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "intermodal_strategy")
@DynamicInsert
public class IntermodalStrategy implements Serializable{


    private Long id;

    private String groupPart;

    private BigDecimal price;
    
    private Currency priceCurrency;
    
	private Integer type;

    private ActivityAirTicket activityAirTicket;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "group_part", unique = false, nullable = true)
    public String getGroupPart() {
        return groupPart;
    }

    public void setGroupPart(String groupPart) {
        this.groupPart = groupPart;
    }

    @Column(name = "price", unique = false, nullable = true)
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "price_currency", referencedColumnName = "currency_id")
    @NotFound(action = NotFoundAction.IGNORE)
    public Currency getPriceCurrency() {
		return priceCurrency;
	}

	public void setPriceCurrency(Currency priceCurrency) {
		this.priceCurrency = priceCurrency;
	}
    
    @Column(name = "type", unique = false, nullable = true)
    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activity_id", referencedColumnName = "id")
    @NotFound(action = NotFoundAction.IGNORE)
    public ActivityAirTicket getActivityAirTicket() {
        return activityAirTicket;
    }

    public void setActivityAirTicket(ActivityAirTicket activityAirTicket) {
        this.activityAirTicket = activityAirTicket;
    }
}
