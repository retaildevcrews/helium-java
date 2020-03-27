package com.microsoft.cse.helium.app.services.keyvault;

public interface IEnvironmentReader {
  String getAuthType();
  void setAuthType(String authType);
  String getKeyVaultName();
  public void setKeyVaultName(String keyVaultName);
}
