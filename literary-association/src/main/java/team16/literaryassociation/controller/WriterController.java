package team16.literaryassociation.controller;

import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team16.literaryassociation.dto.StartProcessDTO;
import team16.literaryassociation.model.MembershipApplication;
import team16.literaryassociation.model.Writer;
import team16.literaryassociation.services.interfaces.UserService;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping(value = "/api/writer")
public class WriterController {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private FormService formService;

    @Autowired
    private IdentityService identityService;

    @Autowired
    private UserService userService;


    @GetMapping(value = "/start-process-register")
    public ResponseEntity<?> startWriterRegistration() {

        ProcessInstance pi = runtimeService.startProcessInstanceByKey("Process_Writer_Reg");
        Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).list().get(0);
        task.setAssignee("user");
        taskService.saveTask(task);
        StartProcessDTO sp = new StartProcessDTO(pi.getId(), task.getId());
        return new ResponseEntity(sp, HttpStatus.OK);
    }

    @PutMapping(value = "/activateAccount/{processId}/{token}")
    public ResponseEntity<?> activateAccount(@PathVariable String processId, @PathVariable String token){

        System.out.println("Usao u Activate account");
        System.out.println("Process Id: " + processId);
        System.out.println("Token Id :" + token);

        List<Task> tasks = taskService.createTaskQuery().processInstanceId(processId).list();
        Task activationTask = null;
        try {
            activationTask = tasks.get(0);
        }catch(Exception e){//ako se proces vec zavrsio, sto znaci da je isteklo vreme za aktivaciju
            return ResponseEntity.badRequest().body("Account activation failed. Token expired.");
        }
        runtimeService.setVariable(processId, "token", token);
        HashMap<String, Object> map = new HashMap<>();
        formService.submitTaskForm(activationTask.getId(), map);

        return ResponseEntity.ok().body("Account activation successful");
    }

    @GetMapping(value = "/membership-fee-payment")
    public ResponseEntity<?> startMembershipFeePayment() {
        System.out.println("Usao u membership fee payment");
        String username = identityService.getCurrentAuthentication().getUserId();
        Writer writer = (Writer) userService.findByUsername(username);
        if(writer == null)
        {
            System.out.println("Nije nasao korisnika");
            return new ResponseEntity("User not found",HttpStatus.BAD_REQUEST);
        }
        MembershipApplication membershipApplication = writer.getMembershipApplication();
        if(membershipApplication == null)
        {
            System.out.println("Nije nasao membership application");
            return new ResponseEntity("Membership application not found",HttpStatus.BAD_REQUEST);
        }
        if(membershipApplication.getApprovedForPaying() == null)
        {
            System.out.println("Membership application jos uvek nije odobrena");
            return new ResponseEntity("Membership application not approved yet",HttpStatus.BAD_REQUEST);
        }
        if(membershipApplication.isPaid())
        {
            System.out.println("Membership application je vec placena");
            return new ResponseEntity("Membership application already paid",HttpStatus.BAD_REQUEST);
        }
        String processId = membershipApplication.getProcessId();
        if (processId == null)
        {
            System.out.println("Nije nasao processid");
            return new ResponseEntity("Process not found",HttpStatus.BAD_REQUEST);
        }
        runtimeService.startProcessInstanceByKey("Pay_Membership_Fee_Process");
        return new ResponseEntity("Membership application fee paid",HttpStatus.OK);
    }

}
