package com.ycg.ycexpress.server;

import java.io.Serializable;
import java.util.List;

public interface BaseService<T extends Serializable, PK extends Serializable> {
	void save(T t);
	void update(T t);
	void delete(PK id);
	T findById(PK id);
	List<T> findAll();
}