package com.microsoft.cse.helium.app.services.configuration;

import java.util.*;

import lombok.AccessLevel;
import lombok.Getter;

public interface IConfigurationService {

    public Map<String, String> getConfigEntries();
}
