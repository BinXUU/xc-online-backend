package com.xuecheng.framework.model.response;

import lombok.Data;
import lombok.ToString;

/**
 * Created by BinXU on 2020/01/22.
 */

@Data
@ToString
public class QueryResponseResult<T> extends ResponseResult {

    QueryResult<T> queryResult;

    public QueryResponseResult(ResultCode resultCode,QueryResult queryResult){
        super(resultCode);
       this.queryResult = queryResult;
    }

}
