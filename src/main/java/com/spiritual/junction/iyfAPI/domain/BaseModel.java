package com.spiritual.junction.iyfAPI.domain;


import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import java.util.Date;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import com.fasterxml.jackson.annotation.JsonIgnore;

@MappedSuperclass
public class BaseModel {


    @Column(name = "CREATED_BY")
    @JsonIgnore
    private String createdBy;

    @Column(name = "MODIFIED_BY")
    @JsonIgnore
    private String modifiedBy;

    @Column(name = "DATE_CREATED")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonIgnore
    private Date createdDate;

    @Column(name = "DATE_MODIFIED")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonIgnore
    private Date modifiedDate;

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    @PrePersist
    void onCreate() {
        String currentUser = getCurrentUser();
        Date date = new Date();
        this.setCreatedBy(currentUser);
        this.setCreatedDate(date);
        this.setModifiedBy(currentUser);
        this.setModifiedDate(date);
    }

    @PreUpdate
    void onPersist() {
        this.setModifiedBy(getCurrentUser());
        this.setModifiedDate(new Date());
    }

    private String getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            try {
                if(authentication.getPrincipal() instanceof org.springframework.security.core.userdetails.User){
                    org.springframework.security.core.userdetails.User user = (User) authentication.getPrincipal();
                    return user.getUsername();
                }else{
                    return (String) authentication.getPrincipal();
                }
            } catch (NullPointerException npe) {
                return null;
            }
        }
        return null;
    }
}
