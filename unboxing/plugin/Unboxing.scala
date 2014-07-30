package unboxing.plugin

import scala.tools.nsc.Global
import scala.tools.nsc.Phase
import scala.tools.nsc.plugins._
import scala.tools.nsc.transform._

/** Main unboxing class */ 
class Unboxing(val global: Global) extends Plugin {
  import global._

  val name = "unboxing"
  val description = "Unbox simple cases"

  lazy val components = {
    List[PluginComponent](PreErasurePhase, PostErasurePhase)
  }

  var flag_log = sys.props.get("unboxing.log").isDefined
  var flag_debug = sys.props.get("unboxing.debug").isDefined
  var flag_stats = sys.props.get("unboxing.stats").isDefined

  override def processOptions(options: List[String], error: String => Unit) {
    options.foreach(option =>
      option.toLowerCase() match {
        case "log" => 
          flag_log = true
        case "debug" =>
          flag_debug = true
        case "stats" =>
          flag_stats = true
        case _ =>
          error("Unboxing: Option not understood: " + option)
      })
  }

  override val optionsHelp: Option[String] = Some(Seq(
    s"  -P:${name}:log    print ${name} signature transformations",
    s"  -P:${name}:debug  print ${name} debug logging",
    s"  -P:${name}:stats  print ${name} tree transformations and statistics"
  ).mkString("\n"))

  private object PreErasurePhase extends PluginComponent {
    val global: Unboxing.this.global.type = Unboxing.this.global
    val runsAfter = List()
    override val runsRightAfter = Some("posterasure")
    val phaseName = "ub-preerasure"

    def newPhase(_prev: Phase) = new StdPhase(_prev) {
      override def name = PreErasurePhase.phaseName
      def apply(unit: CompilationUnit) {
        import global._
        import global.Flag._
        println(Unboxing.this.optionsHelp)
        println(Unboxing.this.flag_log)
        println(name + " arrived")
      }
    }
  }

  private object PostErasurePhase extends PluginComponent {
    val global: Unboxing.this.global.type = Unboxing.this.global
    val runsAfter = List()
    override val runsRightAfter = Some("ub-preerasure")
    val phaseName = "ub-posterasure"

    def newPhase(_prev: Phase) = new StdPhase(_prev) {
      override def name = PostErasurePhase.phaseName
      def apply(unit: CompilationUnit) {
        import global._
        import global.Flag._
        println(name + " arrived")
      }
    }
  }
}
