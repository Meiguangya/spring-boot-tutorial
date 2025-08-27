//package com.mgy.springboottutorial.customstarter;
//
//import com.mgy.ChatService;
//import jakarta.annotation.PostConstruct;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//@Component
//public class UseCustomerStarter {
//
//
//    public ChatService chatService;
//
//    @Autowired
//    public void setChatService(ChatService chatService) {
//        this.chatService = chatService;
//    }
//
//    @PostConstruct
//    public void init(){
//        System.out.println("UseCustomerStarter start");
//        chatService.doSomething();
//        System.out.println("UseCustomerStarter end");
//    }
//
//}
