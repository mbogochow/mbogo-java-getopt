package mbogo.getopt;

import java.util.LinkedList;
import java.util.List;

import sun.misc.FpUtils;

/**
 * Implementation of getopt for Java. This implementation loosely follows the
 * POSIX specification and GNU extensions but with modifications to better
 * follow Java conventions.
 * <p>
 * Currently does not support getopt_long.
 * <p>
 * Instances of this class are <b>not</b> thread-safe.
 * 
 * @author Mike Bogochow
 * @version 1.0.0, Feb 9, 2015
 */
public class Getopt
{
  private final String[] argv;
  private final String optstring;

  /**
   * Array interpretation of <code>optstring</code> for easier lookup of option
   * characters.
   */
  private final char[] optstrings;

  /**
   * The index of the next element to be processed in <code>argv</code>.
   */
  /*
   * Note that default value of optind is 1 in POSIX specification but it must
   * be 0 here since Java does not pass the name of the program as the first
   * value of argv.
   */
  private int optind = 0;

  private int nextchar = 0;

  private String optarg = null;

  private char optopt = 0;

  private int opterr = 0;

  private GetoptScanningMode scanMode = GetoptScanningMode.PERMUTE;

  private final boolean sepMissingArg;

  private boolean processOptions = true;

  /**
   * List of nonoption arguments for permute scan mode.
   */
  private List<String> nonoptions;

  public Getopt(final String[] argv, final String optstring)
  {
    this.argv = argv;
    this.optstring = optstring;

    this.optstrings = this.optstring.toCharArray();

    /*
     * Check optstrings for special starting characters
     */
    if (this.optstrings[0] == '+')
    {
      scanMode = GetoptScanningMode.REQUIRE_ORDER;
      nextchar += 1;
    }
    else if (this.optstrings[0] == '-')
    {
      scanMode = GetoptScanningMode.RETURN_IN_ORDER;
      nextchar += 1;
    }
    else
      nonoptions = new LinkedList<>(); // Permute so init list

    if (this.optstrings[nextchar] == ':')
    {
      sepMissingArg = true;
      nextchar += 1;
    }
    else
      sepMissingArg = false;
  } /* Getopt constructor */

  /*
   * TODO: {Could add pre-processing to find the last option argument according
   * to the scanning mode to make nonoption processing smarter.}
   */
  public int getopt()
  {
    int option = -1;
    boolean nonopt = false;
    int i;
    String str;
    String arg;

    optarg = null;

    while (optind < argv.length)
    {
      arg = argv[optind];
      str = arg.substring(nextchar);

      if (nonopt = (str.length() == 1))
        arg = str;

      if (nonopt |= (!processOptions || arg.charAt(0) != '-'))
      { // str is a nonoption
        if (scanMode == GetoptScanningMode.PERMUTE)
        {
          nonoptions.add(arg);
          option = -1;
          optind += 1;
          continue;
        }
        else if (scanMode == GetoptScanningMode.REQUIRE_ORDER)
        {
          option = -1;
          optarg = arg;
          optind += 1;
          processOptions = false;
          break;
        }
        else if (scanMode == GetoptScanningMode.RETURN_IN_ORDER)
        {
          option = 1;
          optarg = arg;
          optind += 1;
          break;
        }
      }

      if (str.charAt(0) == '-')
      {
        /*
         * We know that str is at least 2 chars long here because of nonopt
         * checking
         */
        str = str.substring(1);
        nextchar += 2;
      }
      else
        nextchar += 1;

      option = str.charAt(0);

      if (option == '-')
      {
        /*
         * "--" special argument. option processing ends, all remaining
         * arguments treated as nonoptions
         */
        processOptions = false;
        continue;
      }

      i = optstring.indexOf(option);
      if (i != -1)
      {
        int next = i + 1;
        if (next < optstrings.length && optstrings[next] == ':')
        { // option takes an argument
          boolean last = str.length() == 1;
          if (last) // end of argv element, get next as optarg
            optarg = argv[++optind];
          else
            optarg = str.substring(1);

          if (optarg.charAt(0) == '-')
          { // illegal option (no argument)
            next += 1;
            if (next < optstrings.length && optstrings[next] == ':')
            { // argument is optional
              optarg = null;
            }
            else
            {
              if (opterr != 0)
              {
                System.err.printf(
                    "illegal option -- %c, no argument provided\n", option);
              }

              if (sepMissingArg)
                option = ':';
              else
                option = '?';
            }

            if (last)
              nextchar = 0;
          }
          /*
           * Remainder of argv element is optarg, move to next option for the
           * next call.
           */
          else
          {
            optind += 1;
            nextchar = 0;
          }

          break;
        }

        if (nextchar >= arg.length())
        {
          optind += 1;
          nextchar = 0;
        }

        break;
      }
      else
      { // illegal option
        if (opterr != 0)
          System.err.printf("illegal option -- %c\n", option);
        option = '?';
        break;
      }
    } /* while */

    if (optind >= argv.length && scanMode == GetoptScanningMode.PERMUTE
        && option == -1)
    {
      optarg = nonoptions.get(0);
      nonoptions.remove(0);
    }

    return option;
  } /* Getopt.getopt */

  /**
   * Get the <code>optind</code> index value. This is the index of the next
   * element to be processed in <code>argv</code>.
   */
  public int getOptind()
  {
    return optind;
  } /* Getopt.getOptind */

  /**
   * Get the <code>optarg</code> value. This is a pointer to an option's
   * included text argument for options which have one (i.e. options followed by
   * a colon in <code>optstring</code>). This value is set after one such option
   * is read during a call to <code>getopt</code>. The <code>optarg</code> value
   * is initialized to <code>null</code> and is set to <code>null</code> if an
   * option is read which does not have an associated parameter in
   * <code>getopt</code>.
   */
  public String getOptarg()
  {
    return optarg;
  } /* Getopt.getOptarg */

  /**
   * Get the <code>optopt</code> value. This value is set in <code>getopt</code>
   * if an option character is encountered which was not included in
   * <code>optstring</code> or if it detects a missing argument. The value of
   * <code>optopt</code> will be set to the actual option character.
   */
  public char getOptopt()
  {
    return optopt;
  } /* Getopt.getOptopt */

  /**
   * Get the <code>opterr</code> value.
   */
  public int getOpterr()
  {
    return opterr;
  } /* Getopt.getOpterr */

  /**
   * Sets the <code>opterr</code> value. Set <code>opterr</code> to 0 to prevent
   * error messages from being written to stderr (this is the default behavior)
   * or any other value if messages should be written to stderr.
   * 
   * @param opterr
   *          the value to set opterr to
   */
  public void setOpterr(final int opterr)
  {
    this.opterr = opterr;
  } /* Getopt.setOpterr */

  /**
   * Reset the value of <code>optind</code> to 0. This allows for rescanning of
   * the same <code>argv</code> array.
   */
  public void reset()
  {
    optind = 1;
    processOptions = true;
  } /* Getopt.reset */
} /* Getopt class end */
