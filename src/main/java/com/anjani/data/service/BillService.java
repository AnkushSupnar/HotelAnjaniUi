package com.anjani.data.service;

import com.anjani.data.common.CommonData;
import com.anjani.data.common.RestTemplateResponseErrorHandler;
import com.anjani.data.entity.Bill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
@Service
public class BillService {
    private RestTemplate template;

    @Autowired
    public BillService(RestTemplateBuilder restTemplateBuilder) {
        this.template = restTemplateBuilder
                .errorHandler(new RestTemplateResponseErrorHandler()).build();
    }
    public Bill getByBillno(Long billno){
        System.out.println("Got bill no "+billno);
        return template.getForObject(CommonData.url+"/bill/bybillno/"+billno,Bill.class,billno);
    }
    public List<Bill> getByDate(LocalDate date){
        return Arrays.asList(template.getForObject(CommonData.url+"/bill/{date}",Bill[].class,date));
    }
    public List<Bill>getCustomerUnpaidBills(Long id, Float paid){
       return Arrays.asList(template.getForObject(CommonData.url+"/customerunpaid/{cid}/{paid}",Bill[].class,id,paid));
    }
    public Bill saveBill(Bill bill){
        System.out.println(CommonData.url+"/bill/save");
       return template.postForObject(CommonData.url+"/bill/save",bill,Bill.class);
    }
    public List<Bill>getByStatus(String status){
        return Arrays.asList(template.getForObject(CommonData.url+"/bill/bystatus/{status}",Bill[].class,status));
    }
    public Bill getByTableStatus(Integer id,String status){
        return template.getForObject(CommonData.url+"/bill/bytableandstatus/{table}/{status}",Bill.class,id,status);
    }
    public Bill getByTableNameAndStatus(String tablename,String status){
        return template.getForObject(CommonData.url+"/bill/bytablenameandstatus/{tablename}/{status}",Bill.class,tablename,status);
    }
    public Bill getClosedBillByTableid(Integer tableid){
        return template.getForObject(CommonData.url+"/bill/bytableidclosedbill/{tableid}",Bill.class,tableid);
    }
    public Bill deleteCloseBillTransaction(long billno){
        return template.getForObject(CommonData.url+"/bill/bybilldeletetransaction/{billno}",Bill.class,billno);
    }
    public List<Bill>getBillWithPagination(int offset,int pageSize){
        return Arrays.asList(template.getForObject(CommonData.url+"/bill/bybillpagination/{offset}/{pageSize}",Bill[].class,offset,pageSize));
    }
}
