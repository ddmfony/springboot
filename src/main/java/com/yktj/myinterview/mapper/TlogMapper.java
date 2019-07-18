package com.yktj.myinterview.mapper;

import com.yktj.myinterview.entities.Tlog;

import java.util.List;

//@Mapper
public interface TlogMapper {

    public Tlog getTlogById(Integer id);

    public List<Tlog> getTlogByKey(String key);

}
