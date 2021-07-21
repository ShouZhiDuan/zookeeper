package com.example.zk.testmain;


import com.example.zk.DTO.StudyInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;


/**
 * @Auther: ShouZhi@Duan
 * @Description:
 */
public class TestMain {
    private final static ObjectMapper objectMapper = new ObjectMapper();

    public static void main(String[] args) throws JSONException {
        //String txt = "{\"studyInfo\":{\"userId\":1, \"name\": \"\\n\\d\\r\\s\\/n\\dd222\", \"startDate\": \"07/19/2021\", \"endDate\": \"07/19/2021\", \"description\": \"\\n\\d\\r\\s\\/n\\dd222\", \"privacy\": \"Private\"}, \"dataSources\": [{\"dataNodeId\":2, \"dataNodeName\": \"杭州节点1\", \"matchedIds\": 0, \"filenames\": [\"5_1_20.0%--1618905125932.csv\"], \"datasetNames\": [], \"sampleSizeArray\": [null,null], \"featuresParas\": null, \"datasetIds\": [2], \"sampleSize\": 5, \"matchRate\": 0.0}], \"server\": \"192.168.10.30\", \"trainingModel\": null, \"model\": \"\", \"analysisMethodId\": 6, \"trainingStudyId\": 0, \"mainStudyId\": 0}";
        String txt = "{\"studyInfo\":\"\\n\\d\\r\\s\\/n\\dd222\"}";
        JSONObject jsonObject = new JSONObject(txt);
        StudyInfo studyInfo = new Gson().fromJson(jsonObject.getJSONObject("studyInfo").toString(), StudyInfo.class);
        System.out.println(studyInfo);
//          test();
    }

    @Test
    public static void test(){
        String s1 = "{\"Hi\":\"\\\\s\"}";
        int i = (int) '/';
        //System.out.println(s1);
        try
        {
            JSONObject json = new JSONObject(s1);
            System.out.println("data = " + json);
        }
        catch (JSONException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
