package com.example.SGUCharity_Project.Controller;

import com.example.SGUCharity_Project.Config.VNPayService;
import com.example.SGUCharity_Project.Model.Payment_model;
import com.example.SGUCharity_Project.Repository.Payment_Repo;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@org.springframework.stereotype.Controller
public class Payment_controller {
    @Autowired
    private VNPayService vnPayService;

    @Autowired
    private Payment_Repo paymentRepo;


    @GetMapping("/thanh-toan")
    public String home(){
        return "payment/payment_user";
    }

    @PostMapping("/submitOrder")
    public String submidOrder(@RequestParam("amount") int orderTotal,
                              @RequestParam("orderInfo") String orderInfo,
                              HttpServletRequest request){
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        String vnpayUrl = vnPayService.createOrder(orderTotal, orderInfo, baseUrl);
        return "redirect:" + vnpayUrl;
    }

    @GetMapping("/vnpay-payment")
    public String GetMapping(HttpServletRequest request, Model model){
        int paymentStatus = vnPayService.orderReturn(request);

        String orderInfo = request.getParameter("vnp_OrderInfo");
        String paymentTime = request.getParameter("vnp_PayDate");
        String transactionId = request.getParameter("vnp_TransactionNo");
        String totalPrice = request.getParameter("vnp_Amount");

        // Định dạng lại paymentTime
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        LocalDateTime parsedPaymentTime = LocalDateTime.parse(paymentTime, inputFormatter);
        String formattedPaymentTime = parsedPaymentTime.format(outputFormatter);

        // Lưu thông tin thanh toán vào cơ sở dữ liệu
        Payment_model payment = new Payment_model();
        payment.setOrderId(orderInfo);
        payment.setTotalPrice(totalPrice);
        payment.setTransactionId(transactionId);
        payment.setPaymentTime(parsedPaymentTime); // Lưu dưới dạng LocalDateTime
        payment.setPaymentStatus(paymentStatus);
        payment.setDisplay(0);

        paymentRepo.save(payment);

        // Truyền thông tin đã định dạng vào model để hiển thị
        model.addAttribute("orderId", orderInfo);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("paymentTime", formattedPaymentTime);
        model.addAttribute("transactionId", transactionId);

        return paymentStatus == 1 ? "payment/ordersuccess" : "payment/orderfail";
    }
}
