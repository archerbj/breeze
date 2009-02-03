// THIS IS AN AUTO-GENERATED FILE. DO NOT MODIFY.    
// generated by GenCounter on Mon Feb 02 23:53:39 PST 2009
package scalanlp.counters.floats;
/*
 Copyright 2009 David Hall, Daniel Ramage
 
 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at 
 
 http://www.apache.org/licenses/LICENSE-2.0
 
 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License. 
*/
import scala.collection.mutable.Map;
import scala.collection.mutable.HashMap;

/**
 * Count objects of type Float with type Int.
 * This trait is a wrapper around Scala's Map trait
 * and can work with any scala Map. 
 *
 * @author dlwh
 */
@serializable 
trait Float2IntCounter extends IntCounter[Float] {


  abstract override def update(k : Float, v : Int) = {

    super.update(k,v);
  }

  // this isn't necessary, except that the jcl MapWrapper overrides put to call Java's put directly.
  override def put(k : Float, v : Int) :Option[Int] = { val old = get(k); update(k,v); old}

  abstract override def -=(key : Float) = {

    super.-=(key);
  }

  /**
   * Increments the count by the given parameter.
   */
   override  def incrementCount(t : Float, v : Int) = {
     update(t,(this(t) + v).asInstanceOf[Int]);
   }


  override def ++=(kv: Iterable[(Float,Int)]) = kv.foreach(+=);

  /**
   * Increments the count associated with Float by Int.
   * Note that this is different from the default Map behavior.
  */
  override def +=(kv: (Float,Int)) = incrementCount(kv._1,kv._2);

  override def default(k : Float) : Int = 0;

  override def apply(k : Float) : Int = super.apply(k);

  // TODO: clone doesn't seem to work. I think this is a JCL bug.
  override def clone(): Float2IntCounter  = super.clone().asInstanceOf[Float2IntCounter]

  /**
   * Return the Float with the largest count
   */
  override  def argmax() : Float = (elements reduceLeft ((p1:(Float,Int),p2:(Float,Int)) => if (p1._2 > p2._2) p1 else p2))._1

  /**
   * Return the Float with the smallest count
   */
  override  def argmin() : Float = (elements reduceLeft ((p1:(Float,Int),p2:(Float,Int)) => if (p1._2 < p2._2) p1 else p2))._1

  /**
   * Return the largest count
   */
  override  def max : Int = values reduceLeft ((p1:Int,p2:Int) => if (p1 > p2) p1 else p2)
  /**
   * Return the smallest count
   */
  override  def min : Int = values reduceLeft ((p1:Int,p2:Int) => if (p1 < p2) p1 else p2)

  // TODO: decide is this is the interface we want?
  /**
   * compares two objects by their counts
   */ 
  override  def comparator(a : Float, b :Float) = apply(a) compare apply(b);

  /**
   * Return a new Float2DoubleCounter with each Int divided by the total;
   */
  override  def normalized() : Float2DoubleCounter = {
    val normalized = Float2DoubleCounter();
    val total : Double = this.total
    if(total != 0.0)
      for (pair <- elements) {
        normalized(pair._1) = pair._2 / total;
      }
    normalized
  }

  /**
   * Return the sum of the squares of the values
   */
  override  def l2norm() : Double = {
    var norm = 0.0
    for (val v <- values) {
      norm += (v * v)
    }
    return Math.sqrt(norm)
  }

  /**
   * Return a List the top k elements, along with their counts
   */
  override  def topK(k : Int) = Counters.topK[(Float,Int)](k,(x,y) => if(x._2 < y._2) -1 else if (x._2 == y._2) 0 else 1)(this);

  /**
   * Return \sum_(t) C1(t) * C2(t). 
   */
  def dot(that : Float2IntCounter) : Double = {
    var total = 0.0
    for (val (k,v) <- that.elements) {
      total += apply(k).asInstanceOf[Double] * v
    }
    return total
  }

  def +=(that : Float2IntCounter) {
    for(val (k,v) <- that.elements) {
      update(k,(this(k) + v).asInstanceOf[Int]);
    }
  }

  def -=(that : Float2IntCounter) {
    for(val (k,v) <- that.elements) {
      update(k,(this(k) - v).asInstanceOf[Int]);
    }
  }

  override  def *=(scale : Int) {
    transform { (k,v) => (v * scale).asInstanceOf[Int]}
  }

  override  def /=(scale : Int) {
    transform { (k,v) => (v / scale).asInstanceOf[Int]}
  }

  override  def unary_-() = {
      val rv : IntCounter[Float] = Float2IntCounter();
      rv -= this;
      rv;
  }
}


object Float2IntCounter {
  import it.unimi.dsi.fastutil.objects._
  import it.unimi.dsi.fastutil.ints._
  import it.unimi.dsi.fastutil.shorts._
  import it.unimi.dsi.fastutil.longs._
  import it.unimi.dsi.fastutil.floats._
  import it.unimi.dsi.fastutil.doubles._


  import scala.collection.jcl.MapWrapper;
  @serializable
  @SerialVersionUID(1L)
  class FastMapCounter extends MapWrapper[Float,Int] with Float2IntCounter {
    private val under = new Float2IntOpenHashMap;
    def underlying() = under.asInstanceOf[java.util.Map[Float,Int]];
    override def apply(x : Float) = under.get(x);
    override def update(x : Float, v : Int) {
      val oldV = this(x);
      updateTotal(v-oldV);
      under.put(x,v);
    }
  }

  def apply() = new FastMapCounter();

  
}

