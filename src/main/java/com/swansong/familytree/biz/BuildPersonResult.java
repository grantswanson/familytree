package com.swansong.familytree.biz;

import com.swansong.familytree.model.Person;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BuildPersonResult {
    private Person person;
    private boolean isNew;

}
