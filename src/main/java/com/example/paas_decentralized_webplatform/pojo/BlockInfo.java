package com.example.paas_decentralized_webplatform.pojo;


import java.util.List;

public class BlockInfo {

    private int blockHeight;
    private int tradingCapacity;
    private List<String> tradeId;



    public BlockInfo(){

    }

    public BlockInfo(int blockHeight, int tradingCapacity, List<String> tradeId) {
        this.blockHeight = blockHeight;
        this.tradingCapacity = tradingCapacity;
        this.tradeId = tradeId;
    }

    public int getBlockHeight() {
        return this.blockHeight;
    }

    public void setBlockHeight(int blockHeight) {
        this.blockHeight = blockHeight;
    }


    public int getTradingCapacity() {
        return tradingCapacity;
    }
    public void setTradingCapacity(int tradingCapacity) {
        this.tradingCapacity = tradingCapacity;
    }

    public List<String> getTradeId() {
        return this.tradeId;
    }

    public void setTradeId(List<String> tradeId) {
        this.tradeId = tradeId;
    }
}
