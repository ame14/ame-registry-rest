package com.ame.rest.extension.instance;

import java.util.Map;

import com.ame.rest.exceptions.UnauthorizedAccessAttempt;
import com.ame.rest.exceptions.UnexpectedUserType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(path = "/instance")
@PreAuthorize("isAuthenticated()")
public class InstanceController {

    @Autowired
    private InstanceService service;

    @GetMapping(value = "/")
    @PreAuthorize("hasRole('WRITER')")
    @ResponseBody
    public Iterable<InstanceDTO> getInstances() throws Exception {
        return service.getInstances();
    }

    @GetMapping(value = "get/instance")
    @PreAuthorize("hasRole('WRITER')")
    @ResponseBody
    public InstanceDTO getInstance(Long id) throws Exception {
        return service.getInstance(id);
    }

    @PreAuthorize("hasRole('WRITER')")
    @PostMapping(value = "/create/copy")
    @ResponseBody
    public InstanceDTO createCopy(@RequestBody Map<String,String> request) throws Exception {
        return service.createCopy(request);
    }

    @PreAuthorize("hasRole('WRITER')")
    @PostMapping(value = "/delete/copy")
    @ResponseBody
    public InstanceDTO deleteCopy(@RequestParam Long id) throws Exception {
         return service.deleteCopy(id);
    }

    @PostMapping(value = "/create")
    @PreAuthorize("hasRole('WRITER')")
    @ResponseBody
    public ResponseEntity<String> createInstance(@RequestParam Long extension) {
        try {
            service.CreateInstance(extension);
        } catch (UnexpectedUserType e) {
            return new ResponseEntity<String>(e.getMessage() ,HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<String>("Something went wrong : (", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<String>("Instance created", HttpStatus.OK);
    }

    @PostMapping(value = "/update")
    @PreAuthorize("hasRole('WRITER')")
    @ResponseBody
    public  ResponseEntity<String> updateDetails(@RequestBody Map<String, String> changes) throws Exception{
        try {
            service.updateDetails(changes);
        } catch (UnauthorizedAccessAttempt e) {
            return new ResponseEntity<String>(e.getMessage() ,HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<String>("Something went wrong : (", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<String>("Instance updated", HttpStatus.OK);
    }

    @GetMapping(value = "/run/{id}")
    @PreAuthorize("permitAll()")
    @ResponseBody
    public String run(@PathVariable Long id) throws Exception{
        return service.runInstance(id);
    }


    @PostMapping(value = "/data/get", consumes = "application/json", produces = "text/plain")
    @PreAuthorize("permitAll()")
    @ResponseBody
    public String getData(@RequestBody Map<String,String> request) throws Exception{
        return service.getData(request.get("key"));
    }

    @PostMapping(value = "/data/set")
    @PreAuthorize("permitAll()")
    @ResponseBody
    public String setData(@RequestBody Map<String,String> request) throws Exception{
        return service.setData(request);
    }

    @PostMapping(value = "/state/set")
    @PreAuthorize("hasRole('WRITER')")
    @ResponseBody
    public void updateState(@RequestParam Long id, @RequestParam String state) throws Exception{
        service.updateState(id,state);
    }
}
