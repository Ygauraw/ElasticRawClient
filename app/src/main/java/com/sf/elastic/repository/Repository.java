package com.sf.elastic.repository;

import rx.Observable;

public interface Repository<T> {
	Observable<T> getNextCity(String text);
}
