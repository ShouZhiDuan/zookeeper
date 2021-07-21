package com.example.zk.testmain;

import org.apache.curator.framework.AuthInfo;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.data.Stat;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;
import org.junit.Test;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: ShouZhi@Duan
 * @Description: 权限操作
 */
public class ACLClientTest {
    private final static String CONNECT_STR = "192.168.10.33:2185";
    private final static String PWD_1 = "123456";
    private final static String PWD_2 = "666666";
    private final static String NAME_SPACE = "namespace-new-1";

    private static CuratorFramework curatorFramework;

    static {
        System.out.println("======初始化zookeeper连接======");
        AuthInfo authInfo = new AuthInfo("digest",PWD_2.getBytes());
        List<AuthInfo> authInfos=new ArrayList<>();
        authInfos.add(authInfo);
        curatorFramework = CuratorFrameworkFactory.builder().
                connectString(CONNECT_STR)
                .sessionTimeoutMs(5000)
                .retryPolicy(new ExponentialBackoffRetry(1000,3))
                .authorization(authInfos) //授权
                .namespace(NAME_SPACE)
                .build();
        curatorFramework.start();
        System.out.println("======连接成功======");
    }

    private List<ACL> acls() throws NoSuchAlgorithmException {
        List<ACL> acl = new ArrayList<>();
        //指定digest模式类似于终端命令：setAcl /namespace-new digest:userName:123456:dw
        //签名：echo -n user1:666666 | openssl dgst -binary -sha1 | openssl base64
        Id id1 = new Id("digest", DigestAuthenticationProvider.generateDigest(PWD_1));
        Id id2 = new Id("digest", DigestAuthenticationProvider.generateDigest(PWD_2));
//        ACL acl1 = new ACL(ZooDefs.Perms.ALL, id1);
//        acl.add(acl1);
        //ACL acl2 = new ACL(ZooDefs.Perms.DELETE | ZooDefs.Perms.READ, id2);
        ACL acl2 = new ACL(ZooDefs.Perms.READ, id2);
        acl.add(acl2);
        return acl;
    }

    /**
     * 添加节点
     */
    @Test
    public void testDoACL() throws Exception {
        List<ACL> acls = acls();
        curatorFramework.create()
                .creatingParentsIfNeeded() //存在就不创建
                .withMode(CreateMode.PERSISTENT)//数据模式
                .withACL(acls,true) //设置权限
                .forPath("/test-6","测试目录权限".getBytes());
        curatorFramework.close();
    }

    /**
     * 查询节点的值
     */
    @Test
    public void test2() throws Exception {
        byte[] bytes = curatorFramework
                .getData()
                .forPath("/test-6");
        String s = new String(bytes);
        System.err.println(s);
        curatorFramework.close();
    }


    /**
     * 修改已存在节点的权限
     */
    @Test
    public void modify() throws Exception {
        curatorFramework.start();
        Stat stat = curatorFramework.setACL().withACL(acls()).forPath("/path");
        System.out.println(stat);
        curatorFramework.close();
    }





}
