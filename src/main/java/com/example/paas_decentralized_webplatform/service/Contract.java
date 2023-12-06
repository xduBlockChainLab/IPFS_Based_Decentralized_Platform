/*
 * Copyright(c) Huawei Technologies Co.,Ltd. 2021. All right reserved.
 */

package com.example.paas_decentralized_webplatform.service;

import com.google.common.base.Charsets;
import com.google.protobuf.ByteString;
import com.huawei.wienerchain.SdkClient;
import com.huawei.wienerchain.WienerChainNode;
import com.huawei.wienerchain.message.Builder;
import com.huawei.wienerchain.message.action.TxEvent;
import com.huawei.wienerchain.message.build.ContractRawMessage;
import com.huawei.wienerchain.proto.common.Message.RawMessage;
import com.huawei.wienerchain.proto.common.Message.Response;
import com.huawei.wienerchain.proto.common.Message.Status;
import com.huawei.wienerchain.proto.common.TransactionOuterClass.CommonTxData;
import com.huawei.wienerchain.proto.common.TransactionOuterClass.Transaction;
import com.huawei.wienerchain.proto.common.TransactionOuterClass.TxPayload;
import com.huawei.wienerchain.proto.common.TransactionOuterClass.TxResult;
import com.huawei.wienerchain.proto.nodeservice.ContractOuterClass.Invocation;
import org.bouncycastle.util.encoders.Hex;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * 功能描述: 执行合约相关的操作，如发送和查询交易数据
 *
 * @author zyp
 * @since 2022-8-25
 */
public class Contract {

    private final String contractName;

    private final String endorserNodes;

    private final String consensusNode;

    private static final int FUTURE_TIMEOUT = 20;

    private static final int LISTEN_TIMEOUT = 200;

    public Contract(String contractName, String endorserNodes, String consensusNode) {
        this.contractName = contractName;
        this.endorserNodes = endorserNodes;
        this.consensusNode = consensusNode;
    }

    // 使用合约进行交易发送。数据修改类的操作使用此方法
    public String send(SdkClient sdkClient, String func, String[] args) throws Exception {
        String[] nodes = endorserNodes.split(",");
        if (nodes.length == 0) {
            System.out.println("[warn] Please input at least one node");
            return "false";
        }
        ContractRawMessage contractRawMessage = sdkClient.getContractRawMessage();
        // 1. 合约调用消息构建

        Invocation invocation = contractRawMessage.buildInvocation(App.chainID, contractName, func, args);
        // 2. 背书请求消息构建与发送
        RawMessage[] invokeResponses = new RawMessage[nodes.length];
        for (int i = 0; i < nodes.length; i++) {
            invokeResponses[i] = generateInvokeResponse(sdkClient, nodes[i], contractRawMessage, invocation);
        }
        WienerChainNode consensusNode = sdkClient.getWienerChainNode(this.consensusNode);
        TxEvent txEvent = sdkClient.getWienerChainNode(nodes[0]).getAyncEventAction().registerTxEvent(App.chainID);
        // 3. 落盘消息构建
        Builder.TxRawMsg txMsg = contractRawMessage.buildTransactionMessage(invokeResponses);
        // 4. 落盘消息发送
        ByteString payload = consensusNode.getContractAction().transaction(txMsg.msg)
                .get(FUTURE_TIMEOUT, TimeUnit.SECONDS).getPayload();
        // 5. 解析发送结果
        Future<TxResult> future = txEvent.registerTx(txMsg.hash);
        Response response = Response.parseFrom(payload);
        if (response.getStatus() == Status.SUCCESS) {
            TxResult txResult = future.get(LISTEN_TIMEOUT, TimeUnit.SECONDS);
            String txId = new String(Hex.encode(txResult.getTxHash().toByteArray()));
            System.out.println("[info] dropped into block, txId is: " + txId);
            String result = txResult.getStatus().toString();
            System.out.println("[info] send transaction result is: " + result);
            // 执行到此处都会落块，但只有 result 为 VALID 时，交易才被认为有效
            return txId;
        }
        System.out.println("[error] send transaction failed, error is: " + response.getStatus() + ": "
                + response.getStatusInfo());
        return "false";
    }

    // 使用合约进行交易查询。数据查询类的操作使用此方法
    public String query(SdkClient sdkClient, String func, String[] args) throws Exception {
        ContractRawMessage contractRawMessage = sdkClient.getContractRawMessage();
        // 1. 合约调用消息构建
        Invocation invocation = contractRawMessage.buildInvocation(App.chainID, contractName, func, args);
        // 2. 查询请求消息构建与发送
        RawMessage invokeResponse = generateInvokeResponse(sdkClient, App.queryNode, contractRawMessage, invocation);
        // 3. 解析查询结果
        Response response = Response.parseFrom(invokeResponse.getPayload());
        if (response.getStatus() == Status.SUCCESS) {
            Transaction transaction = Transaction.parseFrom(response.getPayload());
            TxPayload txPayload = TxPayload.parseFrom(transaction.getPayload());
            CommonTxData commonTxData = CommonTxData.parseFrom(txPayload.getData());
            String result = commonTxData.getResponse().getPayload().toString(Charsets.UTF_8);
            System.out.println("[info] query transaction success, result is: " + result);
            return result;
        }
        System.out.println("[error] query transaction failed, error is: " + response.getStatus() + ": "
                + response.getStatusInfo());
        return null;
    }

    // 私有方法，获取调用合约所产生的结果
    private RawMessage generateInvokeResponse(SdkClient sdkClient, String nodeName,
                                              ContractRawMessage contractRawMessage, Invocation invocation) throws Exception {
        WienerChainNode node = sdkClient.getWienerChainNode(nodeName);
        RawMessage rawMessage = contractRawMessage.getRawMessageBuilder(invocation.toByteString())
                .setType(RawMessage.Type.DIRECT).build();
        return node.getContractAction().invoke(rawMessage).get(FUTURE_TIMEOUT, TimeUnit.SECONDS);
    }
}
