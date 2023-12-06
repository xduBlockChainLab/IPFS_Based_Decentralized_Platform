package com.example.paas_decentralized_webplatform.service;

import com.example.paas_decentralized_webplatform.dao.ContractMapper;
import com.example.paas_decentralized_webplatform.pojo.BlockInfo;
import com.example.paas_decentralized_webplatform.pojo.ResourceInfo;
import com.example.paas_decentralized_webplatform.util.BlockUtil;
import com.example.paas_decentralized_webplatform.util.TxUtil;
import com.huawei.wienerchain.SdkClient;
import com.huawei.wienerchain.exception.ConfigException;
import com.huawei.wienerchain.exception.CryptoException;
import com.huawei.wienerchain.proto.common.BlockOuterClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 功能描述: 业务端发起请求
 *
 * @author zyp
 * @since 2022-8-25
 */

@Service
public class App {

    // yaml格式配置文件的绝对路径，需要您手动输入
    public static final String configFilePath = "D:/cert/bcs-sevuqo-5b90b1e82-organization-zz7npjt8d-sdk.yaml";

    // 合约名称，需要您手动输入
    public static final String contractName = "chaincode";

    // 背书节点，需要您手动输入。如果是多个节点，请用“,”隔开
    public static final String endorserNodes = "node-0.organization-zz7npjt8d";

    // 共识节点，需要您手动输入java: 读取C:\Users\19776\.m2\repository\org\slf4j\slf4j-simple\1.7.32\slf4j-simple-1.7.32.jar时出错;
    public static final String consensusNode = "node-0.organization-zz7npjt8d";

    // 查询数据所使用的节点，需要您手动输入
    public static final String queryNode = "node-0.organization-zz7npjt8d";

    // 链名称，默认使用 default，不需要更改
    public static final String chainID = "bcs-sevuqo-5b90b1e82";

    @Autowired
    private ContractMapper contractMapper;

    public String queryValue(String[] keys){
        String query  = null;
        try {
            SdkClient sdkClient = new SdkClient(configFilePath);
            Contract contract = new Contract(contractName, endorserNodes, consensusNode);
            query = "[info] query transaction success, result is: "+contract.query(sdkClient, "query", keys );

        }  catch (Exception e) {
            System.out.println("[error] catch an exception: " + e);
            query = "[error] query transaction failed, error is: "+e;
        }
        return query;

    }


    public String uploadValue(String[] key_and_value,String name,String cid,String flag,String sort){
            String ret = "success";
            try{
                //SdkClient sdkClient = new SdkClient(configFilePath);
               // Contract contract = new Contract(contractName, endorserNodes, consensusNode);
                //String Txid = contract.send(sdkClient, "insert", key_and_value);
                contractMapper.insertData(name,cid,flag,sort,"1");
            }catch (Exception e){

                System.out.println("[error] catch an exception: " + e);
                ret = "fail";
            }
            return ret;

    }

    public BlockInfo queryBlockInfo(List<ResourceInfo> list){
        BlockInfo blockInfo = new BlockInfo();
        try {
            SdkClient sdkClient= new SdkClient(configFilePath);
            int blockHeight = list.size();
            int tradingCapicity = blockHeight+1;
            List<String> TradeId = new ArrayList<>();
//            System.out.println(blockHeight);
//            for(int i=0;i<blockHeight;i++){
//                BlockOuterClass.Block block = BlockUtil.queryBlockByNumber(sdkClient, i);
//                List<String> txIdList = BlockUtil.getTxIdList(block);
//                String s = txIdList.get(0);
//                TradeId.add(s);
//            }

            blockInfo.setBlockHeight(blockHeight);
            blockInfo.setTradingCapacity(tradingCapicity);
            blockInfo.setTradeId(TradeId);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return blockInfo;
    }
}
