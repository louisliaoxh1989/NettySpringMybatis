package com.ycg.ycexpress.dao;

import java.io.Serializable;
import java.util.List;

public interface BaseDao <T extends Serializable, PK extends Serializable>{

	void save(T t);
	void update(T t);
	void delete(PK id);
	T findById(PK id);
	List<T> findAll();
}
