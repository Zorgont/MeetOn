package com.example.meetontest.api.security.services;



public interface Converter<T,V> {
    T convert(V entity);
    V convertBack(T entity);
}
