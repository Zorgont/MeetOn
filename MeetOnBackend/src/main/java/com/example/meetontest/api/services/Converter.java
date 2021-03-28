package com.example.meetontest.api.services;



public interface Converter<T,V> {
    T convert(V entity);
    V convertBack(T entity);
}
