plugins
=======
- How to compile

  $ cd unboxing/plugin
  $ mkdir classes
  $ fsc -d classes unboxing.scala
  $ cp scalac-plugin.xml classes
  $ cd classes; jar cf ../unboxing.jar .

- How to run

$ scalac -Xplugin:unboxing.jar example.scala