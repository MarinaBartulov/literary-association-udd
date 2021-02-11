package team16.literaryassociation.controller;


import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.TaskFormData;
import org.camunda.bpm.engine.identity.Group;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import team16.literaryassociation.dto.FormFieldsDTO;
import team16.literaryassociation.dto.FormSubmissionDTO;
import team16.literaryassociation.dto.TaskDTO;
import team16.literaryassociation.dto.StartProcessDTO;
import team16.literaryassociation.model.User;
import team16.literaryassociation.security.auth.JwtBasedAuthentication;
import team16.literaryassociation.services.interfaces.UploadDownloadService;

import javax.validation.Valid;
import javax.ws.rs.PathParam;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/task")
public class TaskController {

    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private FormService formService;
    @Autowired
    private IdentityService identityService;
    @Autowired
    private UploadDownloadService uploadDownloadService;

    @GetMapping(value="/get-form-fields/{taskId}")
    public FormFieldsDTO getFormFields(@PathVariable("taskId") String taskId){
        System.out.println("Usao u get form fields");
        TaskFormData tfd = formService.getTaskFormData(taskId);
        System.out.println("Dobavio form fields");
        List<FormField> properties = tfd.getFormFields();
        return new FormFieldsDTO(properties);
    }

    @PostMapping(value = "/submit-form/{taskId}", produces = "application/json")
    public ResponseEntity<?> submitForm(@Valid @RequestBody List<FormSubmissionDTO> formData, @PathVariable("taskId") String taskId) {
        System.out.println("Usao u submit form");
        Map<String, Object> fieldsMap = listFieldsToMap(formData);
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId = task.getProcessInstanceId();

        runtimeService.setVariable(processInstanceId, "formData", formData);

        try{
            System.out.println("Dodje dovde1");
            formService.submitTaskForm(taskId, fieldsMap);
            System.out.println("Dodje dovde2");

        } catch(Exception e){
            return new ResponseEntity("Camunda validation failed.", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();

        if(pi != null) {
            System.out.println("Process instance nije null");
            Object globalErrorObj = runtimeService.getVariable(processInstanceId, "globalError");
            if(globalErrorObj != null){
                System.out.println("Global error object nije null");
                boolean globalError = (boolean) globalErrorObj;
                System.out.println("Global error je " + globalError);
                if(globalError){
                    String globalErrorMessage = (String) runtimeService.getVariable(processInstanceId, "globalErrorMessage");
                    System.out.println("Global error message je " + globalErrorMessage);
                    runtimeService.setVariable(processInstanceId, "globalError", false);
                    runtimeService.setVariable(processInstanceId, "globalErrorMessage", null);
                    return ResponseEntity.badRequest().body(globalErrorMessage);
                }
            }
        }

        //Proveri se da li ima odmah aktivan sledeci task
        String username = identityService.getCurrentAuthentication().getUserId();
        Task nextTask = taskService.createTaskQuery().taskAssignee(username).active()
                .processInstanceId(processInstanceId).singleResult();

        if (nextTask != null) {
            System.out.println("Ima sledeci task");
            System.out.println("Doslo do kraja1");
            TaskFormData tfd = formService.getTaskFormData(nextTask.getId());
            TaskDTO taskDTO = new TaskDTO(nextTask.getId(),nextTask.getProcessInstanceId(),nextTask.getName(),nextTask.getAssignee(),tfd.getFormFields());
            return new ResponseEntity(taskDTO, HttpStatus.OK);
        } else {
            System.out.println("Nema sledeci task");
            System.out.println("Doslo do kraja2");
            return new ResponseEntity(new TaskDTO(), HttpStatus.OK);

        }

    }

    @GetMapping
    public ResponseEntity getAllMyTasks(){

        String username = this.identityService.getCurrentAuthentication().getUserId();
        System.out.println(username);
        List<Task> tasks = taskService.createTaskQuery().taskAssignee(username).active().list();
        List<Group> groups = identityService.createGroupQuery().groupMember(username).list();
        for(Group g : groups){
            List<Task> tasks2 = taskService.createTaskQuery().taskCandidateGroup(g.getId()).active().list();
            for(Task t : tasks2){
                tasks.add(t);
            }
        }

        List<TaskDTO> tasksDTOs = new ArrayList<>();
        for(Task t : tasks){
            TaskFormData tfd = formService.getTaskFormData(t.getId());
            tasksDTOs.add(new TaskDTO(t.getId(),t.getProcessInstanceId(),t.getName(),t.getAssignee(),tfd.getFormFields()));
        }

        return new ResponseEntity(tasksDTOs, HttpStatus.OK);

    }

    @GetMapping(value = "/{taskId}")
    public ResponseEntity getTaskData(@PathVariable("taskId") String taskId){

        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        TaskFormData tfd = formService.getTaskFormData(taskId);
        TaskDTO taskDTO = new TaskDTO(task.getId(),task.getProcessInstanceId(),task.getName(),task.getAssignee(),tfd.getFormFields());

        return new ResponseEntity(taskDTO, HttpStatus.OK);

    }

    @GetMapping(value = "/process/{processId}")
    public ResponseEntity getActiveTaskForProcess(@PathVariable("processId") String processId){

        String username = identityService.getCurrentAuthentication().getUserId();
        Task task = taskService.createTaskQuery().taskAssignee(username).active()
                .processInstanceId(processId).singleResult();

        if (task != null) {
            TaskFormData tfd = formService.getTaskFormData(task.getId());
            TaskDTO taskDTO = new TaskDTO(task.getId(),task.getProcessInstanceId(),task.getName(),task.getAssignee(),tfd.getFormFields());
            return new ResponseEntity(taskDTO, HttpStatus.OK);
        } else {
            return ResponseEntity.ok().build();
        }
    }

    @GetMapping(value = "/taskId/{processId}")
    public ResponseEntity getActiveTaskIdForProcess(@PathVariable("processId") String processId) {

        String username = identityService.getCurrentAuthentication().getUserId();
        Task task = taskService.createTaskQuery().taskAssignee(username).active()
                .processInstanceId(processId).singleResult();

        if (task != null) {
            return new ResponseEntity(task.getId(), HttpStatus.OK);
        } else {
            return ResponseEntity.ok().build();
        }
    }

    @PostMapping(value = "/uploadFile")
    public ResponseEntity uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("processId") String processId, @RequestParam("counter") int counter){

        System.out.println("Uslo u upload");
        System.out.println(file.getOriginalFilename());
        System.out.println(processId);
        String filePath = "";
        try {
            filePath = uploadDownloadService.uploadFile(file, processId, counter);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return  new ResponseEntity<>("Failed to upload file", HttpStatus.EXPECTATION_FAILED);
        }
        return new ResponseEntity(filePath, HttpStatus.OK);
    }

    @GetMapping(value = "/downloadFile")
    public ResponseEntity downloadFile(@RequestParam("filePath") String filePath){

        try {
            Resource resource = this.uploadDownloadService.downloadFile(filePath);
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(value = "/get-assignees-task-id/{processId}")
    public ResponseEntity<?> getAssigneesTaskId(@PathVariable String processId) {
        System.out.println("Usao u get assignees task id");
        System.out.println(processId);

        //Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        //JwtBasedAuthentication jwtBasedAuthentication = (JwtBasedAuthentication) auth;
        //User user = (User) jwtBasedAuthentication.getPrincipal();
        // treba mi da ne mora biti ulogovan pisac
        String username = (String) runtimeService.getVariable(processId,"username");

        Task task = taskService.createTaskQuery().taskAssignee(username).active().processInstanceId(processId).singleResult();
        System.out.println(task.getName());
        System.out.println("Task Id: " + task.getId());
        return new ResponseEntity(task.getId(), HttpStatus.OK);

    }

    private Map<String, Object> listFieldsToMap(List<FormSubmissionDTO> formData) {
        Map<String, Object> retVal = new HashMap<>();
        for(FormSubmissionDTO dto: formData) {
            if(!(dto.getFieldValue() instanceof List)) { //zbog enuma
                retVal.put(dto.getFieldId(), dto.getFieldValue());
            }
        }
        return retVal;
    }
}
