package com.lmrj.cim.recipe.service;

import java.util.List;

public interface IRecipeService {

    List<String> selectRecipeList(String eqpId) throws Exception;
}
