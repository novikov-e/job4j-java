package base.cycle;

import org.junit.Test;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
/**
 * Class FactorialTest tests methods from class Factorial.
 *
 * @author Egor Novikov
 * E-mail: enovikovdev@gmail.com
 * @version 1$
 * @since 0.1
 */
public class FactorialTest {

  @Test
  public void factorial0() {
    Factorial test = new Factorial();
    int result = test.factorial(0);
    assertThat(result, is(1));
  }

  @Test
  public void factorial5() {
    Factorial test = new Factorial();
    int result = test.factorial(5);
    assertThat(result, is(120));
  }
}
