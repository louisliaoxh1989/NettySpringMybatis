package com.ycg.ycexpress.server.impl;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.ycg.ycexpress.dao.BaseDao;
import com.ycg.ycexpress.server.BaseService;


public class BaseServiceImpl<T extends Serializable, PK extends Serializable> implements BaseService<T, PK> {
	@Autowired
	private BaseDao<T, PK> baseDao;
	
	
	public void save(T t) {
		baseDao.save(t);
	}
	public void update(T t) {
		baseDao.update(t);
	}
	public void delete(PK id) {
		baseDao.delete(id);
	}
	public T findById(PK id) {
		return baseDao.findById(id);
	}
	public List<T> findAll() {
		return baseDao.findAll();
	}
}

