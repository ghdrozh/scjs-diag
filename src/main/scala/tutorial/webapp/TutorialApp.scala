package tutorial.webapp


import scala.scalajs.js.JSApp
import org.scalajs.dom.document
import tutorial.model.{Trade, TradeSearchService}
import rx._
import scala.collection.immutable.TreeSet

object TutorialApp extends JSApp {
  def main(): Unit = {
    implicit val ownerCtx: Ctx.Owner = Ctx.Owner.safe()

    val trades: Seq[TradeCtx] = TradeSearchService.find("as").map(TradeCtx(_))

    val pt = position(diagramOfTrades(trades))
    val bnds: Bounds = bounds(pt)

    val maxWidth = 700.0
    var w = bnds.maxWidth - bnds.minWidth
    var h = bnds.maxHeight - bnds.minHeight
    var scl: Option[Double] = None
    if (w > maxWidth) {
      scl = Some(maxWidth / w)
      w = maxWidth
      h = h * scl.get
    }

    val svgFrag = {
      import scalatags.JsDom
      import JsDom.all.{SeqFrag, OptionNode, _}
      import JsDom.svgAttrs.{transform}
      import JsDom.svgTags._

      svg(height := s"$h", width := s"$w", border := "1px solid")(
        g(
          transform := scl.map(s => s" scale($s)").getOrElse("") +
            s" translate(${-bnds.minWidth}, ${-bnds.minHeight})"
          ,
          renderDom(pt)
        )
      ).render
    }

    document.body.appendChild(svgFrag)

    def tbl = {
      import scalatags.JsDom.all._
      val clmns = trades.map(_.trade.details.keySet).fold(TreeSet.empty[String])(_ ++ _).map(_.capitalize)

      def row(t: TradeCtx) = {
        Rx {
          if (t.selected())
            tr(
              Seq(
                td(t.trade.transactionType.toString),
                td(t.trade.status.toString),
                td(t.trade.tid),
                td(t.trade.v),
                td(t.trade.mid)
              ) ++ clmns.map(col => td(format(t.trade.details.getOrElse(col, ""))))
            )
          else tr(display := "none")
        }
      }

      table(
        thead(
          tr(
            Seq(
              th("Transaction type"),
              th("Status"),
              th("Tid"),
              th("Version"),
              th("Mid")
            ) ++ clmns.map(th(_))
          )
        ),
        tbody(
          for (t <- trades) yield row(t)
        )
      ).render
    }
    document.body.appendChild(tbl)
  }

  def format(v: AnyRef): String = v match {
    case s: String => s
    case o: Object => o.toString
    case _ => "unknown"
  }

  def diagramOfTrades(trades: Seq[TradeCtx]): Tree[Option[PieDiagram]] = {

    val groups = trades.groupBy { t => t.trade.mid match {
      case Some(m) if m == t.trade.tid => (t.trade.tid, None)
      case _ => (t.trade.tid, t.trade.mid)
    }}.mapValues(s =>
        Some(PieDiagram(r = 60, rr = 100, sectors = s map toPieSector))
    )
    val tids = groups.keySet.map(_._1)
    val (roots, leaves) = groups.partition {
      case (((_, None), _)) => true
      case (((tid, Some(mid)), _)) => !tids.contains(mid) // dangling pointer
    }
    def buildTree(tid: String, nodes: Map[(String, Option[String]), Option[PieDiagram]]): Seq[Tree[Option[PieDiagram]]] = {
      val (lf, other) = nodes.partition {
        case (((_, Some(mid)), _)) => tid == mid
      }
      (for (((tid, _), pd) <- lf) yield Tree(pd, buildTree(tid, other))).toSeq
    }
    val res = (for (((tid, _), pd) <- roots) yield Tree(pd, buildTree(tid, leaves))).toSeq
    if (res.size == 1) res.head else Tree(None, res)
  }

  def toPieSector(t: TradeCtx) = {
    PieSector(
      weight = 1d,
      cssClass = s"pie-sect-${t.trade.status.toLowerCase}",
      label = Option(s"${t.trade.v}${t.trade.transactionType.take(1).toUpperCase}"),
      selected = t.selected
    )
  }

  case class TradeCtx(trade: Trade, selected: Var[Boolean] = Var(false))
}







