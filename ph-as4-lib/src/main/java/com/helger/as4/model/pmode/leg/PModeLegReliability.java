/**
 * Copyright (C) 2015-2018 Philip Helger (www.helger.com)
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
package com.helger.as4.model.pmode.leg;

import java.io.Serializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotation.ReturnsMutableCopy;
import com.helger.commons.collection.impl.CommonsArrayList;
import com.helger.commons.collection.impl.ICommonsList;
import com.helger.commons.equals.EqualsHelper;
import com.helger.commons.hashcode.HashCodeGenerator;
import com.helger.commons.state.ETriState;

public class PModeLegReliability implements Serializable
{
  public static final boolean DEFAULT_TERMINATE_GROUP = false;
  public static final boolean DEFAULT_START_GROUP = false;
  public static final boolean DEFAULT_IN_ORDER_CONTACT = false;
  public static final boolean DEFAULT_AT_MOST_ONCE_CONTRACT = false;
  public static final boolean DEFAULT_AT_LEAST_ONCE_CONTRACT_ACK_RESPONSE = false;
  public static final boolean DEFAULT_AT_LEAST_ONCE_CONTRACT = false;
  public static final boolean DEFAULT_AT_LEAST_ONCE_ACK_ON_DELIVERY = false;

  /**
   * If "true", this Boolean parameter indicates that the "At-Least-Once"
   * reliability contract (see Section 8.2.2) is to be used between MSH and
   * Consumer (Guaranteed Delivery). It also indicates that this contract
   * applies to ebMS signals (see Section 8.2.1) (e.g. PullRequest) between the
   * receiving reliability module and the next MSH component (e.g. RM-Consumer).
   */
  private ETriState m_eAtLeastOnceContract = ETriState.UNDEFINED;

  /**
   * This Boolean parameter indicates the semantics of acknowledgments that are
   * generated by the reliability module. It is usually constrained by the
   * implementation and not set by users. For User messages: if "true", the
   * acknowledgment is only sent after the message has been delivered by the MSH
   * to the Consumer entity (see Case 2 in Section 8.2.4). If "false", the only
   * guarantee for the sender when receiving an acknowledgment is that the User
   * message has been well received (see Case 1 or 3 in Section 8.2.4), and made
   * available for further processing within the MSH. For Signal messages - e.g.
   * PullRequest: if "true", indicates that Signal messages are acknowledged
   * only if delivered (see Section 8.2.1) from the receiving reliability module
   * to the next MSH component (Case 3 in Section 8.2.4), i.e. to the
   * RM-Consumer (see 8.1.2). If "false", the message acknowledgment only
   * guarantees receipt of the signal (Case 1 in Section 8.2.4).
   */
  private ETriState m_eAtLeastOnceAckOnDelivery = ETriState.UNDEFINED;

  /**
   * This parameter is a URI that specifies where acknowledgments are to be
   * sent. It may contain an anonymous URI (defined in WS-Addressing). If
   * absent, acknowledgments are to be sent to the same URI associated with the
   * MSH sending messages reliably.
   */
  private String m_sAtLeastOnceContractAcksTo;

  /**
   * This Boolean is true when an Acknowledgment must be sent, for a response
   * that is sent reliably.
   */
  private ETriState m_eAtLeastOnceContractAckResponse = ETriState.UNDEFINED;

  /**
   * This parameter indicates whether a reliability acknowledgment is to be sent
   * as a callback, synchronously in the response (back-channel of underlying
   * protocol), or as response of separate ack pulling. Three values are
   * possible for this parameter, when using WS-Reliability: "Response",
   * "Callback", or "Poll".
   */
  private String m_sAtLeastOnceReplyPattern;

  /**
   * If "true", this Boolean parameter indicates that "At-Most-Once" (or
   * duplicate elimination) should be enforced when receiving a message. The
   * contract is for delivery between MSH and Consumer for User messages (see
   * Section 8.2.2), and between reliability module and next MSH component for
   * Signal messages (see Section 8.2.1).
   */
  private ETriState m_eAtMostOnceContract = ETriState.UNDEFINED;

  /**
   * If "true", this Boolean parameter indicates that this message is part of an
   * ordered sequence. It only concerns User messages (delivery contract between
   * MSH and Consumer application, see Section 8.2.2).
   */
  private ETriState m_eInOrderContract = ETriState.UNDEFINED;

  /**
   * This parameter is a Boolean that may be used to indicate if messages
   * matching this P-Mode must be associated with a new reliability group or
   * sequence. For example, a particular Service and Action may have the
   * application semantics of initiating a new ordered sequence of messages.
   */
  private ETriState m_eStartGroup = ETriState.UNDEFINED;

  /**
   * This parameter tells how to correlate a message matching this P-Mode with
   * an existing reliability group or sequence. It is a comma-separated list of
   * XPath elements relative to the <code>eb:Messaging</code> header. Each one
   * of these XPaths identifies an element or attribute inside
   * <code>eb:UserMessage</code> or <code>eb:SignalMessage</code>, and may
   * include predicates. For example,
   * <code>"eb:UserMessage/eb:CollaborationInfo/eb:ConversationId,
   *  eb:UserMessage/eb:MessageProperties/eb:Property[@name=\"ProcessInstance\"]</code>
   * will correlate all messages that share the same <code>ConversationId</code>
   * and have the same value for the message property named
   * <code>ProcessInstance</code>. In case there is no ongoing group or sequence
   * associated with the values in Reliability.Correlation for a message under
   * this P-Mode, then a new group/sequence is started.
   */
  private ICommonsList <String> m_aCorrelation = new CommonsArrayList <> ();

  /**
   * This parameter is a Boolean value that may be used to indicate if messages
   * matching this P-Mode must cause the closure of the reliability group or
   * sequence with which they correlate.
   */
  private ETriState m_eTerminateGroup = ETriState.UNDEFINED;

  public PModeLegReliability (@Nonnull final ETriState eAtLeastOnceContract,
                              @Nonnull final ETriState eAtLeastOnceAckOnDelivery,
                              @Nullable final String sAtLeastOnceContractAcksTo,
                              @Nonnull final ETriState eAtLeastOnceContractAckResponse,
                              @Nullable final String sAtLeastOnceReplyPattern,
                              @Nonnull final ETriState eAtMostOnceContract,
                              @Nonnull final ETriState eInOrderContract,
                              @Nonnull final ETriState eStartGroup,
                              @Nullable final ICommonsList <String> aCorrelation,
                              @Nonnull final ETriState eTerminateGroup)
  {
    m_eAtLeastOnceContract = eAtLeastOnceContract;
    m_eAtLeastOnceAckOnDelivery = eAtLeastOnceAckOnDelivery;
    m_sAtLeastOnceContractAcksTo = sAtLeastOnceContractAcksTo;
    m_eAtLeastOnceContractAckResponse = eAtLeastOnceContractAckResponse;
    m_sAtLeastOnceReplyPattern = sAtLeastOnceReplyPattern;
    m_eAtMostOnceContract = eAtMostOnceContract;
    m_eInOrderContract = eInOrderContract;
    m_eStartGroup = eStartGroup;
    m_aCorrelation = aCorrelation;
    m_eTerminateGroup = eTerminateGroup;
  }

  public boolean isAtLeastOnceContractDefined ()
  {
    return m_eAtLeastOnceContract.isDefined ();
  }

  @Nonnull
  public boolean isAtLeastOnceContract ()
  {
    return m_eAtLeastOnceContract.getAsBooleanValue (DEFAULT_AT_LEAST_ONCE_CONTRACT);
  }

  public void setAtLeastOnceContract (final boolean eAtLeastOnceContract)
  {
    setInOrderContract (ETriState.valueOf (eAtLeastOnceContract));
  }

  public void setAtLeastOnceContract (final ETriState eAtLeastOnceContract)
  {
    ValueEnforcer.notNull (eAtLeastOnceContract, "AtLeastOnceContract");
    m_eAtLeastOnceContract = eAtLeastOnceContract;
  }

  public boolean isAtLeastOnceAckOnDeliveryDefined ()
  {
    return m_eAtLeastOnceAckOnDelivery.isDefined ();
  }

  @Nonnull
  public boolean isAtLeastOnceAckOnDelivery ()
  {
    return m_eAtLeastOnceAckOnDelivery.getAsBooleanValue (DEFAULT_AT_LEAST_ONCE_ACK_ON_DELIVERY);
  }

  public void setAtLeastOnceAckOnDelivery (final boolean eAtLeastOnceAckOnDelivery)
  {
    setInOrderContract (ETriState.valueOf (eAtLeastOnceAckOnDelivery));
  }

  public void setAtLeastOnceAckOnDelivery (final ETriState eAtLeastOnceAckOnDelivery)
  {
    ValueEnforcer.notNull (eAtLeastOnceAckOnDelivery, "AtLeastOnceAckOnDelivery");
    m_eAtLeastOnceAckOnDelivery = eAtLeastOnceAckOnDelivery;
  }

  @Nullable
  public String getAtLeastOnceContractAcksTo ()
  {
    return m_sAtLeastOnceContractAcksTo;
  }

  public void getAtLeastOnceContractAcksTo (final String sAtLeastOnceContractAcksTo)
  {
    m_sAtLeastOnceContractAcksTo = sAtLeastOnceContractAcksTo;
  }

  public boolean isAtLeastOnceContractAckResponseDefined ()
  {
    return m_eAtLeastOnceContractAckResponse.isDefined ();
  }

  @Nonnull
  public boolean isAtLeastOnceContractAckResponse ()
  {
    return m_eAtLeastOnceContractAckResponse.getAsBooleanValue (DEFAULT_AT_LEAST_ONCE_CONTRACT_ACK_RESPONSE);
  }

  public void setAtLeastOnceContractAckResponse (final boolean eAtLeastOnceContractAckResponse)
  {
    setInOrderContract (ETriState.valueOf (eAtLeastOnceContractAckResponse));
  }

  public void setAtLeastOnceContractAckResponse (final ETriState eAtLeastOnceContractAckResponse)
  {
    ValueEnforcer.notNull (eAtLeastOnceContractAckResponse, "AtLeastOnceContractAckResponse");
    m_eAtLeastOnceContractAckResponse = eAtLeastOnceContractAckResponse;
  }

  @Nullable
  public String getAtLeastOnceReplyPattern ()
  {
    return m_sAtLeastOnceReplyPattern;
  }

  public void setAtLeastOnceReplyPattern (final String eAtLeastOnceReplyPattern)
  {
    m_sAtLeastOnceReplyPattern = eAtLeastOnceReplyPattern;
  }

  @Nonnull
  public boolean isAtMostOnceContractDefined ()
  {
    return m_eAtMostOnceContract.isDefined ();
  }

  public boolean isAtMostOnceContract ()
  {
    return m_eAtMostOnceContract.getAsBooleanValue (DEFAULT_AT_MOST_ONCE_CONTRACT);
  }

  public void setAtMostOnceContract (final boolean eAtMostOnceContract)
  {
    setInOrderContract (ETriState.valueOf (eAtMostOnceContract));
  }

  public void setAtMostOnceContract (final ETriState eAtMostOnceContract)
  {
    ValueEnforcer.notNull (eAtMostOnceContract, "AtMostOnceContract");
    m_eAtMostOnceContract = eAtMostOnceContract;
  }

  public boolean isInOrderContractDefined ()
  {
    return m_eInOrderContract.isDefined ();
  }

  @Nonnull
  public boolean isInOrderContract ()
  {
    return m_eInOrderContract.getAsBooleanValue (DEFAULT_IN_ORDER_CONTACT);
  }

  public void setInOrderContract (final boolean eInOrderContract)
  {
    setInOrderContract (ETriState.valueOf (eInOrderContract));
  }

  public void setInOrderContract (final ETriState eInOrderContract)
  {
    ValueEnforcer.notNull (eInOrderContract, "InOrderContract");
    m_eInOrderContract = eInOrderContract;
  }

  public boolean isStartGroupDefined ()
  {
    return m_eStartGroup.isDefined ();
  }

  @Nonnull
  public boolean isStartGroup ()
  {
    return m_eStartGroup.getAsBooleanValue (DEFAULT_START_GROUP);
  }

  public void setStartGroup (final boolean eStartGroup)
  {
    setStartGroup (ETriState.valueOf (eStartGroup));
  }

  public void setStartGroup (final ETriState eStartGroup)
  {
    ValueEnforcer.notNull (eStartGroup, "StartGroup");
    m_eStartGroup = eStartGroup;
  }

  @Nullable
  @ReturnsMutableCopy
  public ICommonsList <String> getCorrelation ()
  {
    return m_aCorrelation.getClone ();
  }

  public void setCorrelation (final ICommonsList <String> aCorrelation)
  {
    m_aCorrelation = aCorrelation;
  }

  public boolean isTerminateGroupDefined ()
  {
    return m_eTerminateGroup.isDefined ();
  }

  @Nonnull
  public boolean isTerminateGroup ()
  {
    return m_eTerminateGroup.getAsBooleanValue (DEFAULT_TERMINATE_GROUP);
  }

  public void setTerminateGroup (final boolean eTerminateGroup)
  {
    setTerminateGroup (ETriState.valueOf (eTerminateGroup));
  }

  public void setTerminateGroup (final ETriState eTerminateGroup)
  {
    ValueEnforcer.notNull (eTerminateGroup, "TerminateGroup");
    m_eTerminateGroup = eTerminateGroup;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (o == null || !getClass ().equals (o.getClass ()))
      return false;
    final PModeLegReliability rhs = (PModeLegReliability) o;
    return m_aCorrelation.equals (rhs.m_aCorrelation) &&
           EqualsHelper.equals (m_eAtLeastOnceAckOnDelivery, rhs.m_eAtLeastOnceAckOnDelivery) &&
           EqualsHelper.equals (m_eAtLeastOnceContract, rhs.m_eAtLeastOnceContract) &&
           EqualsHelper.equals (m_eAtLeastOnceContractAckResponse, rhs.m_eAtLeastOnceContractAckResponse) &&
           EqualsHelper.equals (m_sAtLeastOnceReplyPattern, rhs.m_sAtLeastOnceReplyPattern) &&
           EqualsHelper.equals (m_eAtMostOnceContract, rhs.m_eAtMostOnceContract) &&
           EqualsHelper.equals (m_eInOrderContract, rhs.m_eInOrderContract) &&
           EqualsHelper.equals (m_eStartGroup, rhs.m_eStartGroup) &&
           EqualsHelper.equals (m_eTerminateGroup, rhs.m_eTerminateGroup) &&
           EqualsHelper.equals (m_sAtLeastOnceContractAcksTo, rhs.m_sAtLeastOnceContractAcksTo);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_aCorrelation)
                                       .append (m_eAtLeastOnceAckOnDelivery)
                                       .append (m_eAtLeastOnceContract)
                                       .append (m_eAtLeastOnceContractAckResponse)
                                       .append (m_sAtLeastOnceReplyPattern)
                                       .append (m_eAtMostOnceContract)
                                       .append (m_eInOrderContract)
                                       .append (m_eStartGroup)
                                       .append (m_eTerminateGroup)
                                       .append (m_sAtLeastOnceContractAcksTo)
                                       .getHashCode ();
  }
}
