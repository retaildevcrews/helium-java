package com.microsoft.cse.helium.app.controllers;

import java.util.Optional;

public interface IController {

  Object getAll(
      Optional<String> query,
      Optional<String> pageNumber,
      Optional<String> pageSize,
      Enum entity);

}

