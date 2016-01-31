package tutorial.webapp


import scala.scalajs.js.JSApp
import org.scalajs.dom.document
import Diagram._

object TutorialApp extends JSApp {
  def main(): Unit = {
    val diag = PieDiagram(
      r = 20, rr = 40,
      sectors = Seq(
      PieSector(1, "green", Some("1N")),
      PieSector(1.2, "yellow", Some("Le")),
      PieSector(1.1, "blue", Some("3A")),
      PieSector(2, "red", Some("4R")),
      PieSector(1, "gray", None),
      PieSector(1.7, "orange", None)
    ))

    val r = PieDiagram(
      r = 40, rr = 80,
      sectors = Seq(
        PieSector(1, "green", None),
        PieSector(1, "gray", None)
      ))

    val star = Tree(node = diag, leaves = Seq(
      Tree(node = diag),
      Tree(node = diag),
      Tree(node = diag),
      Tree(node = diag),
      Tree(node = diag),
      Tree(node = diag),
      Tree(node = diag)
    ))

    val t = Tree(node = r, leaves =
      (1 to 10 map (_ => Tree(node = diag))) ++
        (1 to 3 map (_ => star))
    )

    val pt = position(t)
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
    implicit val treePie = new TreeDomRepr[PieDiagram]

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

}
