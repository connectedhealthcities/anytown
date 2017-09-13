(defproject simulator "1.0.0"
  :description "Tool for simulating large corpuses of patient data."
  :url "https://www.connectedhealthcities.org/"
  :license {:name "University of Manchester"
            :url "http://umip.com/"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/data.priority-map "0.0.7"]
                 [org.clojure/algo.generic "0.1.2"]
                 [org.clojure/tools.namespace "0.2.11"]
                 [net.mikera/core.matrix "0.61.0"]]
  :jvm-opts ["-Djava.awt.headless=true"]
  :aot [simulator.core]
  :main simulator.core)
