package com.revature.controllers;

import com.revature.dao.ReimbursementDao;
import com.revature.dao.ReimbursementDaoImpl;
import com.revature.models.Reimbursement;
import com.revature.models.ReimbursementType;
import com.revature.models.User;
import com.revature.service.ReimbursementService;
import com.revature.util.LoggingUtil;
import io.javalin.http.Context;

import java.util.List;

import static java.lang.Integer.parseInt;

public class ReimbursementController {
    private ReimbursementDao reimbursementDao = new ReimbursementDaoImpl();
    private ReimbursementService reimbursementService = new ReimbursementService(reimbursementDao);

    public void handleCreateReimbursement(Context ctx){
        LoggingUtil.logger.info("Attempting to create a reimbursement");
        double amount = Double.parseDouble(ctx.formParam("Amount"));
        int author_id = Integer.parseInt(ctx.pathParam("id"));
        ReimbursementType reimbursementType = ReimbursementType.valueOf(ctx.formParam("ReimbursementType"));

        Reimbursement reimbursement = new Reimbursement(amount, author_id, reimbursementType);
        if(reimbursementService.createReimbursement(reimbursement)){
            LoggingUtil.logger.info("User id:" + author_id + " created a reimbursement");
            ctx.result("Successfully created a reimbursement under ID: " + author_id);
            ctx.status(200);
        } else {
            LoggingUtil.logger.warn("User id:" + author_id + " could not create a reimbursement.");
            ctx.status(403);
            ctx.result("Could not create a reimbursement.");
        }
    }


    public void handleGetAllReimbursements(Context ctx){
        List<Reimbursement> reimbursementList = reimbursementService.getAllReimbursements();
        System.out.println(reimbursementList);
       if(reimbursementList.isEmpty()){
           ctx.status(400);
           ctx.result("There are no reimbursements");
       } else{
           ctx.status(200);
           ctx.json(reimbursementList);
       }
    }
    public void handleGetReimbursementsById(Context ctx){
        int id = Integer.parseInt(ctx.pathParam("id"));
        Reimbursement reimbursement = reimbursementService.getReimbursementById(id);
        if(reimbursement==null){
            ctx.status(400);
            ctx.result("Reimbursement does not exist");
        }else{
            ctx.status(200);
            ctx.json(reimbursement);
        }
    }

    public void handleDenyReimbursement(Context ctx){
        int id = parseInt(ctx.pathParam("id"));
        User user = ctx.bodyAsClass(User.class);

        if(reimbursementService.changeReimbursementStatus(id,user.getId(),false)){
            ctx.status(200);
            ctx.result("You successfully denied the reimbursement");
        }else{
            ctx.status(400);
            ctx.result("Reimbursement doesn't exist");
        }
    }

    public void handleApproveReimbursement(Context ctx){
        int id = parseInt(ctx.pathParam("id"));
        User user = ctx.bodyAsClass(User.class);

        if(reimbursementService.changeReimbursementStatus(id,user.getId(),true)){
            ctx.status(200);
            ctx.result("You successfully approved the reimbursement");
        }else{
            ctx.status(400);
            ctx.result("Reimbursement doesn't exist");
        }
    }

    public void handleGetReimbursementsByAuthor(Context ctx){
        int id = parseInt(ctx.pathParam("id"));
        List<Reimbursement> reimbursement= reimbursementService.getAllReimbursementsByAuthor(id);
        if(reimbursement.isEmpty()){
            ctx.status(403);
            ctx.result("There are no reimbursements for this author");
        }else{
            ctx.status(200);
            ctx.json(reimbursement);
        }
    }
    public void handleGetAllReimbursementPending(Context ctx){
        List<Reimbursement> reimbursementPending =reimbursementService.getAllPendingReimbursements();
        System.out.println(reimbursementPending);
        if(reimbursementPending.isEmpty()){
            ctx.result("There are no pending reimbursements");
        }
        else{
            ctx.status(200);
            ctx.json(reimbursementPending);
        }
    }

    public void handleGetAllResolvedReimbursement(Context ctx){
        List<Reimbursement> reimbursementResolve =reimbursementService.getAllResolvedReimbursements();
        if(reimbursementResolve.isEmpty()){
            ctx.result("There are no resolved reimbursements");
        }
        else{
            ctx.status(200);
            ctx.json(reimbursementResolve);
        }
    }
    public void handleGetAllPendingByUserID(Context ctx){
        int id = Integer.parseInt(ctx.pathParam("id"));
        List<Reimbursement> pendingReimbursemetsByUsers =reimbursementService.getAllPendingReimbursementsByAuthor(id);
        System.out.println(id);
        if(pendingReimbursemetsByUsers.isEmpty()){
            ctx.result("There are no pending reimbursement");
        }
        else{
            ctx.status(200);
            ctx.json(pendingReimbursemetsByUsers);
        }
    }
    // ---
     public void  handleGetResolvedByUserID (Context ctx){
    int id = Integer.parseInt(ctx.pathParam("id"));
    List<Reimbursement> resolvedReimbursemetsByUsers =reimbursementService.getAllResolvedReimbursementsByAuthor(id);
      if(resolvedReimbursemetsByUsers.isEmpty()){
          ctx.result("there are no any resolved reimbursement yet");
      }
      else{
          ctx.status(200);
          ctx.json(resolvedReimbursemetsByUsers);}
}
}


