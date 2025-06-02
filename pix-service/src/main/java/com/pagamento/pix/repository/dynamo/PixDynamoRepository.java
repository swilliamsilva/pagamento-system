package com.pagamento.pix.repository.dynamo;

import com.pagamento.pix.model.PixTransaction;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class PixDynamoRepository {

    @Autowired
    private DynamoDBMapper dynamoDBMapper;

    public PixTransaction save(PixTransaction transaction) {
        dynamoDBMapper.save(transaction);
        return transaction;
    }

    public PixTransaction findById(String id) {
        return dynamoDBMapper.load(PixTransaction.class, id);
    }
}
