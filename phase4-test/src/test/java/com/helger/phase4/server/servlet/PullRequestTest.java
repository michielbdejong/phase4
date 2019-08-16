/**
 * Copyright (C) 2015-2019 Philip Helger (www.helger.com)
 * philip[at]helger[dot]com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.helger.phase4.server.servlet;

import static org.junit.Assert.assertTrue;

import org.apache.http.HttpEntity;
import org.junit.Test;
import org.w3c.dom.Document;

import com.helger.commons.collection.impl.CommonsArrayList;
import com.helger.commons.collection.impl.ICommonsList;
import com.helger.commons.io.resource.ClassPathResource;
import com.helger.phase4.AS4TestConstants;
import com.helger.phase4.crypto.AS4SigningParams;
import com.helger.phase4.error.EEbmsError;
import com.helger.phase4.http.HttpXMLEntity;
import com.helger.phase4.messaging.crypto.AS4Signer;
import com.helger.phase4.messaging.domain.AS4PullRequestMessage;
import com.helger.phase4.messaging.domain.MessageHelperMethods;
import com.helger.phase4.mgr.MetaAS4Manager;
import com.helger.phase4.model.mpc.MPC;
import com.helger.phase4.server.spi.MockMessageProcessorSPI;
import com.helger.phase4.soap.ESOAPVersion;
import com.helger.phase4.util.AS4ResourceHelper;
import com.helger.xml.serialize.read.DOMReader;

public final class PullRequestTest extends AbstractUserMessageTestSetUpExt
{
  private final ESOAPVersion m_eSOAPVersion = ESOAPVersion.AS4_DEFAULT;

  @Test
  public void sendPullRequestSuccess () throws Exception
  {
    final AS4PullRequestMessage aPullReqMsg = AS4PullRequestMessage.create (m_eSOAPVersion,
                                                                            MessageHelperMethods.createEbms3MessageInfo (),
                                                                            AS4TestConstants.DEFAULT_MPC,
                                                                            null);
    Document aDoc = aPullReqMsg.getAsSOAPDocument ();

    final boolean bMustUnderstand = true;
    aDoc = AS4Signer.createSignedMessage (m_aCryptoFactory,
                                          aDoc,
                                          m_eSOAPVersion,
                                          aPullReqMsg.getMessagingID (),
                                          null,
                                          new AS4ResourceHelper (),
                                          bMustUnderstand,
                                          AS4SigningParams.createDefault ());

    final HttpEntity aEntity = new HttpXMLEntity (aDoc, m_eSOAPVersion);
    final String sResponse = sendPlainMessage (aEntity, true, null);

    assertTrue (sResponse.contains (AS4TestConstants.USERMESSAGE_ASSERTCHECK));
  }

  @Test
  public void sendPullRequestEmpty () throws Exception
  {
    // Special MPC name handled in MockMessageProcessorSPI
    final String sFailure = MockMessageProcessorSPI.MPC_EMPTY;
    final MPC aMPC = new MPC (sFailure);
    if (MetaAS4Manager.getMPCMgr ().getMPCOfID (sFailure) == null)
      MetaAS4Manager.getMPCMgr ().createMPC (aMPC);

    final Document aDoc = AS4PullRequestMessage.create (m_eSOAPVersion,
                                                        MessageHelperMethods.createEbms3MessageInfo (),
                                                        sFailure,
                                                        null)
                                               .getAsSOAPDocument ();

    final HttpEntity aEntity = new HttpXMLEntity (aDoc, m_eSOAPVersion);
    sendPlainMessage (aEntity, false, EEbmsError.EBMS_EMPTY_MESSAGE_PARTITION_CHANNEL.getErrorCode ());
  }

  @Test
  public void sendPullRequestFailure () throws Exception
  {
    // Special MPC name handled in MockMessageProcessorSPI
    final String sFailure = MockMessageProcessorSPI.MPC_FAILURE;
    final MPC aMPC = new MPC (sFailure);
    if (MetaAS4Manager.getMPCMgr ().getMPCOfID (sFailure) == null)
      MetaAS4Manager.getMPCMgr ().createMPC (aMPC);

    final Document aDoc = AS4PullRequestMessage.create (m_eSOAPVersion,
                                                        MessageHelperMethods.createEbms3MessageInfo (),
                                                        sFailure,
                                                        null)
                                               .getAsSOAPDocument ();
    final HttpEntity aEntity = new HttpXMLEntity (aDoc, m_eSOAPVersion);
    sendPlainMessage (aEntity, false, EEbmsError.EBMS_OTHER.getErrorCode ());
  }

  @Test
  public void sendPullRequestTwoSPIsFailure () throws Exception
  {
    final String sMPC = "TWO-SPI";
    final MPC aMPC = new MPC (sMPC);
    if (MetaAS4Manager.getMPCMgr ().getMPCOfID (sMPC) == null)
      MetaAS4Manager.getMPCMgr ().createMPC (aMPC);

    final Document aDoc = AS4PullRequestMessage.create (m_eSOAPVersion,
                                                        MessageHelperMethods.createEbms3MessageInfo (),
                                                        sMPC,
                                                        null)
                                               .getAsSOAPDocument ();
    final HttpEntity aEntity = new HttpXMLEntity (aDoc, m_eSOAPVersion);
    sendPlainMessage (aEntity, false, EEbmsError.EBMS_VALUE_INCONSISTENT.getErrorCode ());
  }

  @Test
  public void sendPullRequestSuccessTwoWayPushPull () throws Exception
  {
    // Depending on the payload a different EMEPBinding get chosen by
    // @MockPullRequestProcessorSPI
    // To Test the pull request part of the EMEPBinding
    final Document aPayload = DOMReader.readXMLDOM (new ClassPathResource ("testfiles/PushPull.xml"));
    final ICommonsList <Object> aAny = new CommonsArrayList <> ();
    aAny.add (aPayload.getDocumentElement ());

    final Document aDoc = AS4PullRequestMessage.create (m_eSOAPVersion,
                                                        MessageHelperMethods.createEbms3MessageInfo (),
                                                        AS4TestConstants.DEFAULT_MPC,
                                                        aAny)
                                               .getAsSOAPDocument ();
    final HttpEntity aEntity = new HttpXMLEntity (aDoc, m_eSOAPVersion);
    final String sResponse = sendPlainMessage (aEntity, true, null);

    assertTrue (sResponse.contains (AS4TestConstants.USERMESSAGE_ASSERTCHECK));
  }

  @Test
  public void sendPullRequestSuccessTwoWayPullPush () throws Exception
  {
    // Depending on the payload a different EMEPBinding get chosen by
    // @MockPullRequestProcessorSPI
    // To Test the pull request part of the EMEPBinding
    final Document aPayload = DOMReader.readXMLDOM (new ClassPathResource ("testfiles/PullPush.xml"));
    final ICommonsList <Object> aAny = new CommonsArrayList <> ();
    aAny.add (aPayload.getDocumentElement ());

    final Document aDoc = AS4PullRequestMessage.create (m_eSOAPVersion,
                                                        MessageHelperMethods.createEbms3MessageInfo (),
                                                        AS4TestConstants.DEFAULT_MPC,
                                                        aAny)
                                               .getAsSOAPDocument ();
    final HttpEntity aEntity = new HttpXMLEntity (aDoc, m_eSOAPVersion);
    final String sResponse = sendPlainMessage (aEntity, true, null);

    assertTrue (sResponse.contains (AS4TestConstants.USERMESSAGE_ASSERTCHECK));
  }

  @Test
  public void sendPullRequestWithNoMPC () throws Exception
  {
    final Document aDoc = AS4PullRequestMessage.create (m_eSOAPVersion,
                                                        MessageHelperMethods.createEbms3MessageInfo (),
                                                        null,
                                                        null)
                                               .getAsSOAPDocument ();
    final HttpEntity aEntity = new HttpXMLEntity (aDoc, m_eSOAPVersion);
    sendPlainMessage (aEntity, false, EEbmsError.EBMS_VALUE_NOT_RECOGNIZED.getErrorCode ());
  }
}