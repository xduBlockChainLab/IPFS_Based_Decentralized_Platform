package com.example.paas_decentralized_webplatform.dao;

import com.example.paas_decentralized_webplatform.pojo.ResourceInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

@Mapper
public interface ContractMapper {

    void insertData(@Param("name") String name,@Param("cid") String cid , @Param("flag") String flag,@Param("sort")String sort,@Param("Txid")String id);
     List<HashMap<Object,Object>> countSort();
     List<HashMap<Object,Object>> countFlag();
     List<ResourceInfo> selectByName(@Param("name")String name);
     List<ResourceInfo> selectAll();
     void updateData(@Param("name") String name);
}
