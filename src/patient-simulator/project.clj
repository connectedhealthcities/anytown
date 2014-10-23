(defproject simulator "1.0.0"
  :description "Tool for simulating large corpuses of patient data."
  :url "http://nweh.org.uk/"
  :license {:name "Copyright NWEH (2013)"
            :url "http://nweh.org.uk"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/data.priority-map "0.0.2"]
                 [org.clojure/algo.generic "0.1.0"]
                 [org.clojure/tools.namespace "0.2.3"]
                 [incanter "1.4.0"]]
  :jvm-opts ["-Djava.awt.headless=true"]
  :aot [simulator.core]
  :main simulator.core)
