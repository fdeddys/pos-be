package com.ddabadi.pos.master.CategoryBarang;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@RestController
@EnableAutoConfiguration
@RequestMapping(value = "categoryBarang",
                produces = "application/json")
public class CategoryBarangController {

    private Logger logger = Logger.getLogger(CategoryBarangController.class);

    @Autowired private CategoryBarangRepository repository;

    @RequestMapping(value = "{page}/{size}",
                    method = RequestMethod.GET)
    public Page<CategoryBarang> getAll(@PathVariable int page, @PathVariable int size){

        Sort sort= new Sort(Sort.Direction.ASC,"id").and(new Sort(Sort.Direction.DESC,"keterangan"));
        PageRequest pageRequest = new PageRequest(page,size, sort);
        return repository.findAll(pageRequest) ;
    }

    @RequestMapping(value = "{id}",
                    method = RequestMethod.GET)
    public CategoryBarang getById(@PathVariable Long id) {
        CategoryBarang categoryBarang= repository.findOne(id);
        if (categoryBarang==null){
            return new CategoryBarang();
        }else{
            return categoryBarang;
        }
    }

}
