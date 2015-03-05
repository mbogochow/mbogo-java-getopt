package mbogo.getopt;

import org.testng.annotations.Test;
import org.testng.Assert;

public class GetoptTest extends Assert
{
  Getopt getopt;

  /* @formatter:off */
  /*
  @Test
  public void testTemplate()
  {
    final String optstring = "";
    final String[] argv = {"", ""};
    
    getopt = new Getopt(argv, optstring);
    
    int c;
    
    while ((c = getopt.getopt()) != -1)
    {
      switch (c)
      {
      case 'a':
        break;
      case ':':
        break;
      case '?':
        break;
      default:
        fail("Default case reached in getopt loop");
      }
    }
  }
  */
  /* @formatter:on */

  @Test
  public void basicTest()
  {
    final String optstring = "abcd";
    final String[] argv = { "-a", "-b", "-cd" };

    boolean aarg, barg, carg, darg;
    aarg = barg = carg = darg = false;

    getopt = new Getopt(argv, optstring);

    int c;

    while ((c = getopt.getopt()) != -1)
    {
      switch (c)
      {
      case 'a':
        aarg = true;
        break;
      case 'b':
        barg = true;
        break;
      case 'c':
        carg = true;
        break;
      case 'd':
        darg = true;
        break;
      case '?':
        fail("Got '?' from getopt for invalid switch '" + getopt.getOptopt()
            + "'");
      default:
        fail("Default case reached in getopt loop: '" + c + "'");
      }
    }

    assertTrue(aarg, "'a' parameter given but not processed");
    assertTrue(barg, "'b' parameter given but not processed");
    assertTrue(carg, "'c' parameter given but not processed");
    assertTrue(darg, "'d' parameter given but not processed");
  }

  @Test
  public void argTest()
  {
    final String optstring = ":a:b:c:d";
    final String[] argv = { "-a", "aarg", "-bbarg", "-dccarg" };

    boolean darg = false;
    String aarg = null;
    String barg = null;
    String carg = null;

    getopt = new Getopt(argv, optstring);

    int c;

    while ((c = getopt.getopt()) != -1)
    {
      switch (c)
      {
      case 'a':
        aarg = getopt.getOptarg();
        break;
      case 'b':
        barg = getopt.getOptarg();
        break;
      case 'c':
        carg = getopt.getOptarg();
        break;
      case 'd':
        darg = true;
        break;
      case ':':
        fail("Got ':' from getopt for invalid switch '" + getopt.getOptopt()
            + "'");
        break;
      case '?':
        fail("Got '?' from getopt for invalid switch '" + getopt.getOptopt()
            + "'");
        break;
      default:
        fail("Default case reached in getopt loop");
      }
    }

    assertTrue(darg, "'d' parameter given but not processed");
    assertTrue(aarg != null, "'a' parameter given but not processed");
    assertTrue(aarg.equals("aarg"),
        "incorrect argument processed for 'a' parameter: " + aarg);
    assertTrue(barg != null, "'b' parameter given but not processed");
    assertTrue(barg.equals("barg"),
        "incorrect argument processed for 'b' parameter: " + barg);
    assertTrue(carg != null, "'c' parameter given but not processed");
    assertTrue(carg.equals("carg"),
        "incorrect argument processed for 'c' parameter: " + carg);
  }
}
