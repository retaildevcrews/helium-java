package com.microsoft.cse.helium.app.services.keyvault;

public interface IEnvironmentReader {
  String getAuthType();

  String getKeyVaultName();
}
