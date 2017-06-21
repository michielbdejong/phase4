package com.helger.as4.http;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.commons.datetime.PDTFactory;

/**
 * Turn on/off AS4 HTTP debug logging
 *
 * @author Philip Helger
 */
@ThreadSafe
public final class AS4HttpDebug
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (AS4HttpDebug.class);
  private static final AtomicBoolean s_aEnabled = new AtomicBoolean (false);

  private AS4HttpDebug ()
  {}

  /**
   * Enable or disable
   *
   * @param bEnabled
   *        <code>true</code> to enabled, <code>false</code> to disable
   */
  public static void setEnabled (final boolean bEnabled)
  {
    s_aEnabled.set (bEnabled);
  }

  /**
   * @return <code>true</code> if enabled, <code>false</code> if not.
   */
  public static boolean isEnabled ()
  {
    return s_aEnabled.get ();
  }

  /**
   * Debug the provided string if {@link #isEnabled()}
   *
   * @param aMsg
   *        The message supplier. May not be <code>null</code>. Invoked only if
   *        {@link #isEnabled()}
   */
  public static void debug (@Nonnull final Supplier <? super String> aMsg)
  {
    if (isEnabled ())
      s_aLogger.info ("$$$ AS4 HTTP [" + PDTFactory.getCurrentLocalTime ().toString () + "] " + aMsg.get ());
  }
}
