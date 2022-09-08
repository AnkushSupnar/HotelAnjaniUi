package com.anjani.data.service;

import com.anjani.data.common.CommonData;
import com.anjani.data.common.RestTemplateResponseErrorHandler;
import com.anjani.data.entity.TableMaster;
import com.anjani.data.entity.TempTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class TempTransactionService {
    private RestTemplate template;
    @Autowired
    public TempTransactionService(RestTemplateBuilder restTemplateBuilder) {
        this.template = restTemplateBuilder
                .errorHandler(new RestTemplateResponseErrorHandler()).build();
    }
    public List<TempTransaction> getAll(){
        return Arrays.asList(template.getForObject(CommonData.url+"/temptransation/all",TempTransaction[].class));
    }
    public List<TempTransaction>getByTableId(Integer tableid){
        System.out.println(CommonData.url+"/temptransaction/bytableid/"+tableid);
        return Arrays.asList(template.getForObject(CommonData.url+"/temptransaction/bytableid/{tableid}",TempTransaction[].class,tableid));
    }
    public TempTransaction save(TempTransaction temp){
        return template.postForObject(CommonData.url+"/temptransaction/save",temp,TempTransaction.class);
    }
    public void deleteTempTransaction(Integer tableid){
        Map<String,Integer> map = new HashMap<>();
        template.delete(CommonData.url+"/temptransaction/deletebytableid/{tableid}",map);
    }
    public List<TempTransaction>deleteByTable(TableMaster table){
        try {
              template.exchange(CommonData.url+"/temptransaction/deletebytable",
                      HttpMethod.DELETE,
                      new HttpEntity<TableMaster>(table),
                      TempTransaction[].class
                      );
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }
    public TempTransaction getByItemAndTable(Long item,Integer table){
        return template.getForObject(CommonData.url+"/temptransaction/byitemidandtableid/{item}/{table}",TempTransaction.class,item,table);
    }
    public TempTransaction getByItemAndTableAndRate(String itemname,Integer table,Float rate){
        return template.getForObject(CommonData.url+"/temptransaction/byitemidandtableidandrate/{itemname}/{table}/{rate}",TempTransaction.class,itemname,table,rate);
    }
    public void deleteById(Long id){
        template.delete(CommonData.url+"/temptransaction/deletebyid/{id}",id);
    }
    public List<TableMaster>getOpenTable(){
        return Arrays.asList(template.getForObject(CommonData.url+"/temptransaction/getopentable",TableMaster[].class));
    }
    public List<String>getOpenTableNames(){
        List<TableMaster>list =Arrays.asList(template.getForObject(CommonData.url+"/temptransaction/getopentable",TableMaster[].class));
        List<String>names = new ArrayList<>();
        for(TableMaster t:list){
            names.add(t.getTablename());
        }
        return names;
    }
    public List<TempTransaction>getOrder(Integer tableid){
        return Arrays.asList(template.getForObject(CommonData.url+"/temptransaction/getorder/{id}",TempTransaction[].class,tableid));
    }
    public int resetPrintQuantity(Integer tableid){
        Map < String, Integer > params = new HashMap < String, Integer>();
        params.put("tableid", tableid);
        template.put(CommonData.url+"/temptransaction/resetPrintQuantity/{tableid}",null,params);
        return 1;
    }

}
