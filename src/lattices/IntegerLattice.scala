package tapy.lattices

import tapy.dfa._

/*  Assuming that the python program is running on a 32 bit computer.
    This limits integers in python to -2**31-1 <= x <= 2**32-1. */
object IntegerLattice {
  sealed trait Elt extends MergeLattice.Elt
  
  case class Concrete(i: Int) extends Elt
  case class Bottom() extends Elt
  case class Abstract() extends Elt
} 

class IntegerLattice extends Lattice[IntegerLattice.Elt] {
  def top: IntegerLattice.Elt = IntegerLattice.Abstract()
  def bottom: IntegerLattice.Elt = IntegerLattice.Bottom()
  
  // a >= b
  def compare(a: IntegerLattice.Elt, b: IntegerLattice.Elt) = (a, b) match {
    case (IntegerLattice.Abstract(), _)  => true
    case (_, IntegerLattice.Bottom()) => true
    case (IntegerLattice.Concrete(i),IntegerLattice.Concrete(j)) => (i == j)
  }

  def leastUpperBound(a: IntegerLattice.Elt, b: IntegerLattice.Elt) = (a, b) match {
    case (IntegerLattice.Abstract(), _) =>                           IntegerLattice.Abstract()
    case (_, IntegerLattice.Abstract()) =>                           IntegerLattice.Abstract()

    case (IntegerLattice.Bottom(), IntegerLattice.Bottom()) =>  IntegerLattice.Bottom()

    case (IntegerLattice.Concrete(i),IntegerLattice.Bottom()) => IntegerLattice.Concrete(i)
    case (IntegerLattice.Bottom(),IntegerLattice.Concrete(i)) => IntegerLattice.Concrete(i)

    case (IntegerLattice.Concrete(i),IntegerLattice.Concrete(j)) => if (i.equals(j)) IntegerLattice.Concrete(j) else IntegerLattice.Abstract()

    case (_, _) =>  IntegerLattice.Abstract()
  }

  def greatestLowerBound(a: IntegerLattice.Elt, b: IntegerLattice.Elt) = (a, b) match {
    case (IntegerLattice.Bottom(), _) =>                        IntegerLattice.Bottom()
    case (_, IntegerLattice.Bottom()) =>                        IntegerLattice.Bottom()

    case (IntegerLattice.Abstract(), IntegerLattice.Abstract()) =>  IntegerLattice.Abstract()


    case (IntegerLattice.Concrete(i),IntegerLattice.Abstract()) => IntegerLattice.Concrete(i)
    case (IntegerLattice.Abstract(),IntegerLattice.Concrete(i)) => IntegerLattice.Concrete(i)

    case (IntegerLattice.Concrete(i),IntegerLattice.Concrete(j)) => if (i.equals(j)) IntegerLattice.Concrete(j) else IntegerLattice.Bottom()

    case (_, _) =>  IntegerLattice.Bottom()
  }
}