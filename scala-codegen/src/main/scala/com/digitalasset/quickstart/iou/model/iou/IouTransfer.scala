/*
 * THIS FILE WAS AUTOGENERATED BY THE DIGITAL ASSET DAML SCALA CODE GENERATOR
 * DO NOT EDIT BY HAND!
 */
import _root_.com.digitalasset.ledger.client.{binding=>$u0020lfdomainapi}
import _root_.com.digitalasset.ledger.api.v1.{value=>$u0020rpcvalue}
package com.digitalasset.quickstart.iou.model {
  package Iou {
    final case class IouTransfer(iou: com.digitalasset.quickstart.iou.model.Iou.Iou, newOwner: ` lfdomainapi`.Primitive.Party) extends ` lfdomainapi`.Template[IouTransfer] {
      override protected[this] def templateCompanion(implicit ` d` : _root_.scala.Predef.DummyImplicit) = IouTransfer
    }

    object IouTransfer extends ` lfdomainapi`.TemplateCompanion[IouTransfer] with _root_.scala.Function2[com.digitalasset.quickstart.iou.model.Iou.Iou, ` lfdomainapi`.Primitive.Party, _root_.com.digitalasset.quickstart.iou.model.Iou.IouTransfer] {
      import _root_.scala.language.higherKinds;
      trait view[` C`[_]] extends ` lfdomainapi`.encoding.RecordView[` C`, view] { $u0020view =>
        val iou: ` C`[com.digitalasset.quickstart.iou.model.Iou.Iou];
        val newOwner: ` C`[` lfdomainapi`.Primitive.Party];
        final override def hoist[` D`[_]](` f` : _root_.scalaz.~>[` C`, ` D`]): view[` D`] = {
          final class $anon extends _root_.scala.AnyRef with view[` D`] {
            override val iou = ` f`(` view`.iou);
            override val newOwner = ` f`(` view`.newOwner)
          };
          new $anon()
        }
      };
      override val id = ` templateId`(packageId = `Package IDs`.Iou, moduleName = "Iou", entityName = "IouTransfer");
      final implicit class `IouTransfer syntax`[+` ExOn`](private val id: ` ExOn`) extends _root_.scala.AnyVal {
        def exerciseIouTransfer_Cancel(actor: ` lfdomainapi`.Primitive.Party, choiceArgument: com.digitalasset.quickstart.iou.model.Iou.IouTransfer_Cancel)(implicit ` exOn` : ` lfdomainapi`.encoding.ExerciseOn[` ExOn`, IouTransfer]): ` lfdomainapi`.Primitive.Update[` lfdomainapi`.Primitive.ContractId[com.digitalasset.quickstart.iou.model.Iou.Iou]] = ` exercise`(id, "IouTransfer_Cancel", _root_.scala.Some(` lfdomainapi`.Value.encode(choiceArgument)));
        def exerciseIouTransfer_Cancel(actor: ` lfdomainapi`.Primitive.Party)(implicit ` exOn` : ` lfdomainapi`.encoding.ExerciseOn[` ExOn`, IouTransfer]): ` lfdomainapi`.Primitive.Update[` lfdomainapi`.Primitive.ContractId[com.digitalasset.quickstart.iou.model.Iou.Iou]] = exerciseIouTransfer_Cancel(actor, _root_.com.digitalasset.quickstart.iou.model.Iou.IouTransfer_Cancel());
        def exerciseIouTransfer_Reject(actor: ` lfdomainapi`.Primitive.Party, choiceArgument: com.digitalasset.quickstart.iou.model.Iou.IouTransfer_Reject)(implicit ` exOn` : ` lfdomainapi`.encoding.ExerciseOn[` ExOn`, IouTransfer]): ` lfdomainapi`.Primitive.Update[` lfdomainapi`.Primitive.ContractId[com.digitalasset.quickstart.iou.model.Iou.Iou]] = ` exercise`(id, "IouTransfer_Reject", _root_.scala.Some(` lfdomainapi`.Value.encode(choiceArgument)));
        def exerciseIouTransfer_Reject(actor: ` lfdomainapi`.Primitive.Party)(implicit ` exOn` : ` lfdomainapi`.encoding.ExerciseOn[` ExOn`, IouTransfer]): ` lfdomainapi`.Primitive.Update[` lfdomainapi`.Primitive.ContractId[com.digitalasset.quickstart.iou.model.Iou.Iou]] = exerciseIouTransfer_Reject(actor, _root_.com.digitalasset.quickstart.iou.model.Iou.IouTransfer_Reject());
        def exerciseArchive(actor: ` lfdomainapi`.Primitive.Party, choiceArgument: com.digitalasset.quickstart.iou.model.DA.Internal.Template.Archive)(implicit ` exOn` : ` lfdomainapi`.encoding.ExerciseOn[` ExOn`, IouTransfer]): ` lfdomainapi`.Primitive.Update[` lfdomainapi`.Primitive.Unit] = ` exercise`(id, "Archive", _root_.scala.Some(` lfdomainapi`.Value.encode(choiceArgument)));
        def exerciseArchive(actor: ` lfdomainapi`.Primitive.Party)(implicit ` exOn` : ` lfdomainapi`.encoding.ExerciseOn[` ExOn`, IouTransfer]): ` lfdomainapi`.Primitive.Update[` lfdomainapi`.Primitive.Unit] = exerciseArchive(actor, _root_.com.digitalasset.quickstart.iou.model.DA.Internal.Template.Archive());
        def exerciseIouTransfer_Accept(actor: ` lfdomainapi`.Primitive.Party, choiceArgument: com.digitalasset.quickstart.iou.model.Iou.IouTransfer_Accept)(implicit ` exOn` : ` lfdomainapi`.encoding.ExerciseOn[` ExOn`, IouTransfer]): ` lfdomainapi`.Primitive.Update[` lfdomainapi`.Primitive.ContractId[com.digitalasset.quickstart.iou.model.Iou.Iou]] = ` exercise`(id, "IouTransfer_Accept", _root_.scala.Some(` lfdomainapi`.Value.encode(choiceArgument)));
        def exerciseIouTransfer_Accept(actor: ` lfdomainapi`.Primitive.Party)(implicit ` exOn` : ` lfdomainapi`.encoding.ExerciseOn[` ExOn`, IouTransfer]): ` lfdomainapi`.Primitive.Update[` lfdomainapi`.Primitive.ContractId[com.digitalasset.quickstart.iou.model.Iou.Iou]] = exerciseIouTransfer_Accept(actor, _root_.com.digitalasset.quickstart.iou.model.Iou.IouTransfer_Accept())
      };
      override val consumingChoices: Set[` lfdomainapi`.Primitive.ChoiceId] = ` lfdomainapi`.Primitive.ChoiceId.subst(Set("IouTransfer_Cancel", "IouTransfer_Reject", "Archive", "IouTransfer_Accept"));
      override def toNamedArguments(` self` : IouTransfer) = ` arguments`(scala.Tuple2("iou", ` lfdomainapi`.Value.encode(` self`.iou)), scala.Tuple2("newOwner", ` lfdomainapi`.Value.encode(` self`.newOwner)));
      override def fromNamedArguments(` r` : ` rpcvalue`.Record) = if (` r`.fields.length.==(2))
        ` r`.fields(0) match {
          case ` rpcvalue`.RecordField((""| "iou"), _root_.scala.Some(zv0)) => (` lfdomainapi`.Value.decode[com.digitalasset.quickstart.iou.model.Iou.Iou](zv0) match {
            case _root_.scala.Some(z0) => (` r`.fields(1) match {
              case ` rpcvalue`.RecordField((""| "newOwner"), _root_.scala.Some(zv1)) => (` lfdomainapi`.Value.decode[` lfdomainapi`.Primitive.Party](zv1) match {
                case _root_.scala.Some(z1) => Some(IouTransfer(z0, z1))
                case _root_.scala.None => _root_.scala.None
              })
              case _ => _root_.scala.None
            })
            case _root_.scala.None => _root_.scala.None
          })
          case _ => _root_.scala.None
        }
      else
        _root_.scala.None;
      override def fieldEncoding(lte: ` lfdomainapi`.encoding.LfTypeEncoding): view[lte.Field] = {
        object `view ` extends view[lte.Field] {
          val iou = lte.field("iou", ` lfdomainapi`.encoding.LfEncodable.encoding[com.digitalasset.quickstart.iou.model.Iou.Iou](lte));
          val newOwner = lte.field("newOwner", ` lfdomainapi`.encoding.LfEncodable.encoding[` lfdomainapi`.Primitive.Party](lte))
        };
        `view `
      };
      override def encoding(lte: ` lfdomainapi`.encoding.LfTypeEncoding)(`view `: view[lte.Field]): lte.Out[_root_.com.digitalasset.quickstart.iou.model.Iou.IouTransfer] = {
        val `recordFields `: lte.RecordFields[_root_.com.digitalasset.quickstart.iou.model.Iou.IouTransfer] = lte.RecordFields.xmapN(lte.fields(`view `.iou), lte.fields(`view `.newOwner))({
          case scala.Tuple2(iou, newOwner) => _root_.com.digitalasset.quickstart.iou.model.Iou.IouTransfer(iou, newOwner)
        })({
          case _root_.com.digitalasset.quickstart.iou.model.Iou.IouTransfer(iou, newOwner) => scala.Tuple2(iou, newOwner)
        });
        lte.record(` dataTypeId`, `recordFields `)
      }
    }
  }
}
