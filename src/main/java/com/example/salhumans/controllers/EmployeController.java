package com.example.salhumans.controllers;

import com.example.salhumans.models.Employe;
import com.example.salhumans.repositories.EmployeRepository;
import com.example.salhumans.services.EmployeService;
import jakarta.validation.Valid;
import org.eclipse.tags.shaded.org.apache.xpath.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
public class EmployeController {
    @Autowired
    EmployeService employeService;

    @RequestMapping("/createEmploye")
    public String createEmploye(){
        return "CreateEmploye";
    }
    @RequestMapping("saveEmploye")
    public String saveEmploye(@Valid Employe employe, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) return "CreateEmploye";
        employeService.saveEmployee(employe);

        return "CreateEmploye";
    }
    @RequestMapping("/employeList")
    public String employeList(ModelMap modelMap,
                                @RequestParam(name = "page",defaultValue = "0") int page,
                                @RequestParam(name = "size",defaultValue = "10") int size
                                ){
        Page<Employe> employesController = employeService.getAllEmployesByPage(page, size);
        modelMap.addAttribute("employesJsp",employesController);
        modelMap.addAttribute("pages",new int[employesController.getTotalPages()]);
        modelMap.addAttribute("currentPage",page);
        return "EmployeList";
    }
    @RequestMapping("/deleteEmploye")
    public String deleteEmploye(@RequestParam("id") Long id,ModelMap modelMap,
                                @RequestParam(name = "page",defaultValue = "0") int page,
                                @RequestParam(name = "size",defaultValue = "10") int size
    ){
        employeService.deleteEmployeById(id);
        Page<Employe> employesController = employeService.getAllEmployesByPage(page, size);
        modelMap.addAttribute("employesJsp",employesController);
        modelMap.addAttribute("pages",new int[employesController.getTotalPages()]);
        modelMap.addAttribute("currentPage",page);
        return "EmployeList";
    }

    @RequestMapping("/editEmploye")
    public String editEmploye(@RequestParam("id") long id, ModelMap modelMap) {
        Employe employeController = employeService.getEmployeById(id);
        modelMap.addAttribute("employeView", employeController);
        return "EditEmploye";
    }
    
    @RequestMapping("updateEmploye")
    public String updateEmploye(
            @ModelAttribute("employe") Employe employe,
            @RequestParam("dateJsp") String dateController,
            ModelMap modelMap
    ) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date dateEmbauche = dateFormat.parse(dateController);
        employe.setDate_embauche(dateEmbauche);
        Employe updatedEmploye = employeService.updateEmploye(employe);
        String messageController = "The employee with ID: " + updatedEmploye.getEmployeId() + " has been updated";
        modelMap.addAttribute("messageJsp", messageController);
        return "EditEmploye";
    }
}
