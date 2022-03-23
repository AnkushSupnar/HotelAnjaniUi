package com.anjani.data.service;
import com.anjani.data.common.CommonData;
import com.anjani.data.common.RestTemplateResponseErrorHandler;
import com.anjani.data.entity.TableMaster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import java.util.Arrays;
import java.util.List;

@Service
public class TableMasterService {
    private RestTemplate template;
    @Autowired
    public TableMasterService(RestTemplateBuilder restTemplateBuilder) {
        this.template = restTemplateBuilder
                .errorHandler(new RestTemplateResponseErrorHandler()).build();
    }
    public List<TableMaster> getAllTables(){
        return Arrays.asList(template.getForObject(CommonData.url+"/tablemaster/all",TableMaster[].class));
    }
    public TableMaster getById(Integer id){
        return template.getForObject(CommonData.url+"/tablemaster/byid/{id}",TableMaster.class,id);
    }
    public TableMaster getByName(String name){
        return template.getForObject(CommonData.url+"/tablemaster/byname/{name}",TableMaster.class,name);
    }
    public List<String> getAllNames(){
        return Arrays.asList(template.getForObject(CommonData.url+"/tablemaster/allnames",String[].class));
    }
    public TableMaster getByTableNameAndGroupName(String tablename,String groupname){
        try {
            TableMaster response = template.getForObject(CommonData.url + "/bytablenamegroupname/{tablename}/{groupname}", TableMaster.class, tablename, groupname);
            System.out.println("Got  " + response);
            if(response.getId()==null){
                return null;
            }
            return response;
           // return template.getForObject(CommonData.url+"/bytablenamegroupname/{tablename}/{groupname}",TableMaster.class,tablename,groupname);
        }catch(HttpStatusCodeException codeEx){
            System.out.println("Got Code=="+codeEx.getStatusCode());
            return null;

        }

    }
    public List<TableMaster>getByTableGroup(String group){
        return Arrays.asList(template.getForObject(CommonData.url+"/tablemaster/bygroup/{group}",TableMaster[].class,group));
    }

    public TableMaster save(TableMaster tableMaster){
        return template.postForObject(CommonData.url+"/tablemaster/save",tableMaster,TableMaster.class);
    }

}
