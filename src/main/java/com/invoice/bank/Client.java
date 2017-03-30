package com.invoice.bank;

import feign.Body;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import java.util.List;

public interface Client {
    @RequestLine("POST v1/operation-history/{accountId}")
    @Headers({"Content-Type: application/json", "Authorization: Bearer {token}"})
    @Body("%7B" +
            "  \"category\": \"Debet\"," +
            "  \"from\": \"{fromDate}\"," +
            "%7D")
    List<Operation> getLatestOperation(
            @Param("accountId") String accountId, @Param("token") String token, @Param("fromDate") String fromDate);
}