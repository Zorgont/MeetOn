package com.example.meetontest.converters;

public interface Converter<T,V> {
    T convert(V entity);
    V convertBack(T entity);
}
