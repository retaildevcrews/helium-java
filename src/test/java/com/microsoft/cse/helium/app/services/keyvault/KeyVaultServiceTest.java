import com.microsoft.cse.helium.app.services.keyvault.KeyVaultService;

import static org.junit.Assert.assertEquals;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

// import org.junit.jupiter.api.Test;
// import org.springframework.boot.test.context.SpringBootTest;

// import org.junit.runner.RunWith;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.junit.MockitoJUnitRunner;
// import org.springframework.test.context.junit4.SpringRunner;
// import org.springframework.boot.test.context.SpringBootTest;

//@SpringBootTest
//@RunWith(SpringRunner.class)
public class KeyVaultServiceTest {

    KeyVaultService keyVaultServiceTest;
  
    @BeforeClass
    public static void testSetup() {
        
    }
  
    @AfterClass
    public static void testCleanup() {
      // Do your cleanup here like close URL connection , releasing resources etc
    }
  
    @Test(expected = IllegalArgumentException.class)
    public void TestBadEnvironmentFlag() {        
      keyVaultServiceTest = new KeyVaultService("1two-3", "BadValue");
    }
  
    @Test
    public void TestGoodEnvironmentFlag() {
        keyVaultServiceTest = null;
        keyVaultServiceTest = new KeyVaultService("1two-3", "CLI");
        assertNotNull(keyVaultServiceTest);

        keyVaultServiceTest = null;
        keyVaultServiceTest = new KeyVaultService("1two-3", "MSI");
        assertNotNull(keyVaultServiceTest);
    }
  } 
