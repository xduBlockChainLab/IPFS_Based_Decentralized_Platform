package com.example.paas_decentralized_webplatform.controller;


import com.example.paas_decentralized_webplatform.pojo.*;
import com.example.paas_decentralized_webplatform.service.App;
import com.example.paas_decentralized_webplatform.service.InfoService;
import com.example.paas_decentralized_webplatform.util.TempName;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
public class ContractController {

    @Autowired
    private App app_service;

    @Autowired
    private InfoService infoService;

    @RequestMapping("/queryValue")
    public ReturnInfo queryData(@RequestParam("key") String key){
        ReturnInfo returnInfo = new ReturnInfo();
        String[] keys = new String[1];
        keys[0]= key;
        String value = app_service.queryValue(keys);
        returnInfo.setData(value);
        return returnInfo;
    }

    @RequestMapping("/upload")
    public String uploadData(@RequestParam("name")String name ,@RequestParam("cid")String cid,@RequestParam("flag")String flag,@RequestParam("sort")String sort){

        System.out.println(name +" "+ cid);
        String[] key_and_value = new String[]{name,cid};
        return app_service.uploadValue(key_and_value,name,cid,flag,sort);

    }

    @RequestMapping("/querySort")
    public ReturnInfo querySortNum(){
        ReturnInfo returnInfo = new ReturnInfo();
       HashMap<String, Long> sortCountByGroup = infoService.getSortCountByGroup();
        returnInfo.setData(sortCountByGroup);
        return returnInfo;
    }


    @RequestMapping("/queryFlag")
    public ReturnInfo queryFlagNum(){

        System.out.println(123123);
        ReturnInfo returnInfo = new ReturnInfo();
        HashMap<String,Long> sortFlagByGroup = infoService.getFlagCountByGroup();
        returnInfo.setData(sortFlagByGroup);
        return returnInfo;
    }

    @RequestMapping("/queryBlockInfo")
    public ReturnInfo queryBlockInfo(){
        ReturnInfo returnInfo = new ReturnInfo();
        List<ResourceInfo> list= infoService.queryAllBookInfo();
        BlockInfo blockInfo = app_service.queryBlockInfo(list);
        returnInfo.setData(blockInfo);
        return returnInfo;
    }

    @RequestMapping("/queryByName")
    public ReturnInfo queryBookInfoByName(@RequestParam("name")String name){
        ReturnInfo returnInfo  = new ReturnInfo();
        List<ResourceInfo> resourceInfo = infoService.queryBookInfoByName(name);
        if(resourceInfo!=null){
            returnInfo.setData(resourceInfo.get(0));
            return returnInfo;
        }
        returnInfo.setCode(400);
        returnInfo.setMessage("book not found");
        return returnInfo;

    }

    @RequestMapping("/queryAll")
    public ReturnInfo queryAllBookInfo(){
        ReturnInfo returnInfo = new ReturnInfo();
        List<ResourceInfo> list= infoService.queryAllBookInfo();
        returnInfo.setData(list);
        return returnInfo;
    }

    @RequestMapping("/queryAllLink")
    public ReturnInfo queryAllLinkInfo(){
        ReturnInfo returnInfo = new ReturnInfo();
        List<ResourceInfo> list= infoService.queryAllBookInfo();
        List<LinkInfo> list1 = new ArrayList<>();
        for(ResourceInfo resourceInfo:list){
            int f = 1;
            for(LinkInfo linkInfo1:list1){
                if(linkInfo1.getSource().equals(resourceInfo.getName())&&linkInfo1.getTarget().equals(resourceInfo.getFlag())){
                    f = 0;
                    break;
                }
            }
            if(f==0)break;
            LinkInfo linkInfo = new LinkInfo();
            linkInfo.setSource(resourceInfo.getName());
            linkInfo.setTarget(resourceInfo.getFlag());
            System.out.println(linkInfo);
            list1.add(linkInfo);
        }

        returnInfo.setData(list1);
        return returnInfo;
    }

    @RequestMapping("/queryAllNode")
    public ReturnInfo queryAllNodeInfo(){
        ReturnInfo returnInfo = new ReturnInfo();
        List<ResourceInfo> list= infoService.queryAllBookInfo();
        List<NodeInfo> list1 = new ArrayList<>();
        for(ResourceInfo no:list){
            int f= 1;
            for(NodeInfo nodeInfo1:list1){
                if(nodeInfo1.getId().equals(no.getName())){
                    f= 0;
                    break;
                }
            }
            if (f == 0) continue;
            NodeInfo nodeInfo = new NodeInfo();
            nodeInfo.setId(no.getName());
            System.out.println(nodeInfo);
            list1.add(nodeInfo);

        }

        for(ResourceInfo no:list){

            int f= 1;
            for(NodeInfo nodeInfo1:list1){
                if(nodeInfo1.getId().equals(no.getFlag())){
                    System.out.println(no.getFlag());
                    f= 0;
                    break;
                }
            }
            if (f == 0) continue;
            NodeInfo nodeInfo = new NodeInfo();
            nodeInfo.setId(no.getFlag());
            System.out.println(no.getFlag());
            list1.add(nodeInfo);

        }
        returnInfo.setData(list1);
        return returnInfo;
    }

    @RequestMapping("/searchData")
    public String uploadTempData(@Param("name") String name){
        System.out.println(name);
        infoService.updateTempData(name);
        TempName.name = name;
        return "success";
    }

    @RequestMapping("/tempname")
    public ReturnInfo tempName(){
        ReturnInfo returnInfo = new ReturnInfo();
        returnInfo.setData(TempName.name);
        System.out.println(TempName.name);
        return returnInfo;
    }
}
