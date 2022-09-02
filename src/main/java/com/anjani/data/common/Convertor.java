package com.anjani.data.common;

import com.anjani.data.entity.TempTransaction;
import com.anjani.data.entity.Transaction;
import org.springframework.stereotype.Component;

@Component
public class Convertor {

    public  TempTransaction transactionToTemTransaction(Transaction transaction){

        return null;

    }
    static public TempTransaction transactionToTemptransaction(Transaction tr){
        return TempTransaction.builder()
                .amount(tr.getAmount())
                .printqty(0.0f)
                .employee(tr.getBill().getWaitor())
                .quantity(tr.getQuantity())
                .tableMaster(tr.getBill().getTable())
                .itemname(tr.getItemname())
                .rate(tr.getRate())
                .build();

    }

}
