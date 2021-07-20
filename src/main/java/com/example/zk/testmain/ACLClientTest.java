package com.example.zk.testmain;

import org.apache.curator.framework.AuthInfo;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
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
    private final static String NAME_SPACE = "namespace";

    private static CuratorFramework curatorFramework;

    private static CuratorFramework curatorFrameworkQuery;

    static {
//        AuthInfo authInfo = new AuthInfo("digest",PWD_1.getBytes());
//        List<AuthInfo> authInfos=new ArrayList<>();
//        authInfos.add(authInfo);
        curatorFramework = CuratorFrameworkFactory.builder().
                connectString(CONNECT_STR)
                .sessionTimeoutMs(5000)
                .retryPolicy(new ExponentialBackoffRetry(1000,3))
                //.authorization(authInfos)
                //.namespace(NAME_SPACE)
                .build();
    }
    static {
        AuthInfo authInfo = new AuthInfo("digest",PWD_1.getBytes());
        List<AuthInfo> authInfos=new ArrayList<>();
        authInfos.add(authInfo);
        curatorFrameworkQuery = CuratorFrameworkFactory.builder().
                connectString(CONNECT_STR)
                .sessionTimeoutMs(5000)
                .retryPolicy(new ExponentialBackoffRetry(1000,3))
                .authorization(authInfos) //授权
                //.namespace(NAME_SPACE) //工作空间
                .build();
    }

    private List<ACL> acls() throws NoSuchAlgorithmException {
        List<ACL> acl = new ArrayList<>();
        Id id1 = new Id("digest", DigestAuthenticationProvider.generateDigest(PWD_1));
        Id id2 = new Id("digest", DigestAuthenticationProvider.generateDigest(PWD_2));
        ACL acl1 = new ACL(ZooDefs.Perms.ALL, id1);
        ACL acl2 = new ACL(ZooDefs.Perms.DELETE | ZooDefs.Perms.READ, id2);
        acl.add(acl1);
        acl.add(acl2);
        return acl;
    }

    /**
     * 添加节点
     */
    @Test
    public void testDoACL() throws Exception {
        List<ACL> acls = acls();
        curatorFramework.start();
        curatorFramework.create()
                .withMode(CreateMode.PERSISTENT)//数据模式
                .withACL(acls) //设置权限
                .forPath("/acls-test-2","测试目录权限".getBytes());
        curatorFramework.close();
    }

    /**
     * 查询节点
     */
    @Test
    public void test2() throws Exception {
        curatorFramework.start();
        byte[] bytes = curatorFramework
                .getData()
                .forPath("/acls-test-2");
        String s = new String(bytes);
        System.out.printf(s);
    }





}
