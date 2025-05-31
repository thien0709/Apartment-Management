package com.apartment_management.services;

import com.apartment_management.configs.VNPayConfig;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@PropertySource("classpath:application.properties")
public class VNPayService {

    @Autowired
    private Environment env;

    public String createOrder(HttpServletRequest request, @RequestBody Map<String, Object> orderRequest, String orderInfor, String urlReturn) {

        List<Integer> invoiceIds = (List<Integer>) orderRequest.get("invoiceId");
        double amount = Double.parseDouble(String.valueOf(orderRequest.get("amount")));

        String invoiceIdString = invoiceIds.stream()
                .map(String::valueOf)
                .collect(Collectors.joining("-"));

        //Các bạn có thể tham khảo tài liệu hướng dẫn và điều chỉnh các tham số
        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String vnp_TxnRef = invoiceIdString;
        String vnp_IpAddr = VNPayConfig.getIpAddress(request);
        String vnp_TmnCode = env.getProperty("vnp.tmnCode");
        String orderType = "order-type";

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf((long) (amount * 100)));

        vnp_Params.put("vnp_CurrCode", "VND");

        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", orderInfor);
        vnp_Params.put("vnp_OrderType", orderType);

        String locate = "vn";
        vnp_Params.put("vnp_Locale", locate);

        urlReturn += env.getProperty("vnp.returnUrl");
        vnp_Params.put("vnp_ReturnUrl", urlReturn);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List fieldNames = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                try {
                    hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                    //Build query
                    query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                    query.append('=');
                    query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String salt = env.getProperty("vnp.hashSecret");
        String vnp_SecureHash = VNPayConfig.hmacSHA512(salt, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        return env.getProperty("vnp.payUrl") + "?" + queryUrl;
    }

    public int orderReturn(HttpServletRequest request) {
        Map<String, String> fields = new HashMap<>();

        // Lấy tham số từ request, KHÔNG encode lại
        Enumeration<String> params = request.getParameterNames();
        while (params.hasMoreElements()) {
            String fieldName = params.nextElement();
            String fieldValue = request.getParameter(fieldName);
            if (fieldValue != null && fieldValue.length() > 0) {
                fields.put(fieldName, fieldValue);
            }
        }

        // Lấy giá trị secure hash từ request
        String vnp_SecureHash = request.getParameter("vnp_SecureHash");

        // Loại bỏ tham số không cần thiết để tính hash
        fields.remove("vnp_SecureHashType");
        fields.remove("vnp_SecureHash");

        // Sắp xếp tham số theo key alphabet
        List<String> fieldNames = new ArrayList<>(fields.keySet());
        Collections.sort(fieldNames);

        // Tạo chuỗi dữ liệu để hash
        StringBuilder hashData = new StringBuilder();
        for (int i = 0; i < fieldNames.size(); i++) {
            String key = fieldNames.get(i);
            String value = fields.get(key);
            if (value != null && value.length() > 0) {
                hashData.append(key).append('=').append(value);
                if (i < fieldNames.size() - 1) {
                    hashData.append('&');
                }
            }
        }

        // Tính hash với secret key
        String secretKey = env.getProperty("vnp.hashSecret"); // hoặc lấy từ config
        String calculatedHash = VNPayConfig.hmacSHA512(secretKey, hashData.toString());

        // So sánh hash (chú ý chữ hoa/thường)
        if (calculatedHash.equalsIgnoreCase(vnp_SecureHash)) {
            // Kiểm tra trạng thái giao dịch
            if ("00".equals(request.getParameter("vnp_TransactionStatus"))) {
                return 1; // thành công
            } else {
                return 0; // thất bại
            }
        } else {
            return -1; 
        }
    }

}
 