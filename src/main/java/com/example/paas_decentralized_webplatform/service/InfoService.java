package com.example.paas_decentralized_webplatform.service;

import com.example.paas_decentralized_webplatform.dao.ContractMapper;
import com.example.paas_decentralized_webplatform.pojo.ResourceInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class InfoService {
    @Autowired
    ContractMapper contractMapper;

    public HashMap<String,Long> getSortCountByGroup(){

        HashMap<String,Long> ret = new HashMap<>();
        List<HashMap<Object, Object>> hashMaps = contractMapper.countSort();
        for(HashMap<Object,Object> hashMap:hashMaps){

            String  key = (String) hashMap.get("sort");
            long value =(Long) hashMap.get("count(sort)");
            ret.put(key,value);
        }
        return ret;
    }

    public HashMap<String ,Long> getFlagCountByGroup(){
        HashMap<String,Long> ret = new HashMap<>();
        List<HashMap<Object, Object>> hashMaps = contractMapper.countFlag();
        for(HashMap<Object,Object> hashMap:hashMaps){

            String  key = (String) hashMap.get("flag");
            long value =(Long) hashMap.get("count(flag)");
            ret.put(key,value);
        }
        return ret;
    }



    public List<ResourceInfo> queryBookInfoByName(String name){

        List<ResourceInfo> resourceInfo = contractMapper.selectByName(name);
        if(resourceInfo!=null) {
            return resourceInfo;
        }
        return null;
    }

    public List<ResourceInfo> queryAllBookInfo(){
        List<ResourceInfo> list = contractMapper.selectAll();
        return list;

    }


    public void updateTempData(String name){
        contractMapper.updateData(name);
    }




}
