package com.ellin.batch.batchk8.batch;

import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;

import java.util.HashMap;
import java.util.Map;

public class S3MultiObjectPartitioner implements Partitioner {

    String[] keys;

    String keyName = "keyName";


    public void setKeys(String[] keys) {
        this.keys = keys;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    @Override
    public Map<String, ExecutionContext> partition(int gridSize) {
        Map<String, ExecutionContext> map = new HashMap(gridSize);
        int i = 0;
        for(String key:keys){
            ExecutionContext context = new ExecutionContext();
            context.putString(this.keyName, key);
            map.put("partition" + i, context);
            ++i;
        };
        return map;
    }
}
