package com.sxjkwm.pm.business.file.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "pm_pattern_file")
public class PatternFile extends BaseFile {

    @Column(name = "file_path")
    private String path;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
