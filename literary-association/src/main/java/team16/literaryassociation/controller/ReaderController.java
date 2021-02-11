package team16.literaryassociation.controller;

import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team16.literaryassociation.dto.FormSubmissionDTO;
import team16.literaryassociation.dto.StartProcessDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/reader")
public class ReaderController {

    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private FormService formService;


    @GetMapping(value = "/start-process-register")
    public ResponseEntity<?> startReaderRegistration() {

        ProcessInstance pi = runtimeService.startProcessInstanceByKey("Process_Reader_Reg");
        Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).list().get(0);
        taskService.saveTask(task);
        StartProcessDTO sp = new StartProcessDTO(pi.getId(), task.getId());
        return new ResponseEntity(sp, HttpStatus.OK);
    }

    @PutMapping(value = "/activateAccount/{processId}/{token}")
    public ResponseEntity<?> activateAccount(@PathVariable String processId, @PathVariable String token){

        System.out.println(processId);
        System.out.println(token);

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


}
