package com.example.ecombackend.customer;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;
//
//@RestController
//@RequestMapping("/payments")
//@CrossOrigin(origins = {"http://localhost:3000","https://grabzy-e-com-frontend.vercel.app","https://grabzy-hub-e-com-frontend.vercel.app"}, allowCredentials = "true")
//public class PaymentController {
//
//    @PostMapping("/create-order")
//    public ResponseEntity<String> createOrder(@RequestBody Map<String, Object> data) throws RazorpayException {
//        RazorpayClient client = new RazorpayClient(
//                "rzp_test_vxv4CM7EvhMf2a",
//                "iahfx7zxt13tWY1o5VVKXHk4"
//        );
//
//        // ✅ Ensure receipt is max 40 characters
//        String receiptId = "order_rcptid_" + UUID.randomUUID().toString().replace("-", "").substring(0, 20);
//
//        JSONObject options = new JSONObject();
//        options.put("amount", data.get("amount")); // amount in paise
//        options.put("currency", "INR");
//        options.put("receipt", receiptId);
//        options.put("payment_capture", true);
//
//        Order order = client.orders.create(options);
//        return ResponseEntity.ok(order.toJson().toString()); // returns JSON string
//
//    }
//}





import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
@CrossOrigin(
        origins = {
                "http://localhost:3000",
                "https://grabzy-e-com-frontend.vercel.app",
                "https://grabzy-hub-e-com-frontend.vercel.app"
        },
        allowCredentials = "true"
)
public class PaymentController {

    @Value("${razorpay.key_id}")
    private String razorpayKeyId;

    @Value("${razorpay.key_secret}")
    private String razorpayKeySecret;

    @PostMapping("/create-order")
    public ResponseEntity<String> createOrder(@RequestBody Map<String, Object> data) throws RazorpayException {
        RazorpayClient client = new RazorpayClient(razorpayKeyId, razorpayKeySecret);

        String receiptId = "order_rcptid_" + UUID.randomUUID().toString().replace("-", "").substring(0, 20);

        JSONObject options = new JSONObject();
        options.put("amount", data.get("amount"));
        options.put("currency", "INR");
        options.put("receipt", receiptId);
        options.put("payment_capture", true);

        Order order = client.orders.create(options);
        return ResponseEntity.ok(order.toJson().toString());
    }
}
