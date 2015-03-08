package mbogo.getopt;

import java.util.LinkedList;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

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

  @Test(priority = -1)
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
    final String optstring = "a:b:c:d";
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

  @Test
  public void noArgsTest()
  {
    final String optstring = "a:b:c:d";
    final String[] argv = {};

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
      case '?':
        fail("Got '?' from getopt for invalid switch '" + getopt.getOptopt()
            + "'");
        break;
      default:
        fail("Default case reached in getopt loop");
      }
    }

    assertFalse(getopt.getOptind() < argv.length,
        "Incorrect optind value after getopt processing");

    assertTrue(aarg == null, "'a' parameter not given but processed");
    assertTrue(barg == null, "'b' parameter not given but processed");
    assertTrue(carg == null, "'c' parameter not given but processed");
    assertFalse(darg, "'d' parameter not given but processed");
  }

  @Test
  public void invalidOptTest1()
  {
    final String optstring = "a:b:c:d";
    final String[] argv = { "-o", "-a", "aarg" };

    boolean darg = false;
    String aarg = null;
    String barg = null;
    String carg = null;

    int qCount = 0;

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
        qCount += 1;
        break;
      default:
        fail("Default case reached in getopt loop");
      }
    }

    assertTrue(aarg != null, "'a' parameter given but not processed");
    assertTrue(aarg.equals("aarg"),
        "incorrect argument processed for 'a' parameter: " + aarg);
    assertTrue(barg == null, "'b' parameter not given but processed");
    assertTrue(carg == null, "'c' parameter not given but processed");
    assertFalse(darg, "'d' parameter not given but processed");
    assertTrue(qCount == 1, "Invalid parameters given but not processed");
  }

  @Test(dependsOnMethods = "invalidOptTest1")
  public void invalidOptTest2()
  {
    final String optstring = "a:b:c:d";
    final String[] argv = { "-odpa", "aarg" };

    boolean darg = false;
    String aarg = null;
    String barg = null;
    String carg = null;

    int qCount = 0;

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
        qCount += 1;
        break;
      default:
        fail("Default case reached in getopt loop");
      }
    }

    assertTrue(aarg != null, "'a' parameter given but not processed");
    assertTrue(aarg.equals("aarg"),
        "incorrect argument processed for 'a' parameter: " + aarg);
    assertTrue(barg == null, "'b' parameter not given but processed");
    assertTrue(carg == null, "'c' parameter not given but processed");
    assertTrue(darg, "'d' parameter given but not processed");
    assertTrue(qCount == 2, "Invalid parameters given but not processed");
  }

  @Test(dependsOnMethods = "invalidOptTest2")
  public void invalidOptTest3()
  {
    final String optstring = ":a:b:c:d";
    final String[] argv = { "-odpa", "aarg", "-c" };

    boolean darg = false;
    String aarg = null;
    String barg = null;
    String carg = null;

    int qCount = 0;
    int cCount = 0;

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
        cCount += 1;
        break;
      case '?':
        qCount += 1;
        break;
      default:
        fail("Default case reached in getopt loop");
      }
    }

    assertTrue(aarg != null, "'a' parameter given but not processed");
    assertTrue(aarg.equals("aarg"),
        "incorrect argument processed for 'a' parameter: " + aarg);
    assertTrue(barg == null, "'b' parameter not given but processed");
    assertTrue(carg == null, "'c' parameter not given but processed");
    assertTrue(darg, "'d' parameter given but not processed");
    assertTrue(qCount == 2, "Invalid parameters given (2) but not processed ("
        + qCount + ")");
    assertTrue(cCount == 1, "No arg given to c option but not processed ("
        + cCount + ")");
  }

  @Test
  public void optionalArgTest1()
  {
    final String optstring = ":ab:c::d";
    final String[] argv = { "-adb", "barg", "-c", "carg" };

    boolean aarg = false;
    String barg = null;
    String carg = null;
    boolean darg = false;

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

    assertTrue(aarg, "'a' parameter given but not processed");
    assertTrue(barg != null, "'b' parameter given but not processed");
    assertTrue(barg.equals("barg"),
        "incorrect argument processed for 'b' parameter: " + barg);
    assertTrue(carg != null, "'c' parameter given but not processed");
    assertTrue(carg.equals("carg"),
        "incorrect argument processed for 'c' parameter: " + carg);
    assertTrue(darg, "'d' parameter given but not processed");
  }

  @Test(dependsOnMethods = "optionalArgTest1")
  public void optionalArgTest2()
  {
    final String optstring = ":ab:c::d";
    final String[] argv = { "-adb", "barg", "-c" };

    boolean aarg = false;
    String barg = null;
    String carg = null;
    boolean cargGiven = false;
    boolean darg = false;

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
        barg = getopt.getOptarg();
        break;
      case 'c':
        carg = getopt.getOptarg();
        cargGiven = true;
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

    assertTrue(aarg, "'a' parameter given but not processed");
    assertTrue(barg != null, "'b' parameter given but not processed");
    assertTrue(barg.equals("barg"),
        "incorrect argument processed for 'b' parameter: " + barg);
    assertTrue(cargGiven, "'c' parameter given but not processed");
    assertTrue(carg == null,
        "'c' parameter given without arg but arg processed");
    assertTrue(darg, "'d' parameter given but not processed");
  }

  @Test(
      description = "Nonoption testing with permute scanning mode, no options")
  public void nonoptTestPermute1()
  {
    final String optstring = "a:b:c:d";
    final String[] argv = { "nonopt1", "nonopt2" };

    boolean darg = false;
    String aarg = null;
    String barg = null;
    String carg = null;

    final List<String> nonoptions = new LinkedList<>();

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

    assertTrue(
        getopt.getOptind() == 0,
        "Optind set to wrong value after getopt processing: "
            + getopt.getOptind());

    for (int i = getopt.getOptind(); i < argv.length; i++)
      nonoptions.add(argv[i]);

    assertTrue(aarg == null, "'a' parameter not given but processed");
    assertTrue(barg == null, "'b' parameter not given but processed");
    assertTrue(carg == null, "'c' parameter not given but processed");
    assertFalse(darg, "'d' parameter not given but processed");

    assertTrue(nonoptions.size() == 2,
        "Incorrect number of nonoptions processed: " + nonoptions.size());
    assertTrue(nonoptions.get(0).equals("nonopt1"),
        "First nonoption has incorrect value: " + nonoptions.get(0));
    assertTrue(nonoptions.get(1).equals("nonopt2"),
        "Second nonoption has incorrect value: " + nonoptions.get(1));
  }

  @Test(description = "Nonoption testing with permute scanning mode, with "
      + "options", dependsOnMethods = "nonoptTestPermute1")
  public void nonoptTestPermute2()
  {
    final String optstring = "a:b:c:d";
    final String[] argv =
        { "-a", "aarg", "nonopt1", "-bbarg", "-dccarg", "nonopt2" };

    boolean darg = false;
    String aarg = null;
    String barg = null;
    String carg = null;

    final List<String> nonoptions = new LinkedList<>();

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

    assertTrue(
        getopt.getOptind() == 4,
        "Optind set to wrong value after getopt processing: "
            + getopt.getOptind());

    for (int i = getopt.getOptind(); i < argv.length; i++)
      nonoptions.add(argv[i]);

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

    assertTrue(nonoptions.size() == 2,
        "Incorrect number of nonoptions processed: " + nonoptions.size());
    assertTrue(nonoptions.get(0).equals("nonopt1"),
        "First nonoption has incorrect value: " + nonoptions.get(0));
    assertTrue(nonoptions.get(1).equals("nonopt2"),
        "Second nonoption has incorrect value: " + nonoptions.get(1));
  }

  @Test
  public void nonoptTestRequireOrder1()
  {
    final String optstring = "+a:b:c:d";
    final String[] argv = { "nonopt1", "nonopt2" };

    boolean darg = false;
    String aarg = null;
    String barg = null;
    String carg = null;

    final List<String> nonoptions = new LinkedList<>();

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

    assertTrue(
        getopt.getOptind() == 0,
        "Optind set to wrong value after getopt processing: "
            + getopt.getOptind());

    for (int i = getopt.getOptind(); i < argv.length; i++)
      nonoptions.add(argv[i]);

    assertTrue(aarg == null, "'a' parameter not given but processed");
    assertTrue(barg == null, "'b' parameter not given but processed");
    assertTrue(carg == null, "'c' parameter not given but processed");
    assertFalse(darg, "'d' parameter not given but processed");

    assertTrue(nonoptions.size() == 2,
        "Incorrect number of nonoptions processed: " + nonoptions.size());
    assertTrue(nonoptions.get(0).equals("nonopt1"),
        "First nonoption has incorrect value: " + nonoptions.get(0));
    assertTrue(nonoptions.get(1).equals("nonopt2"),
        "Second nonoption has incorrect value: " + nonoptions.get(1));
  }

  @Test(dependsOnMethods = "nonoptTestRequireOrder1")
  public void nonoptTestRequireOrder2()
  {
    final String optstring = "+a:b:c:d";
    final String[] argv =
        { "-a", "aarg", "nonopt1", "-bbarg", "-dccarg", "nonopt2" };

    boolean darg = false;
    String aarg = null;
    String barg = null;
    String carg = null;

    final List<String> nonoptions = new LinkedList<>();

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

    assertTrue(
        getopt.getOptind() == 2,
        "Optind set to wrong value after getopt processing: "
            + getopt.getOptind());

    for (int i = getopt.getOptind(); i < argv.length; i++)
      nonoptions.add(argv[i]);

    assertTrue(aarg != null, "'a' parameter given but not processed");
    assertTrue(aarg.equals("aarg"),
        "incorrect argument processed for 'a' parameter: " + aarg);
    assertTrue(barg == null, "'b' parameter not given but processed");
    assertTrue(carg == null, "'c' parameter not given but processed");
    assertFalse(darg, "'d' parameter not given but processed");

    assertTrue(nonoptions.size() == 4,
        "Incorrect number of nonoptions processed: " + nonoptions.size());
    assertTrue(nonoptions.get(0).equals("nonopt1"),
        "First nonoption has incorrect value: " + nonoptions.get(0));
    assertTrue(nonoptions.get(1).equals("-bbarg"),
        "First nonoption has incorrect value: " + nonoptions.get(1));
    assertTrue(nonoptions.get(2).equals("-dccarg"),
        "First nonoption has incorrect value: " + nonoptions.get(2));
    assertTrue(nonoptions.get(3).equals("nonopt2"),
        "Second nonoption has incorrect value: " + nonoptions.get(3));
  }

  @Test
  public void nonoptTestReturnInOrder1()
  {
    final String optstring = "-a:b:c:d";
    final String[] argv = { "nonopt1", "nonopt2" };

    boolean darg = false;
    String aarg = null;
    String barg = null;
    String carg = null;

    final List<String> nonoptions = new LinkedList<>();

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
      case 1:
        nonoptions.add(getopt.getOptarg());
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

    assertFalse(
        getopt.getOptind() < argv.length,
        "Optind set to wrong value after getopt processing: "
            + getopt.getOptind());

    for (int i = getopt.getOptind(); i < argv.length; i++)
      fail("Should be no additional nonoption processing");

    assertTrue(aarg == null, "'a' parameter not given but processed");
    assertTrue(barg == null, "'b' parameter not given but processed");
    assertTrue(carg == null, "'c' parameter not given but processed");
    assertFalse(darg, "'d' parameter not given but processed");

    assertTrue(nonoptions.size() == 2,
        "Incorrect number of nonoptions processed: " + nonoptions.size());
    assertTrue(nonoptions.get(0).equals("nonopt1"),
        "First nonoption has incorrect value: " + nonoptions.get(0));
    assertTrue(nonoptions.get(1).equals("nonopt2"),
        "Second nonoption has incorrect value: " + nonoptions.get(1));
  }

  @Test(dependsOnMethods = "nonoptTestReturnInOrder1")
  public void nonoptTestReturnInOrder2()
  {
    final String optstring = "-a:b:c:d";
    final String[] argv =
        { "-a", "aarg", "nonopt1", "-bbarg", "-dccarg", "nonopt2" };

    boolean darg = false;
    String aarg = null;
    String barg = null;
    String carg = null;

    final List<String> nonoptions = new LinkedList<>();

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
      case 1:
        nonoptions.add(getopt.getOptarg());
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

    assertFalse(
        getopt.getOptind() < argv.length,
        "Optind set to wrong value after getopt processing: "
            + getopt.getOptind());

    for (int i = getopt.getOptind(); i < argv.length; i++)
      fail("Should be no additional nonoption processing");

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

    assertTrue(nonoptions.size() == 2,
        "Incorrect number of nonoptions processed: " + nonoptions.size());
    assertTrue(nonoptions.get(0).equals("nonopt1"),
        "First nonoption has incorrect value: " + nonoptions.get(0));
    assertTrue(nonoptions.get(1).equals("nonopt2"),
        "Second nonoption has incorrect value: " + nonoptions.get(1));
  }

  @Test(description = "Nonoptions with only a single character")
  public void nonOptSingleCharsTest()
  {
    final String optstring = "a:b:c:d";
    final String[] argv = { "-a", "aarg", "-", "-bbarg", "a", "-dccarg" };

    boolean darg = false;
    String aarg = null;
    String barg = null;
    String carg = null;

    final List<String> nonoptions = new LinkedList<>();

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
      case '?':
        fail("Got '?' from getopt for invalid switch '" + getopt.getOptopt()
            + "'");
        break;
      default:
        fail("Default case reached in getopt loop");
      }
    }

    assertTrue(
        getopt.getOptind() == 4,
        "Optind set to wrong value after getopt processing: "
            + getopt.getOptind());

    for (int i = getopt.getOptind(); i < argv.length; i++)
      nonoptions.add(argv[i]);

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

    assertTrue(nonoptions.size() == 2,
        "Incorrect number of nonoptions processed: " + nonoptions.size());
    assertTrue(nonoptions.get(0).equals("-"),
        "First nonoption has incorrect value: " + nonoptions.get(0));
    assertTrue(nonoptions.get(1).equals("a"),
        "Second nonoption has incorrect value: " + nonoptions.get(1));
  }

  @Test(description = "Test for the special option '--', permute mode")
  public void specialOptPermuteTest()
  {
    final String optstring = "a:b:c:d";
    final String[] argv = { "-a", "aarg", "--", "-bbarg", "-dccarg" };

    boolean darg = false;
    String aarg = null;
    String barg = null;
    String carg = null;

    final List<String> nonoptions = new LinkedList<>();

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
      case '?':
        fail("Got '?' from getopt for invalid switch '" + getopt.getOptopt()
            + "'");
        break;
      default:
        fail("Default case reached in getopt loop");
      }
    }

    assertTrue(
        getopt.getOptind() == 3,
        "Optind set to wrong value after getopt processing: "
            + getopt.getOptind());

    for (int i = getopt.getOptind(); i < argv.length; i++)
      nonoptions.add(argv[i]);

    assertTrue(aarg != null, "'a' parameter given but not processed");
    assertTrue(aarg.equals("aarg"),
        "incorrect argument processed for 'a' parameter: " + aarg);
    assertTrue(barg == null, "'b' parameter not given but processed");
    assertTrue(carg == null, "'c' parameter not given but processed");
    assertFalse(darg, "'d' parameter not given but processed");

    assertTrue(nonoptions.size() == 2,
        "Incorrect number of nonoptions processed: " + nonoptions.size());
    assertTrue(nonoptions.get(0).equals("-bbarg"),
        "First nonoption has incorrect value: " + nonoptions.get(0));
    assertTrue(nonoptions.get(1).equals("-dccarg"),
        "Second nonoption has incorrect value: " + nonoptions.get(1));
  }

  @Test(description = "Test for the special option '--', require order mode")
  public void specialOptRequireOrderTest()
  {
    final String optstring = "+a:b:c:d";
    final String[] argv = { "-a", "aarg", "--", "-bbarg", "-dccarg" };

    boolean darg = false;
    String aarg = null;
    String barg = null;
    String carg = null;

    final List<String> nonoptions = new LinkedList<>();

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
      case '?':
        fail("Got '?' from getopt for invalid switch '" + getopt.getOptopt()
            + "'");
        break;
      default:
        fail("Default case reached in getopt loop");
      }
    }

    assertTrue(
        getopt.getOptind() == 3,
        "Optind set to wrong value after getopt processing: "
            + getopt.getOptind());

    for (int i = getopt.getOptind(); i < argv.length; i++)
      nonoptions.add(argv[i]);

    assertTrue(aarg != null, "'a' parameter given but not processed");
    assertTrue(aarg.equals("aarg"),
        "incorrect argument processed for 'a' parameter: " + aarg);
    assertTrue(barg == null, "'b' parameter not given but processed");
    assertTrue(carg == null, "'c' parameter not given but processed");
    assertFalse(darg, "'d' parameter not given but processed");

    assertTrue(nonoptions.size() == 2,
        "Incorrect number of nonoptions processed: " + nonoptions.size());
    assertTrue(nonoptions.get(0).equals("-bbarg"),
        "First nonoption has incorrect value: " + nonoptions.get(0));
    assertTrue(nonoptions.get(1).equals("-dccarg"),
        "Second nonoption has incorrect value: " + nonoptions.get(1));
  }

  @Test(description = "Test for the special option '--', return in order mode",
      enabled = false)
  public void specialOptReturnInOrderTest()
  {
    final String optstring = "-a:b:c:d";
    final String[] argv = { "-a", "aarg", "--", "-bbarg", "-dccarg" };

    boolean darg = false;
    String aarg = null;
    String barg = null;
    String carg = null;

    final List<String> nonoptions = new LinkedList<>();

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
      case 1:
        nonoptions.add(getopt.getOptarg());
        break;
      case '?':
        fail("Got '?' from getopt for invalid switch '" + getopt.getOptopt()
            + "'");
        break;
      default:
        fail("Default case reached in getopt loop");
      }
    }

    assertFalse(
        getopt.getOptind() < argv.length,
        "Optind set to wrong value after getopt processing: "
            + getopt.getOptind());

    for (int i = getopt.getOptind(); i < argv.length; i++)
      fail("Should be no further nonoption parsing");

    assertTrue(aarg != null, "'a' parameter given but not processed");
    assertTrue(aarg.equals("aarg"),
        "incorrect argument processed for 'a' parameter: " + aarg);
    assertTrue(barg == null, "'b' parameter not given but processed");
    assertTrue(carg == null, "'c' parameter not given but processed");
    assertFalse(darg, "'d' parameter not given but processed");

    assertTrue(nonoptions.size() == 2,
        "Incorrect number of nonoptions processed: " + nonoptions.size());
    assertTrue(nonoptions.get(0).equals("-bbarg"),
        "First nonoption has incorrect value: " + nonoptions.get(0));
    assertTrue(nonoptions.get(1).equals("-dccarg"),
        "Second nonoption has incorrect value: " + nonoptions.get(1));
  }
}
