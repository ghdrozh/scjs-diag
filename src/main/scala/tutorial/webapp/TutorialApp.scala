package tutorial.webapp


import scala.scalajs.js.JSApp
import org.scalajs.dom.document
import tutorial.model.{Trade, TradeSearchService}

object TutorialApp extends JSApp {
  def main(): Unit = {
    val pt = position(renderTrades(TradeSearchService.find("as")))
    val bnds: Bounds = bounds(pt)

    val maxWidth = 1280
    var w = bnds.maxWidth - bnds.minWidth
    var h = bnds.maxHeight - bnds.minHeight
    var scl: Option[Double] = None
    if (w > maxWidth) {
      w = maxWidth
      scl = Some(maxWidth / w)
      h = h * scl.get
    }

    val svgFrag = {
      import scalatags.JsDom
      import JsDom.all.{SeqFrag, OptionNode, _}
      import JsDom.svgAttrs.{transform}
      import JsDom.svgTags._

      svg(height := s"$h", width := s"$w", border := "1px solid")(
        g(
          transform := s"translate(${-bnds.minWidth}, ${-bnds.minHeight})",
          scl.map(s => transform := s"scale($s)"),
          renderDom(pt)
        )
      ).render
    }

    document.body.appendChild(svgFrag)
  }

  def renderTrades(trades: Seq[Trade]): Tree[Option[PieDiagram]] = {
    val groups = trades.groupBy(t => (t.tid, t.mid)).mapValues(s =>
        Some(PieDiagram(r = 20, rr = 40, sectors = s map toPieSector))
    )
    val tids = groups.keySet.map(_._1)
    val (roots, leaves) = groups.partition {
      case (((_, None), _)) => true
      case (((tid, Some(mid)), _)) => !tids.contains(mid) || mid == tid // dangling pointer
    }
    def walkTree(tid: String, nodes: Map[(String, Option[String]), Option[PieDiagram]]): Seq[Tree[Option[PieDiagram]]] = {
      val (lf, other) = nodes.partition {
        case (((_, Some(mid)), _)) => tid == mid
      }
      (for (((tid, _), pd) <- lf) yield Tree(pd, walkTree(tid, other))).toSeq
    }
    val res = (for (((tid, _), pd) <- roots) yield Tree(pd, walkTree(tid, leaves))).toSeq
    if (res.size == 1) res.head else Tree(None, res)
  }

  def toPieSector(trade: Trade): PieSector = PieSector(1d, "green", Option(s"${trade.v}"))

}
