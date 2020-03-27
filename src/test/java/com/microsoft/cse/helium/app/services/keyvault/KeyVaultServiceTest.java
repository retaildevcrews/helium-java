// package com.microsoft.cse.helium.app.services.keyvault;

// import org.junit.AfterClass;
// import org.junit.BeforeClass;
// import org.junit.Test;


// import static org.junit.Assert.*;

// public class KeyVaultServiceTest {

//   KeyVaultService keyVaultServiceTest;

//   @BeforeClass
//   public static void testSetup() {

//   }

//   @AfterClass
//   public static void testCleanup() {
//     // Do your cleanup here like close URL connection , releasing resources etc
//   }

//   //Constructor:EnvironmentFlag tests
//   @Test(expected = Exception.class)
//   public void TestBadEnvironmentFlag() throws Exception {
//     keyVaultServiceTest = new KeyVaultService("One2-3", "BadValue");
//   }

//   @Test(expected = Exception.class)
//   public void TestEmptyEnvironmentFlag() throws Exception {
//     keyVaultServiceTest = new KeyVaultService("One2-3", "");
//   }

//   @Test(expected = Exception.class)
//   public void TestNullEnvironmentFlag() throws Exception {
//     keyVaultServiceTest = new KeyVaultService("One2-3", null);
//   }

//   @Test
//   public void TestGoodEnvironmentFlag() throws Exception {
//     keyVaultServiceTest = null;
//     keyVaultServiceTest = new KeyVaultService("One2-thR33", "CLI");
//     assertNotNull(keyVaultServiceTest);

//     keyVaultServiceTest = null;
//     keyVaultServiceTest = new KeyVaultService("One2-thR33", "MSI");
//     assertNotNull(keyVaultServiceTest);
//   }
//   // END Constructor:EnvironmentFlag tests

//   //Constructor:KeyVaultName tests
//   @Test(expected = Exception.class)
//   public void TestShortKeyVaultName() throws Exception {
//     keyVaultServiceTest = new KeyVaultService("kv", "MSI");
//   }

//   @Test(expected = Exception.class)
//   public void TestLongKeyVaultName() throws Exception {
//     keyVaultServiceTest = new KeyVaultService("one234five6789ten1112thirteen", "MSI");
//   }

//   @Test(expected = Exception.class)
//   public void TestEmptyKeyVaultName() throws Exception {
//     keyVaultServiceTest = new KeyVaultService("", "MSI");
//   }

//   @Test(expected = Exception.class)
//   public void TestNullKeyVaultName() throws Exception {
//     keyVaultServiceTest = new KeyVaultService(null, "MSI");
//   }

//   @Test(expected = Exception.class)
//   public void TestTrailingHyphenKeyVaultName() throws Exception {
//     keyVaultServiceTest = new KeyVaultService("one2-3-", "MSI");
//   }

//   @Test(expected = Exception.class)
//   public void TestConsecutiveHyphenKeyVaultName() throws Exception {
//     keyVaultServiceTest = new KeyVaultService("one2--3", "MSI");
//   }

//   @Test(expected = Exception.class)
//   public void TestLeadingNumberKeyVaultName() throws Exception {
//     keyVaultServiceTest = new KeyVaultService("1one2--3", "MSI");
//   }

//   @Test(expected = Exception.class)
//   public void TestLeadingHyphenKeyVaultName() throws Exception {
//     keyVaultServiceTest = new KeyVaultService("1one2--3", "MSI");
//   }

//   @Test
//   public void TestGoodKeyVaultName() throws Exception {
//     keyVaultServiceTest = null;
//     keyVaultServiceTest = new KeyVaultService("One2-thR33", "CLI");
//     assertNotNull(keyVaultServiceTest);

//     keyVaultServiceTest = null;
//     keyVaultServiceTest = new KeyVaultService("One2-thR33", "MSI");
//     assertNotNull(keyVaultServiceTest);
//   }
// }
