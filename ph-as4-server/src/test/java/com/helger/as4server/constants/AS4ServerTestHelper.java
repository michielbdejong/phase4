package com.helger.as4server.constants;

import com.helger.as4lib.ebms3header.Ebms3Property;
import com.helger.commons.collection.ext.CommonsArrayList;
import com.helger.commons.collection.ext.ICommonsList;

/**
 * This class helps to keep the test clean from strings that can be changed.
 * Most of the test use the predefined settings here.
 *
 * @author bayerlma
 */
public class AS4ServerTestHelper
{

  public static String DEFAULT_PARTY_ID = "APP_1000000101";
  public static String DEFAULT_INITIATOR_ID = "http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/sender";
  public static String DEFAULT_RESPONDER_ID = "http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/responder";
  public static String DEFAULT_AGREEMENT = "http://agreements.holodeckb2b.org/examples/agreement0";

  public static ICommonsList <Ebms3Property> getEBMSProperties ()
  {
    final ICommonsList <Ebms3Property> aEbms3Properties = new CommonsArrayList<> ();
    final Ebms3Property aOriginalSender = new Ebms3Property ();
    aOriginalSender.setName ("originalSender");
    aOriginalSender.setValue ("C1-test");
    final Ebms3Property aFinalRecipient = new Ebms3Property ();
    aFinalRecipient.setName ("finalRecipient");
    aFinalRecipient.setValue ("C4-test");
    aEbms3Properties.add (aOriginalSender);
    aEbms3Properties.add (aFinalRecipient);

    return aEbms3Properties;
  }

}