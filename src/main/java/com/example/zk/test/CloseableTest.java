package com.example.zk.test;

/**
 * @Auther: ShouZhi@Duan
 * @Description:
 */
public class CloseableTest implements AutoCloseable {

    public  void test0() {
            System.out.println("test0方法 处理逻辑");
    }

    public  void test1() {
        try {
            System.out.println("test1方法 处理逻辑");
        } catch (Exception e) {
            System.out.println("test1方法 异常处理");
        } finally {
            System.out.println("test1方法 释放资源");
        }
    }
    public  void test2() {
        try  {
           throw new RuntimeException("");
           // System.out.println("test2方法 处理逻辑");
        } catch (Exception e) {
            System.out.println("test2方法 处理异常");
        }
    }

    @Override
    public void close(){
        System.out.println("执行关闭操作");
    }

    public static void main(String[] args) {
        try (CloseableTest closeableTest = new CloseableTest()){
            System.out.println("==1==");
            new CloseableTest().test0();
        } catch (Exception e){
            System.out.println("==2==");
        }

    }


}
