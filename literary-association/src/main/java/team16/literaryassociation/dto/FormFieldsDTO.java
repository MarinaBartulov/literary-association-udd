package team16.literaryassociation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.camunda.bpm.engine.form.FormField;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FormFieldsDTO {

    //private String taskId;
    private List<FormField> formFields;
    //private String processInstanceId;

}
