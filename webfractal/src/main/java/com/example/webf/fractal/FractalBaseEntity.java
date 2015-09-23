package com.example.webf.fractal;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * @author And-4
 * 
 * Определяет уникальный id фрактала - fid
 */

@MappedSuperclass
public class FractalBaseEntity implements Serializable {
	private static final long serialVersionUID = 111379222048217476L;

    @Id
    @GeneratedValue
    private Long fid;

    public Long getFid() {
            return fid;
    }

    public void setFid(Long fid) {
            this.fid = fid;
    }
    
}