package com.example.paas_decentralized_webplatform.pojo;

public class NodeInfo {
    private String id;
    private int group =(int) (Math.random()*(15));


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "NodeInfo{" +
                "id='" + id + '\'' +
                ", group=" + group +
                '}';
    }

    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        this.group = group;
    }
}
