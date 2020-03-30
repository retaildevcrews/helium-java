package com.microsoft.cse.helium.app.services.keyvault;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.EnvironmentVariables;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.Assert.*;

public class KeyVaultServiceTest {

  @Rule
  public final ExpectedSystemExit exit = ExpectedSystemExit.none();

  @Rule
  public final EnvironmentVariables environmentVariables = new EnvironmentVariables();

  @MockBean
  private ApplicationArguments applicationArguments;



  @BeforeClass
  public static void testSetup() {

  }

  @AfterClass
  public static void testCleanup() {
    // Do your cleanup here like close URL connection , releasing resources etc
  }


  @Test
  public void TestBadAuthType() throws Exception {
    EnvironmentReader environmentReader = new EnvironmentReader(applicationArguments);
    exit.expectSystemExitWithStatus(-1);
    environmentVariables.set("AUTH_TYPE", "BadValue");
    environmentReader.getAuthType();

  }

  @Test
  public void TestEmptyAuthType() throws Exception {
    EnvironmentReader environmentReader = new EnvironmentReader(applicationArguments);
    environmentVariables.clear("AUTH_TYPE");
    assertTrue("No Auth specified, MSI used", environmentReader.getAuthType() == "MSI");
  }

  @Test
  public void TestPositiveMSI() throws Exception {
    EnvironmentReader environmentReader = new EnvironmentReader(applicationArguments);
    environmentVariables.set("AUTH_TYPE", "MSI");
    assertTrue("MSI specified, MSI used", environmentReader.getAuthType() == "MSI");
  }

  @Test
  public void TestPositiveCLI() throws Exception {
    EnvironmentReader environmentReader = new EnvironmentReader(applicationArguments);
    environmentVariables.set("AUTH_TYPE", "CLI");
    assertTrue("CLI specified, CLI used", environmentReader.getAuthType() == "CLI");
  }

  @Test
  public void TestGoodEnvironmentFlag() throws Exception {
    EnvironmentReader environmentReader = new EnvironmentReader(applicationArguments);
    environmentVariables.set("KeyVaultName", "One2-thR33");
    assertTrue("Key vault name is good", environmentReader.getKeyVaultName().equals("One2-thR33"));
  }

  //KeyVaultName tests
  @Test
  public void TestShortKeyVaultName() throws Exception {
    EnvironmentReader environmentReader = new EnvironmentReader(applicationArguments);
    exit.expectSystemExitWithStatus(-1);
    environmentVariables.set("KeyVaultName", "kv");
    environmentReader.getKeyVaultName();
  }

  @Test
  public void TestLongKeyVaultName() throws Exception {
    EnvironmentReader environmentReader = new EnvironmentReader(applicationArguments);
    exit.expectSystemExitWithStatus(-1);
    environmentVariables.set("KeyVaultName", "one234five6789ten1112thirteen");
    environmentReader.getKeyVaultName();
  }

  @Test
  public void TestEmptyKeyVaultName() throws Exception {
    EnvironmentReader environmentReader = new EnvironmentReader(applicationArguments);
    exit.expectSystemExitWithStatus(-1);
    environmentVariables.set("KeyVaultName", "");
    environmentReader.getKeyVaultName();
  }

  @Test(expected = Exception.class)
  public void TestNullKeyVaultName() throws Exception {
    EnvironmentReader environmentReader = new EnvironmentReader(applicationArguments);
    exit.expectSystemExitWithStatus(-1);
    environmentVariables.set("KeyVaultName", null);
    environmentReader.getKeyVaultName();
  }

  @Test(expected = Exception.class)
  public void TestTrailingHyphenKeyVaultName() throws Exception {
    EnvironmentReader environmentReader = new EnvironmentReader(applicationArguments);
    exit.expectSystemExitWithStatus(-1);
    environmentVariables.set("KeyVaultName", "one2-3-");
    environmentReader.getKeyVaultName();
  }

  @Test
  public void TestConsecutiveHyphenKeyVaultName() throws Exception {
    EnvironmentReader environmentReader = new EnvironmentReader(applicationArguments);
    exit.expectSystemExitWithStatus(-1);
    environmentVariables.set("KeyVaultName", "one2--3");
    environmentReader.getKeyVaultName();
  }

  @Test
  public void TestLeadingNumberKeyVaultName() throws Exception {
    EnvironmentReader environmentReader = new EnvironmentReader(applicationArguments);
    exit.expectSystemExitWithStatus(-1);
    environmentVariables.set("KeyVaultName", "1one2--3");
    environmentReader.getKeyVaultName();
  }
}
