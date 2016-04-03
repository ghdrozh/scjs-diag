package tutorial

import scala.language.implicitConversions

import scalajs.js
import org.scalajs.dom
import dom.Node

import scalatags.JsDom.all._
import rx._



package object webapp {

  type Size = Double

  trait Spatial[A] {
    def size(a: A): Size
    def bounds(a: A): Bounds
  }

  object Spatial {
    implicit def spatialOption[A](implicit spatialA: Spatial[A]): Spatial[Option[A]] = new Spatial[Option[A]]() {
      override def size(opt: Option[A]): Size = opt match {
        case None => 0d
        case Some(a) => spatialA.size(a)
      }

      override def bounds(opt: Option[A]): Bounds = opt match {
        case None => Bounds(0d,0d,0d,0d)
        case Some(a) => spatialA.bounds(a)
      }
    }
  }

  trait DomRepr[A] {
    def render(a: A): Node
  }

  object DomRepr {
    implicit def domReprOption[A](implicit domReprA: DomRepr[A]): DomRepr[Option[A]] = new DomRepr[Option[A]] {
      override def render(opt: Option[A]): Node = opt match {
        case None => scalatags.JsDom.svgTags.circle().render
        case Some(a) => domReprA.render(a)
      }
    }
  }

  case class Bounds(minHeight: Double, maxHeight: Double, minWidth: Double, maxWidth: Double) {
    import math.{min, max}
    def +(other: Bounds) = Bounds(
      min(minHeight, other.minHeight),
      max(maxHeight, other.maxHeight),
      min(minWidth, other.minWidth),
      max(maxWidth, other.maxWidth))
    def shift(p: Point) = Bounds(
      minHeight + p.y,
      maxHeight + p.y,
      minWidth + p.x,
      maxWidth + p.x)
  }

  object Bounds {
    def square(sz: Size) = Bounds(-sz, sz, -sz, sz)
  }

  case class Tree[A](node: A, leaves: Seq[Tree[A]] = Seq.empty)

  def size[A](a: A)(implicit spatial: Spatial[A]): Size = spatial.size(a)
  def renderDom[A](a: A)(implicit domRepr: DomRepr[A]): Node = domRepr.render(a)

  def position[A: Spatial](t: Tree[A]): Tree[(Point, Size, A)] = t match {
    case Tree(n, Nil) => Tree((Point.zero, size(n), n), Nil)
    case Tree(n, leaves) =>
      val pleaves = leaves.map(position(_)(implicitly[Spatial[A]]))
      val r0 = size(n)
      val szs = pleaves.map(_.node._2)
      var optDist = szs.map(r1 => 1.1 * (r1 + r0))
      var sectors = optDist zip szs map { case (d, r) => 2 * Math.asin(r / d) }
      val scale = sectors.sum / 2 / Math.PI
      if (scale > 1) {
        optDist = optDist.map(_ * scale)
        sectors = optDist zip szs map { case (d, r) => 2 * Math.asin(r / d) }
      }
      val angles = sectors.scanLeft(0.0)(_ + _).tail zip sectors.map(_ / 2) map {
        case (l, r) => l - r
      }
      Tree((Point.zero, optDist zip szs map (p => p._1 + p._2) max, n),
        for (((alpha, d), t) <- angles zip optDist zip pleaves)
          yield t.copy(t.node.copy(_1= Point(d, 0.0).rotate(alpha)))
      )
  }

  def bounds[A](t: Tree[(Point, Size, A)])(implicit spatial: Spatial[A]): Bounds = {
    val Tree((p, sz, a), leaves) = t
    val lb: Option[Bounds] = leaves.map(bounds(_)(spatial)).reduceOption(_ + _).map(_.shift(p))
    val slf = spatial.bounds(a).shift(p)
    if (lb.isDefined) slf + lb.get else slf
  }

  implicit def rxFrag[T <% Frag](r: Rx[T])(implicit ownerCtx: Ctx.Owner): Frag = {
    def rSafe: dom.Node = r.now.render
    var last = rSafe
    r.triggerLater {
      val newLast = rSafe
      js.Dynamic.global.last = last
      last.parentNode.replaceChild(newLast, last)
      last = newLast
    }
    last
  }

}
