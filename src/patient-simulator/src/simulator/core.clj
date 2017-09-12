(ns simulator.core
  (:use clojure.java.io
        clojure.tools.namespace
        incanter.distributions
        simulator.simulation
        [simulator.simulation utils standard]
        simulator.simulation.coding.read
        [simulator.pathways utils]
        simulator.time)  
  (:gen-class))

(defn- construct-sim-ns [sim-file]
  (symbol (second (read-file-ns-decl (as-file sim-file)))))

(defn- construct-fn [ns f]
  (eval (symbol (str ns "/" f))))

(defn -main
  "Run a simulation"
  [& args]
  (if (not (= (count args) 4))
    (do (println "usage... blah")
        (System/exit 0))
    (let [[name number journal patients] args
          sim-ns (construct-sim-ns name)]
      (println "simulating" number "patients from" name)
      (load-file name)
      (require sim-ns)
      (simulate-patients
       (Integer. number)
       (construct-fn sim-ns (quote *patient-fn*))
       (construct-fn sim-ns (quote *lifeline-fn*))
       (construct-fn sim-ns (quote *journal-map*))
       (construct-fn sim-ns (quote *journal-formatter*))
       (construct-fn sim-ns (quote *patient-formatter*))
       (str name)
       journal
       patients)
      (println "...done")
      (System/exit 0))))
