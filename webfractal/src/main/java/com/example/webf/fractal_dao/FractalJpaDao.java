package com.example.webf.fractal_dao;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.example.webf.fractal.FractalEntity;

	/**
	 * @author And-4
	 * 
	 * Дополнительная реализация DAO для работы с фракталами 
	 */
public class FractalJpaDao extends GenericFractalJpaDao<FractalEntity,Long> implements FractalDao {

	public FractalJpaDao() {
		super(FractalEntity.class);
	}

	/*
	 * проверяет - доступен ли фрактал для создания
	 * возвращает true - доступен (еще нет в БД)
	 */
	public boolean checkAvailable(FractalEntity fractalEntity) {
		Query query = getEntityManager()
                .createQuery("select count(*) from " + getPersistentClass().getSimpleName()
                        		+ " u where u.c_x = :C_X AND u.c_y = :C_Y AND u.id = :ID").setParameter("C_X", fractalEntity.getC_x()).setParameter("C_Y", fractalEntity.getC_y()).setParameter("ID", fractalEntity.getId());

		Long count = (Long) query.getSingleResult();
        return count < 1;
	}
	
	/*
     * Возвращает фрактал по fid
     */
	public FractalEntity loadFractalByFid(long fid){
		FractalEntity fractal = null;
		Query query = getEntityManager().createQuery(
				"select u from " + getPersistentClass().getSimpleName()
						+ " u where u.fid = :fid").setParameter("fid", fid);
		try {
			fractal = (FractalEntity) query.getSingleResult();
		} catch (NoResultException e) {
			// do nothing
			}
		return fractal;
		}
	
	/*
	 *  Возвращвет список все фракталов пользователя по его id
	 */
	@SuppressWarnings("unchecked")
	public List<FractalEntity>  loadAllByUserID(long id) {
		List<FractalEntity> fractalList = null;
		try {
			fractalList = getEntityManager().createQuery(
					"select u from " + getPersistentClass().getSimpleName()
							+ " u where u.id = :id").setParameter("id", id).getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fractalList;
	}

	/*
	 * Возвращает фрактал по значениям x, y и id пользователя
	 */
	public FractalEntity loadFractalByXYUserID(String c_x, String c_y, long userID) {
		FractalEntity fractal = null;
		Query query = getEntityManager().createQuery(
				"select u from " + getPersistentClass().getSimpleName()
						+ " u where u.c_x = :C_X AND u.c_y = :C_Y AND u.id = :ID")
                        		.setParameter("C_X", c_x).setParameter("C_Y", c_y)
                        		.setParameter("ID", userID);
		try {
			fractal = (FractalEntity) query.getSingleResult();
		} catch (NoResultException e) {
			// do nothing
			}
		return fractal;
		}
}