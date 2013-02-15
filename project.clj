(defproject simulator "1.0.0"
  :description "Tool for simulating large corpuses of patient data."
  :url "http://nweh.org.uk/"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [org.clojure/data.priority-map "0.0.2"]
                 [org.clojure/algo.generic "0.1.0"]
                 [incanter "1.4.0"]]
  :jvm-opts ["-Djava.awt.headless=true"])
