package com.sf.elastic.repositories;

import rx.Observable;

public interface Repository<T> {
	Observable<T> getNextCity(String text);
}
