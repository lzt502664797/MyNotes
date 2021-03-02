package cn.taqu.dns;

import mousio.etcd4j.EtcdClient;
import mousio.etcd4j.promises.EtcdResponsePromise;
import mousio.etcd4j.requests.EtcdKeyGetRequest;
import mousio.etcd4j.responses.EtcdKeysResponse;

import java.io.IOException;
import java.net.URI;

/**
 * @Description:
 * @Author linzt
 * @Date 2021年2月18日 0018
 */
public class EtcdClientAddListenerTest {
    private static Long waitIndex;

    public static void main(String[] args) throws Exception {
        EtcdClientAddListenerTest etcdClientAddListenerTest = new EtcdClientAddListenerTest();
        etcdClientAddListenerTest.executeBefore();
        etcdClientAddListenerTest.testListener();
    }

    EtcdClient client = null;

    public void executeBefore() {
        client = new EtcdClient(URI.create("http://10.10.60.229:2479"));
    }

    private String getConfig(String configFile , EtcdKeysResponse dataTree) {

        if(null != dataTree ) {
            return dataTree.getNode().getValue();
        }
        System.out.println("Etcd configFile"+ configFile+"is not exist,Please Check");
        return null;
    }

    public void testListener() throws Exception{
        this.startListenerThread(client , "antispam_v2/dns");
    }

    public void startListenerThread(EtcdClient client , String dir) throws Exception{

        EtcdKeyGetRequest clientDir = client.getDir(dir).consistent();
        EtcdKeysResponse dataTree = clientDir.send().get();
        waitIndex = dataTree.etcdIndex;

        if(null != dataTree && dataTree.getNode().getNodes().size()>0) {
            getAllKeyStartThread(dataTree.node);
        }

    }

    public void getAllKeyStartThread(EtcdKeysResponse.EtcdNode etcdNode){

        if(null != etcdNode.nodes && etcdNode.nodes.size()>0) {
            for(EtcdKeysResponse.EtcdNode node:etcdNode.nodes) {
                if (node.dir){
                    getAllKeyStartThread(node);
                } else{
                    new Thread(()->{
                        startListener(client,node.key,waitIndex + 1);
                    }).start();
                    System.out.println(node.key + " is not dir");
                }
            }
        }
    }

    public void startListener(final EtcdClient client , final String dir , long waitIndex)  {
        EtcdResponsePromise<EtcdKeysResponse> promise = null;
        try {
            // 如果监听的waitIndex 不存在与key对应的EventHistroy 中（currentIndex >= waitIndex） ，
            // 并且key对应的modifyIndex > waitIndex , 则会查找到第一个大于waitIndex 的数据进行展示
            promise = client.get(dir).waitForChange(waitIndex).consistent().send();
        } catch (IOException e) {
            e.printStackTrace();
        }

        promise.addListener(promise1 -> {
            try {
                EtcdKeysResponse etcdKeysResponse = promise1.get();
                new Thread(() -> {startListener(client , dir , waitIndex + 1);}).start();
                String config = getConfig(dir, etcdKeysResponse);//加载配置项
                System.out.println(config);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("listen etcd 's config change cause exception:{}"+e.getMessage());
            }
        });
    }


}
