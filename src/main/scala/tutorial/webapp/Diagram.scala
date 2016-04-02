package tutorial.webapp

import org.scalajs.dom.{Event, Node}
import scala.language.postfixOps
import scalatags.JsDom


sealed trait Diagram
case class PieSector(weight: Double, color: String, label: Option[String]) extends Diagram
case class PieDiagram(r: Double = 40, rr: Double = 80, gap: Double = 0.05, sectors: Seq[PieSector]) extends Diagram


object Diagram {

  implicit def treeDomRepr[A](implicit domReprA: DomRepr[A]) = new DomRepr[Tree[(Point, Double, A)]]() {
    override def render(t: Tree[(Point, Double, A)]): Node = {
      import JsDom.short._
      import JsDom.svgAttrs._
      import JsDom.svgTags._
      import JsDom.all.{OptionNode, SeqNode}

      def go(orig: Option[Point], t: Tree[(Point, Double, A)]): JsDom.Frag = {
        val Tree((p@Point(px, py), sz, node), leaves) = t
        g(orig.map(op => line(
          x1 := 0, y1 := 0, x2 := px, y2 := py,
          stroke := "black", strokeWidth := 2)
        ),
          g(transform := s"translate($px, $py)") (
            leaves.map(go(Some(p), _)),
            implicitly[DomRepr[A]].render(node)
          )
        )
      }
      go(None, t).render
    }
  }

  implicit object SpatialPieD extends Spatial[PieDiagram] {
    override def size(a: PieDiagram): Size = a.rr
    override def bounds(a: PieDiagram): Bounds = Bounds.square(a.rr)
  }

  implicit object pieDiagramDom extends DomRepr[PieDiagram] {
    import JsDom.short._
    import JsDom.svgAttrs._
    import JsDom.svgTags._
    import JsDom.all.SeqNode
    override def render(diagram: PieDiagram) = {

      import JsDom.all.{SeqFrag, OptionNode, _}
      import JsDom.svgAttrs

      val PieDiagram(r, rr, gap, sectors) = diagram
      val weights = sectors.map(_.weight)
      val wsum = weights.sum
      val arcs = weights.map(_ / wsum * 2 * Math.PI).scanLeft(0.0)(_ + _).map(_ - Math.PI / 2)

      def toggleSelection(ps: PieSector, p: Point) = {
        var selected = false
        (event: Event) => {
          selected = !selected
          val trg = event.currentTarget.asInstanceOf[org.scalajs.dom.raw.SVGElement]
          if (selected) trg.setAttribute("transform", s"translate(${p.x} ${p.y})")
          else trg.removeAttribute("transform")
        }
      }

      (for (
        ((alpha, beta), ps) <- arcs zip arcs.tail zip sectors
      ) yield {
          val R = rr * 0.1
          val gamma: Double = (alpha + beta) / 2
          g(
            arc(
              r, rr,
              alpha + gap,
              beta - gap,
              svgAttrs.fill := ps.color),
            ps.label.map(svgLabel(r, rr, gamma, _))
          )(
            cursor := "pointer",
            onclick := toggleSelection(ps, Point(R * math.cos(gamma), R * math.sin(gamma)))
          )
        }
        ).render
    }

    def arc(r0: Double, r1: Double, alpha: Double, beta: Double, xs: JsDom.Modifier*) = {
      val p1@Point(x1, y1) = Point(r0, 0).rotate(alpha)
      val p2@Point(x2, y2) = Point(r0, 0).rotate(beta)
      val p3@Point(x3, y3) = Point(r1, 0).rotate(beta)
      val p4@Point(x4, y4) = Point(r1, 0).rotate(alpha)
      val largeArc = if (beta - alpha > Math.PI) 1 else 0
      val points = Seq(p1, p2, p3, p4)

      g(
        path(d :=
          s"""M $x1 $y1
              |A $r0 $r0 0 $largeArc 1 $x2 $y2
              |L $x3 $y3
              |A $r1 $r1 0 $largeArc 0 $x4 $y4
              |Z
           """.stripMargin
        )(SeqNode(xs))
      )
    }

    def svgLabel(r0: Double, r1: Double, alpha: Double, label: String) = {
      val Point(px, py) = Point((r0 + r1) / 2, 0).rotate(alpha)
      val fontSz = (r1 -r0) / 2
      text(x := px, y := py,
        textAnchor := "middle",
        fontSize := fontSz,
        alignmentBaseline := "middle"
      )(label)
    }
  }

}