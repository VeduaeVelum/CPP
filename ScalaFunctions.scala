package main_classes

/**
 * @Class ScalaFunctions Class sort replays fields with the quick sort
 */

class ScalaFunctions {
  def qsort(xs: Array[Int], l: Int, r: Int) {
    val pivot = xs((l + r) / 2)
    var i = l
    var j = r
    while (i <= j) {
      while (xs(i) < pivot) i += 1
      while (xs(j) > pivot) j -= 1
      if (i <= j) {
        val temp = xs(i)
        xs(i) = xs(j)
        xs(j) = temp
        i += 1
        j -= 1
      }
    }
    if (l < j) qsort(xs, l, j)
    if (i < r) qsort(xs, i, r)
  }
  
  def qsort(xs: Array[Long], l: Int, r: Int) {
    val pivot = xs((l + r) / 2)
    var i = l
    var j = r
    while (i <= j) {
      while (xs(i) < pivot) i += 1
      while (xs(j) > pivot) j -= 1
      if (i <= j) {
        val temp = xs(i)
        xs(i) = xs(j)
        xs(j) = temp
        i += 1
        j -= 1
      }
    }
    if (l < j) qsort(xs, l, j)
    if (i < r) qsort(xs, i, r)
  }   
 }