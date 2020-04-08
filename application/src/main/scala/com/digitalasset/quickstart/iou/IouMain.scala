// Copyright (c) 2020 The DAML Authors. All rights reserved.
// SPDX-License-Identifier: Apache-2.0

package com.digitalasset.quickstart.iou

import akka.actor.ActorSystem
import akka.stream.Materializer
import akka.stream.scaladsl.Sink
import com.digitalasset.api.util.TimeProvider
import com.digitalasset.grpc.adapter.AkkaExecutionSequencerPool
import com.digitalasset.ledger.api.refinements.ApiTypes.{ApplicationId, WorkflowId}
import com.digitalasset.ledger.api.v1.ledger_offset.LedgerOffset
import com.digitalasset.ledger.client.LedgerClient
import com.digitalasset.ledger.client.binding.Contract
import com.digitalasset.ledger.client.configuration.{CommandClientConfiguration, LedgerClientConfiguration, LedgerIdRequirement}
import com.digitalasset.ledger.client.services.commands.CompletionStreamElement
import com.digitalasset.quickstart.iou.ClientUtil.workflowIdFromParty
import com.digitalasset.quickstart.iou.DecodeUtil.decodeCreated
import com.google.protobuf.ByteString
import com.google.rpc.Code
import com.typesafe.scalalogging.StrictLogging
import scalaz.Tag

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.util.{Failure, Success}

// <doc-ref:imports>
import com.digitalasset.ledger.client.binding.{Primitive => P}
import com.digitalasset.quickstart.iou.model.{Iou => M}
// </doc-ref:imports>

object IouMain extends App with StrictLogging {

  if (args.length != 3) {
    logger.error("Usage: LEDGER_HOST LEDGER_PORT NCOMMANDS")
    System.exit(-1)
  }

  private val ledgerHost = args(0)
  private val ledgerPort = args(1).toInt
  private val ncommands = args(2).toInt

  // <doc-ref:issuer-definition>
  private val issuerHint = P.Party("Alice")
  // </doc-ref:issuer-definition>
  // <doc-ref:new-owner-definition>
  private val newOwner = P.Party("Bob")
  // </doc-ref:new-owner-definition>

  private val asys = ActorSystem()
  private val amat = Materializer(asys)
  private val aesf = new AkkaExecutionSequencerPool("clientPool")(asys)

  private def shutdown(): Unit = {
    logger.info("Shutting down...")
    Await.result(asys.terminate(), 10.seconds)
    ()
  }

  private implicit val ec: ExecutionContext = asys.dispatcher
  private val applicationId = ApplicationId("IOU Example")

  private val timeProvider = TimeProvider.UTC

  // <doc-ref:ledger-client-configuration>
  private val clientConfig = LedgerClientConfiguration(
    applicationId = ApplicationId.unwrap(applicationId),
    ledgerIdRequirement = LedgerIdRequirement("", enabled = false),
    commandClient = CommandClientConfiguration.default,
    sslContext = None,
    token = None
  )
  // </doc-ref:ledger-client-configuration>

  private val clientF: Future[LedgerClient] =
    LedgerClient.singleHost(ledgerHost, ledgerPort, clientConfig)(ec, aesf)

  private val clientUtilF: Future[ClientUtil] =
    clientF.map(client => new ClientUtil(client, applicationId, 30.seconds, timeProvider))

  private val offset0F: Future[LedgerOffset] = clientUtilF.flatMap(_.ledgerEnd)

  private val issuerWorkflowId: WorkflowId = workflowIdFromParty(issuerHint)
  private val newOwnerWorkflowId: WorkflowId = workflowIdFromParty(newOwner)

  val newOwnerAcceptsAllTransfers: Future[Unit] = for {
    clientUtil <- clientUtilF
    offset0 <- offset0F
    // <doc-ref:subscribe-and-decode-iou-transfer>
    _ <- clientUtil.subscribe(newOwner, offset0, None) { tx =>
      logger.info(s"$newOwner received transaction: $tx")
      decodeCreated[M.IouTransfer](tx).foreach { contract: Contract[M.IouTransfer] =>
        logger.info(s"$newOwner received contract: $contract")
        // </doc-ref:subscribe-and-decode-iou-transfer>
        // <doc-ref:submit-iou-transfer-accept-exercise-command>
        val exerciseCmd = contract.contractId.exerciseIouTransfer_Accept(actor = newOwner)
        clientUtil.submitCommand(newOwner, newOwnerWorkflowId, exerciseCmd) onComplete {
          case Success(_) =>
            logger.info(s"$newOwner sent exercise command: $exerciseCmd")
            logger.info(s"$newOwner accepted IOU Transfer: $contract")
          case Failure(e) =>
            logger.error(s"$newOwner failed to send exercise command: $exerciseCmd", e)
        }
      // </doc-ref:submit-iou-transfer-accept-exercise-command>
      }
    }(amat)
  } yield ()


  // <doc-ref:iou-contract-instance>
  val iou = M.Iou(
    issuer = issuerHint,
    owner = issuerHint,
    currency = "USD",
    amount = BigDecimal("1000.00"),
    observers = List())
  // </doc-ref:iou-contract-instance>

  val iouBenchFlow: Future[Unit] = for {
    clientUtil <- clientUtilF
    client <- clientF
    offset0 <- offset0F
    _ = println(s"Client API initialization completed, Ledger ID: ${clientUtil.toString}")

    _ = println("Uploading package...")
    darFileInputStream = this.getClass.getClassLoader.getResourceAsStream("quickstart-0.0.1.dar")
    _ <- client.packageManagementClient.uploadDarFile(
      ByteString.readFrom(darFileInputStream)
    ).recover {
      case err => sys.error(s"Package upload failed: $err")
    }
    _ = darFileInputStream.close

    _ = println(s"Allocating party with hint '$issuerHint'")
    issuer <- client.partyManagementClient.allocateParty(Some(Tag.unwrap(issuerHint)), Some(Tag.unwrap(issuerHint)))
        .map { result =>
          println(s"Allocated party '${result.party}'")
          result.party
        }
        .recover {
          case _ =>
            println("Party already existed, reusing.")
            Tag.unwrap(issuerHint)
        }

    // <doc-ref:submit-iou-create-command>
    createCmd = iou.create

    t0 = System.nanoTime

    _ = print(s"Submitting $ncommands Iou creations: ")
    _ <- Future.sequence(
      (1 to ncommands).map { _ =>
        clientUtil.submitCommand(P.Party(issuer), issuerWorkflowId, createCmd)
          .map { _ =>
            print(".")
          }
          .recover {
            case _ => print("X")
          }
        }
    )
    _ = println(s"\nWaiting for $ncommands completions: ")

    _ <-
      client.commandClient.completionSource(Seq(issuer), offset0)
        .collect {
          case x: CompletionStreamElement.CompletionElement =>
            if (x.completion.getStatus.code == Code.OK.getNumber)
              print(".")
            else
              print("X")
            x
        }
        .take(ncommands)
        .runWith(Sink.ignore)(amat)

    _ = println("")
    t1 = System.nanoTime
  } yield {
    val millis = (t1 - t0) / 1000 / 1000

    println(s"completed $ncommands in ${millis}ms")

  }

  val returnCodeF: Future[Int] = iouBenchFlow.transform {
    case Success(_) =>
      logger.info("IOU flow completed.")
      Success(0)
    case Failure(e) =>
      logger.error("IOU flow completed with an error", e)
      Success(1)
  }

  val returnCode: Int = Await.result(returnCodeF, 300.seconds)
  shutdown()
  System.exit(returnCode)
}
