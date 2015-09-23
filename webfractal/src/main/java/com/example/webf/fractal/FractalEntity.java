package com.example.webf.fractal;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author And-4
 * 
 *  Определяет понятие фрактала, id - ссылается к user.id 
 *  У каждого фрактала только один пользователь
 *  У одного пользователя может быть много фракталов
 *  У одного пользователя фракталы не повторяются
 */

@Entity
@Table(name="appfractal")
public class FractalEntity extends FractalBaseEntity{
	
	private static final long serialVersionUID = -4114317757267180076L;
	private String c_x; 
	private String c_y;
	private Long id;  // ссылается к user.id 
	
	
    public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getC_x() {
		return c_x;
	}
	public void setC_x(String c_x) {
		this.c_x = c_x;
	}
	public String getC_y() {
		return c_y;
	}
	public void setC_y(String c_y) {
		this.c_y = c_y;
	}
}
