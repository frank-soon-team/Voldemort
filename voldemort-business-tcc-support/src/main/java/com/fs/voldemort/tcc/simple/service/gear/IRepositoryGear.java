package com.fs.voldemort.tcc.simple.service.gear;

import com.fs.voldemort.tcc.simple.service.model.TCCTaskModel;

public interface IRepositoryGear {

    boolean create(TCCTaskModel taskModel);

    boolean update(TCCTaskModel taskModel);
    
}
