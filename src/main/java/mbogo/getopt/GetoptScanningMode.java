package mbogo.getopt;

/**
 * The different scanning modes for <code>getopt</code>.
 * 
 * @author Mike Bogochow
 * @version 1.0.0, Feb 9, 2015
 */
public enum GetoptScanningMode
{
  /**
   * Nonoptions are permuted so that they are all at the end of
   * <code>argv</code> (i.e. after all options).
   */
  PERMUTE,
  /**
   * Processing of options is ceased after the first encounter of a nonoption.
   */
  REQUIRE_ORDER,
  /**
   * All nonoptions are treated as though they are the arguments of an option
   * with character code 1.
   */
  RETURN_IN_ORDER
} /* GetoptScanningMode enum end */
