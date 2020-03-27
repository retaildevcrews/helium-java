package com.microsoft.cse.helium.app.services.keyvault;

import java.util.Map;


public interface IEnvironmentReader {

  String getAuthType();
  String getKeyVaultName();
}
